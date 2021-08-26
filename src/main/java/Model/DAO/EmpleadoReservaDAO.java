/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoReserva;
import Model.Interfaces.IDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author kestradalp
 */
@Named
@ApplicationScoped
public class EmpleadoReservaDAO implements IDAO<EmpleadoReserva> {

    private Conexion conexion;
    private EmpleadoReserva empleadoReserva;

    public EmpleadoReservaDAO() {
        conexion = new Conexion();
        empleadoReserva = new EmpleadoReserva();
    }

    public EmpleadoReservaDAO(EmpleadoReserva empleadoReserva) {
        conexion = new Conexion();
        this.empleadoReserva = empleadoReserva;
    }

    public EmpleadoReservaDAO(Conexion conexion) {
        this.conexion = conexion;
        empleadoReserva = new EmpleadoReserva();
    }

    public EmpleadoReservaDAO(EmpleadoReserva empleadoReserva, Conexion conexion) {
        this.conexion = conexion;
        this.empleadoReserva = empleadoReserva;
    }

    public EmpleadoReserva getEmpleadoReserva() {
        return empleadoReserva;
    }

    public void setEmpleadoReserva(EmpleadoReserva empleadoReserva) {
        this.empleadoReserva = empleadoReserva;
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
            conexion.insertar("empleado_reserva",
                    "id_empleado, fecha_solicitud, forma_pago, detalle",
                    empleadoReserva.getEmpleado().getId() + ", '" + empleadoReserva.getFechaSolicitud()+ "', " + 
                    empleadoReserva.getFormaPago() + ", '" + empleadoReserva.getDetalle() + "'" ,
                    "id_empleado");
            return empleadoReserva.getEmpleado().getId();
        }
        return -1;
    }

    @Override
    public int insertar(EmpleadoReserva entity) {
        this.empleadoReserva = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("empleado_reserva",
                    "fecha_solicitud = '" + empleadoReserva.getFechaSolicitud() + "', detalle = '" + empleadoReserva.getDetalle() 
                    + "', forma_pago = " + empleadoReserva.getFormaPago(),
                    "id_empleado = " + empleadoReserva.getEmpleado().getId());
        }
        return -1;
    }

    @Override
    public int actualizar(EmpleadoReserva entity) {
        this.empleadoReserva = entity;
        return actualizar();
    }

    @Override
    public EmpleadoReserva buscarPorId(Object id) {
        List<EmpleadoReserva> lista = buscar("id_empleado = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new EmpleadoReserva();
    }

    @Override
    public List<EmpleadoReserva> Listar() {
        return buscar(null, "fecha_solicitud DESC");
    }
    
    public EmpleadoReserva buscar(Empleado empleado) {
        empleadoReserva = new EmpleadoReserva();
        empleadoReserva.setEmpleado(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_reserva", "fecha_solicitud, forma_pago, detalle",
                                             "id_empleado = " + empleado.getId(), null);
                while (result.next()) {
                    empleadoReserva.setFechaSolicitud(result.getDate("fecha_solicitud"));
                    empleadoReserva.setFormaPago(result.getFloat("forma_pago"));
                    empleadoReserva.setDetalle(result.getString("detalle"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoReserva;
    }

    private List<EmpleadoReserva> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<EmpleadoReserva> empleadoReservas = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_reserva", "id_empleado, fecha_solicitud, forma_pago, detalle", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                while (result.next()) {
                    empleadoReservas.add(new EmpleadoReserva(
                            edao.buscarPorId(result.getInt("id_empleado")),
                            result.getDate("fecha_actualizacion"),
                            result.getFloat("forma_pago"),
                            result.getString("detalle")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoReservas;
    }   
}