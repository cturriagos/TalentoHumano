/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.DetalleHorario;
import Model.DAO.DiaSemanaDAO;
import Model.Entidad.HorarioLaboral;
import Model.Interfaces.IDAO;
import com.sun.istack.internal.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;

/**
 *
 * @author rturr
 */
public class HorarioLaboralDAO implements IDAO<HorarioLaboral>{
    protected final Conexion conexion;
    private HorarioLaboral horarioLaboral;

    public HorarioLaboralDAO() {
        conexion = new Conexion();
        horarioLaboral = new HorarioLaboral();
    }

    public HorarioLaboralDAO(Conexion conexion) {
        this.conexion = conexion;
        horarioLaboral = new HorarioLaboral();
    }

    public HorarioLaboralDAO(HorarioLaboral horarioLaboral) {
        conexion = new Conexion();
        this.horarioLaboral = horarioLaboral;
    }

    public HorarioLaboralDAO(Conexion conexion, HorarioLaboral horarioLaboral) {
        this.conexion = conexion;
        this.horarioLaboral = horarioLaboral;
    }

    public HorarioLaboral getHorarioLaboral() {
        return horarioLaboral;
    }

    public void setHorarioLaboral(HorarioLaboral horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            horarioLaboral.setId(conexion.insertar("horario_laboral",
                    "nombre, estado, observaciones, fecha_vigencia",
                    "'" + horarioLaboral.getNombre() + "', "+ horarioLaboral.isEstado() + ",'" + horarioLaboral.getObservaciones()+ "','" + horarioLaboral.getFechaVigencia() + "'", true));
            return horarioLaboral.getId();
        }
        return -1;
    }

    @Override
    public int insertar(HorarioLaboral entity) {
        this.horarioLaboral = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("horario_laboral",
                    "nombre= '" + horarioLaboral.getNombre() + "', estado = " + horarioLaboral.isEstado() + ", observaciones = '" + horarioLaboral.getObservaciones()+ "', fecha_vigencia ='" + horarioLaboral.getFechaVigencia() + "'" ,
                    "id_horario_laboral = " + horarioLaboral.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(HorarioLaboral entity) {
        this.horarioLaboral = entity;
        return actualizar();
    }

    @Override
    public HorarioLaboral buscarPorId(Object id) {
        List<HorarioLaboral> lista = buscar("id_horario_laboral = " + id, "nombre");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<HorarioLaboral> Listar() {
        return buscar(null, "nombre");
    }
    
    private List<HorarioLaboral> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<HorarioLaboral> horarios;
            try {
                result = conexion.selecionar("horario_laboral", "id_horario_laboral, nombre, estado, observaciones, fecha_vigencia", restricciones, OrdenarAgrupar);
                horarios = new ArrayList<>();
                while (result.next()) {
                    horarios.add(
                            new HorarioLaboral(
                                    result.getInt("id_horario_laboral"),
                                    result.getString("nombre"),
                                    result.getBoolean("estado"),
                                    result.getString("observaciones"),
                                    result.getDate("fecha_vigencia")
                            )
                    );
                }
                result.close();
                return horarios;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }

    public int cambiarEstado() {
        if (conexion.isEstado()) {
            return conexion.modificar("horario_laboral", "estado = NOT estado","id_horario_laboral = " + horarioLaboral.getId());
        }
        return -1;
    }
    
    public int insertarDetalle(List<DetalleHorario> detalles){
        try {
            if(conexion.iniciarTransaccion()){
                for( DetalleHorario detalle : detalles ){
                    conexion.ejecutarInsertarToTrnasaccion("detalle_horario", 
                            "id_ingreso_salida, id_horario_laboral, id__dia_semana",
                            detalle.getIngresoSalida() + ", " + detalle.getHorarioLaboral() + "," + detalle.getDiaSemana());
                }
            }
            conexion.finalizarTransaccion(true);
            return 1;
        }catch (ClassNotFoundException | SQLException exSQL) {
            try {
                conexion.setMensaje(exSQL.getMessage());
                System.out.println(conexion.getMensaje());
                conexion.setTipoMensaje(FacesMessage.SEVERITY_FATAL);
                conexion.finalizarTransaccion(false);
            } catch (SQLException ex) {
                Logger.getLogger(HorarioLaboralDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
                return -1;
        }
    }
    
    public List<DetalleHorario> ListarDetalle(HorarioLaboral horarioLaboral){
        this.horarioLaboral = horarioLaboral;
        return ListarDetalle();
    }
    
    public List<DetalleHorario> ListarDetalle(){
        if(conexion.isEstado()){
            ResultSet result;
            List<DetalleHorario> detalles;
            try {
                result = conexion.ejecutarConsulta("SELECT id_ingreso_salida, id_horario_laboral, id__dia_semana FROM detalle_horario horario_laboral WHERE id_horario_laboral = " + horarioLaboral.getId());
                detalles = new ArrayList<>();
                IngresosSalidasDAO ingresosSalidasDAO = new IngresosSalidasDAO(conexion);
                DiaSemanaDAO diaSemanaDAO = new DiaSemanaDAO(conexion);
                while (result.next()) {
                    detalles.add(
                            new DetalleHorario(
                                    ingresosSalidasDAO.buscarPorId(result.getInt("id_horario_laboral")),
                                    horarioLaboral,
                                    diaSemanaDAO.buscarPorId(result.getInt("id__dia_semana"))
                            )
                    );
                }
                result.close();
                return detalles;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}