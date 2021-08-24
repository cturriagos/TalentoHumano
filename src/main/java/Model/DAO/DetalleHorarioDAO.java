/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.DetalleHorario;
import Model.Entidad.EmpleadoPuesto;
import Model.Entidad.HorarioLaboral;
import Model.Interfaces.IDAO;
import org.jetbrains.annotations.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class DetalleHorarioDAO implements IDAO<DetalleHorario> {

    private final Conexion conexion;
    private DetalleHorario detalleHorario;

    public DetalleHorarioDAO() {
        conexion = new Conexion();
        detalleHorario = new DetalleHorario();
    }

    public DetalleHorarioDAO(DetalleHorario detalleHorario) {
        conexion = new Conexion();
        this.detalleHorario = detalleHorario;
    }

    public DetalleHorarioDAO(Conexion conexion) {
        this.conexion = conexion;
        detalleHorario = new DetalleHorario();
    }

    public DetalleHorarioDAO(DetalleHorario detalleHorario, Conexion conexion) {
        this.conexion = conexion;
        this.detalleHorario = detalleHorario;
    }

    public DetalleHorario getDetalleHorario() {
        return detalleHorario;
    }

    public void setDetalleHorario(DetalleHorario detalleHorario) {
        this.detalleHorario = detalleHorario;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            return conexion.insertar("detalle_horario",
                    "id_ingreso_salida, id_horario_laboral, id_dia_semana, estado",
                    detalleHorario.getIngresoSalida().getId() + "," + detalleHorario.getHorarioLaboral().getId() + "," + detalleHorario.getDiaSemana().getId() + "," + detalleHorario.isEstado(),
                    "id_detalle_horario");
        }
        return -1;
    }

    @Override
    public int insertar(DetalleHorario entity) {
        this.detalleHorario = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("detalle_horario",
                    "id_ingreso_salida = " + detalleHorario.getIngresoSalida().getId() + ", id_horario_laboral = " + detalleHorario.getHorarioLaboral().getId() + ", id_dia_semana = " + detalleHorario.getDiaSemana().getId() + ", estado = " + detalleHorario.isEstado(),
                    "id_detalle_horario = " + detalleHorario.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(DetalleHorario entity) {
        this.detalleHorario = entity;
        return actualizar();
    }

    @Override
    public DetalleHorario buscarPorId(Object id) {
        List<DetalleHorario> lista = buscar("id_detalle_horario = " + id + " AND id_horario_laboral = " + detalleHorario.getHorarioLaboral().getId(), null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public DetalleHorario buscarPorId(int id, HorarioLaboral horarioLaboral) {
        detalleHorario = new DetalleHorario();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("detalle_horario",
                                             "id_ingreso_salida, id_dia_semana, estado", "id_horario_laboral = " 
                                                     + horarioLaboral.getId() + " AND id_detalle_horario = " + id, null);
                DiaSemanaDAO diaDAO = new DiaSemanaDAO();
                IngresosSalidasDAO isDAO = new IngresosSalidasDAO();
                while (result.next()) {
                    detalleHorario.setId(id);
                    detalleHorario.setIngresoSalida(isDAO.buscarPorId(result.getInt("id_ingreso_salida")));
                    detalleHorario.setHorarioLaboral(horarioLaboral);
                    detalleHorario.setDiaSemana(diaDAO.buscarPorId(result.getInt("id_dia_semana")));
                    detalleHorario.setEstado(result.getBoolean("estado"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return detalleHorario;
    }

    @Override
    public List<DetalleHorario> Listar() {
        return buscar(null, null);
    }
    
    public List<DetalleHorario> Listar(int idHorarioLaboral) {
        return buscar("id_horario_laboral = " + idHorarioLaboral , null);
    }

    public List<DetalleHorario> buscar(EmpleadoPuesto empleadoPuesto) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<DetalleHorario> detalles;
            try {
                detalleHorario.setHorarioLaboral(empleadoPuesto.getHorarioLaboral());
                result = conexion.selecionar("detalle_horario AS dh INNER JOIN public.horario_laboral AS hl ON hl.id_horario_laboral = dh.id_horario_laboral",
                                             "id_detalle_horario, id_ingreso_salida, dh.id_horario_laboral, id_dia_semana, dh.estado",
                                             "hl.id_horario_laboral = " + detalleHorario.getHorarioLaboral().getId() 
                                                     + " AND hl.estado = true AND dh.estado = true",
                                             null);
                detalles = new ArrayList<>();
                DiaSemanaDAO diaDAO = new DiaSemanaDAO();
                IngresosSalidasDAO ingresoSalidaDAO = new IngresosSalidasDAO();
                while (result.next()) {
                    detalles.add(new DetalleHorario(result.getInt("id_detalle_horario"),
                            ingresoSalidaDAO.buscarPorId(result.getInt("id_ingreso_salida")),
                            detalleHorario.getHorarioLaboral(),
                            diaDAO.buscarPorId(result.getInt("id_dia_semana")),
                            result.getBoolean("estado")
                    ));
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

    private List<DetalleHorario> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<DetalleHorario> detalles;
            try {
                result = conexion.selecionar("detalle_horario", "id_detalle_horario, id_ingreso_salida, id_horario_laboral, id_dia_semana, estado", restricciones, OrdenarAgrupar);
                detalles = new ArrayList<>();
                DiaSemanaDAO diaDAO = new DiaSemanaDAO();
                IngresosSalidasDAO ingresoSalidaDAO = new IngresosSalidasDAO();
                while (result.next()) {
                    detalles.add(new DetalleHorario(result.getInt("id_detalle_horario"),
                            ingresoSalidaDAO.buscarPorId(result.getInt("id_ingreso_salida")),
                            detalleHorario.getHorarioLaboral(),
                            diaDAO.buscarPorId(result.getInt("id_dia_semana")),
                            result.getBoolean("estado")
                    ));
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
