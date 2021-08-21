/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoPuesto;
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
 * @author cturriagos
 */
@Named
@ApplicationScoped
public class EmpleadoPuestoDAO implements IDAO<EmpleadoPuesto> {

    private Conexion conexion;
    private EmpleadoPuesto empleadoPuesto;

    public EmpleadoPuestoDAO() {
        conexion = new Conexion();
        empleadoPuesto = new EmpleadoPuesto();
    }

    public EmpleadoPuestoDAO(EmpleadoPuesto empleadoPuesto) {
        conexion = new Conexion();
        this.empleadoPuesto = empleadoPuesto;
    }

    public EmpleadoPuestoDAO(Conexion conexion) {
        this.conexion = conexion;
        empleadoPuesto = new EmpleadoPuesto();
    }

    public EmpleadoPuestoDAO(EmpleadoPuesto empleadoPuesto, Conexion conexion) {
        this.conexion = conexion;
        this.empleadoPuesto = empleadoPuesto;
    }

    public EmpleadoPuesto getEmpleadoPuesto() {
        return empleadoPuesto;
    }

    public void setEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) {
        this.empleadoPuesto = empleadoPuesto;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            empleadoPuesto.setId(conexion.insertar("empleado_puesto",
                    "id_empleado, id_puesto, id_horario_laboral, fecha_cambio, estado, observaciones",
                    empleadoPuesto.getEmpleado().getId() + ", " + empleadoPuesto.getPuestoLaboral().getId() + ", "
                    + empleadoPuesto.getHorarioLaboral().getId() + ", CURRENT_TIMESTAMP, "
                    + empleadoPuesto.isEstado()+ ", '" + empleadoPuesto.getObservaciones()+ "'",
                    "id_empleado_puesto"));
            return empleadoPuesto.getId();
        }
        return -1;
    }

    @Override
    public int insertar(EmpleadoPuesto entity) {
        this.empleadoPuesto = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("empleado_puesto",
                    "id_empleado = " + empleadoPuesto.getEmpleado().getId() + ", id_puesto = " + empleadoPuesto.getPuestoLaboral().getId()
                    + ", id_horario_laboral = " + empleadoPuesto.getHorarioLaboral().getId() + ", fecha_cambio = '"
                    + empleadoPuesto.getFechaCambio()+ "', estado = " + empleadoPuesto.isEstado()+ ", observaciones = '" + empleadoPuesto.getObservaciones()+ "'",
                    "id_empleado_puesto = " + empleadoPuesto.getId());
        }
        return -1;
    }
    
    public void desactivar() {
        if (conexion.isEstado()) {
            conexion.modificar("empleado_puesto", "estado = false ",
                    "estado = true AND id_empleado = " + empleadoPuesto.getEmpleado().getId());
        }
    }

    @Override
    public int actualizar(EmpleadoPuesto entity) {
        this.empleadoPuesto = entity;
        return actualizar();
    }

    @Override
    public EmpleadoPuesto buscarPorId(Object id) {
        List<EmpleadoPuesto> lista = buscar("id_empleado_puesto = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new EmpleadoPuesto();
    }

    @Override
    public List<EmpleadoPuesto> Listar() {
        return buscar(null, "fecha_cambio DESC");
    }
    
    public EmpleadoPuesto buscar(Empleado empleado){
        empleadoPuesto = new EmpleadoPuesto();
        empleadoPuesto.setEmpleado(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_puesto", 
                        "id_empleado_puesto, id_puesto, id_horario_laboral, fecha_cambio, observaciones", 
                        "estado = true AND id_empleado = " + empleado.getId(), null);
                PuestoLaboralDAO pldao = new PuestoLaboralDAO();
                HorarioLaboralDAO hldao = new HorarioLaboralDAO();
                while (result.next()) {
                     empleadoPuesto.setId(result.getInt("id_empleado_puesto"));
                     empleadoPuesto.setPuestoLaboral(pldao.buscarPorId(result.getInt("id_puesto")));
                     empleadoPuesto.setHorarioLaboral(hldao.buscarPorId(result.getInt("id_horario_laboral")));
                     empleadoPuesto.setFechaCambio(result.getDate("fecha_cambio"));
                     empleadoPuesto.setObservaciones(result.getString("observaciones"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoPuesto;
    }
    

    private List<EmpleadoPuesto> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<EmpleadoPuesto> empleadoPuestos = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_puesto", "id_empleado_puesto, id_empleado, id_puesto, id_horario_laboral, fecha_cambio, estado, observaciones", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                PuestoLaboralDAO pldao = new PuestoLaboralDAO();
                HorarioLaboralDAO hldao = new HorarioLaboralDAO();
                while (result.next()) {
                    empleadoPuestos.add(new EmpleadoPuesto(
                            result.getInt("id_empleado_puesto"),
                            edao.buscarPorId(result.getInt("id_empleado")),
                            pldao.buscarPorId(result.getInt("id_puesto")),
                            hldao.buscarPorId(result.getInt("id_horario_laboral")),
                            result.getDate("fecha_cambio"),
                            result.getBoolean("estado"),
                            result.getString("observaciones")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoPuestos;
    }
}
