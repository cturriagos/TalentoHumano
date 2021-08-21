/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoSucursal;
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
public class EmpleadoSucursalDAO implements IDAO<EmpleadoSucursal> {

    private Conexion conexion;
    private EmpleadoSucursal empleadoSucursal;

    public EmpleadoSucursalDAO() {
        conexion = new Conexion();
        empleadoSucursal = new EmpleadoSucursal();
    }

    public EmpleadoSucursalDAO(EmpleadoSucursal empleadoSucursal) {
        conexion = new Conexion();
        this.empleadoSucursal = empleadoSucursal;
    }

    public EmpleadoSucursalDAO(Conexion conexion) {
        this.conexion = conexion;
        empleadoSucursal = new EmpleadoSucursal();
    }

    public EmpleadoSucursalDAO(EmpleadoSucursal empleadoSucursal, Conexion conexion) {
        this.conexion = conexion;
        this.empleadoSucursal = empleadoSucursal;
    }

    public EmpleadoSucursal getEmpleadoSucursal() {
        return empleadoSucursal;
    }

    public void setEmpleadoSucursal(EmpleadoSucursal empleadoSucursal) {
        this.empleadoSucursal = empleadoSucursal;
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
            empleadoSucursal.setId(conexion.insertar("empleado_sucursal",
                    "id_empleado, id_sucursal, fecha_cambio, detalle_cambio, estado",
                    empleadoSucursal.getEmpleado().getId() + ", " + empleadoSucursal.getSucursal().getId()
                    + ", CURRENT_TIMESTAMP, '" + empleadoSucursal.getDetalle() + "', " + empleadoSucursal.isEstado(),
                    "id_empleado_sucursal"));
            return empleadoSucursal.getId();
        }
        return -1;
    }

    @Override
    public int insertar(EmpleadoSucursal entity) {
        this.empleadoSucursal = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("empleado_sucursal",
                    "id_empleado = " + empleadoSucursal.getEmpleado().getId() + ", id_sucursal = " + empleadoSucursal.getSucursal().getId()
                    + ", fecha_cambio = '" + empleadoSucursal.getFechaCambio() + ", detalle_cambio = '"
                    + empleadoSucursal.getDetalle() + "', estado = " + empleadoSucursal.isEstado(),
                    "id_empleado_sucursal = " + empleadoSucursal.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(EmpleadoSucursal entity) {
        this.empleadoSucursal = entity;
        return actualizar();
    }

    @Override
    public EmpleadoSucursal buscarPorId(Object id) {
        List<EmpleadoSucursal> lista = buscar("id_empleado_sucursal = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new EmpleadoSucursal();
    }

    public void desactivar() {
        if (conexion.isEstado()) {
            conexion.modificar("empleado_sucursal", "estado = false ",
                    "estado = true AND id_empleado = " + empleadoSucursal.getEmpleado().getId());
        }
    }

    @Override
    public List<EmpleadoSucursal> Listar() {
        return buscar(null, "fecha_cambio DESC");
    }

    public EmpleadoSucursal buscar(Empleado empleado) {
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_sucursal",
                                             "id_empleado_sucursal, id_sucursal, fecha_cambio, detalle_cambio",
                                             "estado = true AND id_empleado = " + empleado.getId(), null);
                SucursalDAO sdao = new SucursalDAO();
                while (result.next()) {
                    empleadoSucursal = new EmpleadoSucursal(
                            result.getInt("id_empleado_sucursal"),
                                          empleado, sdao.buscarPorId(result.getInt("id_sucursal")),
                                          result.getDate("fecha_cambio"), true, result.getString("detalle_cambio"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoSucursal;
    }

    private List<EmpleadoSucursal> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<EmpleadoSucursal> empleadoSucursals = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_sucursal", "id_empleado_sucursal, id_empleado, id_sucursal, fecha_cambio, detalle_cambio, estado", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                SucursalDAO sdao = new SucursalDAO();
                while (result.next()) {
                    empleadoSucursals.add(new EmpleadoSucursal(
                            result.getInt("id_empleado_sucursal"),
                            edao.buscarPorId(result.getInt("id_empleado")),
                            sdao.buscarPorId(result.getInt("id_sucursal")),
                            result.getDate("fecha_cambio"),
                            result.getBoolean("estado"),
                            result.getString("detalle_cambio")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleadoSucursals;
    }
}
