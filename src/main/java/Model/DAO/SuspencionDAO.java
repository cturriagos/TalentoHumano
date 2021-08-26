/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.Suspencion;
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
public class SuspencionDAO implements IDAO<Suspencion> {

    private final Conexion conexion;
    private Suspencion suspencion;

    public SuspencionDAO() {
        this.conexion = new Conexion();
        this.suspencion = new Suspencion();
    }

    public SuspencionDAO(Conexion conexion) {
        this.conexion = conexion;
        this.suspencion = new Suspencion();
    }

    public SuspencionDAO(Suspencion suspencion) {
        this.conexion = new Conexion();
        this.suspencion = suspencion;
    }

    public SuspencionDAO(Conexion conexion, Suspencion suspencion) {
        this.conexion = conexion;
        this.suspencion = suspencion;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            return conexion.insertar("suspencion", "id_empleado, cantidad_dias, valor, detalle, estado", 
                    suspencion.getEmpleado().getId()+ "," + suspencion.getCantidadDias() + "," + suspencion.getValor() + ",'"
                    + suspencion.getDetalle() + "'," + suspencion.isEstado(), "id_suspencion");
        }
        return -1;
    }

    @Override
    public int insertar(Suspencion entity) {
        this.suspencion = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("suspencion",
                    "cantidad_dias = " + suspencion.getCantidadDias()+ ", valor = " + suspencion.getValor() + ", detalle = '"
                    + suspencion.getDetalle() + "', estado =" + suspencion.isEstado(),
                    "id_suspencion = " + suspencion.getId() + " AND id_empleado = " + suspencion.getEmpleado().getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Suspencion entity) {
        this.suspencion = entity;
        return actualizar();
    }

    @Override
    public Suspencion buscarPorId(Object id) {
        List<Suspencion> lista = buscar("id_suspencion = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Suspencion> Listar() {
        return buscar(null, null);
    }
    
    public List<Suspencion> Listar(Empleado empleado) {
        List<Suspencion> suspenciones = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("suspencion", "id_suspencion, cantidad_dias, valor, detalle",
                        "id_empleado = " + empleado.getId() + " AND estado = true", null);
                while (result.next()) {
                    suspenciones.add(new Suspencion(
                            result.getInt("id_suspencion"),
                            result.getInt("cantidad_dias"),
                            empleado,
                            result.getFloat("valor"),
                            result.getString("detalle"), true));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return suspenciones;
    }

public Suspencion buscar(Empleado empleado) {
        suspencion = new Suspencion(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("suspencion", "id_suspencion, cantidad_dias, valor, detalle",
                        "id_empleado = " + empleado.getId() + " AND estado = true", null);
                while (result.next()) {
                    suspencion.setId(result.getInt("id_suspencion"));
                    suspencion.setCantidadDias(result.getInt("cantidad_dias"));
                    suspencion.setValor(result.getFloat("valor"));
                    suspencion.setDetalle(result.getString("detalle"));
                    suspencion.setEstado(true);
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return suspencion;
    }

    private List<Suspencion> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Suspencion> suspenciones = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("suspencion", "id_suspencion, id_empleado, cantidad_dias, valor, detalle, estado",
                        restricciones, OrdenarAgrupar);
                suspenciones = new ArrayList<>();
                EmpleadoDAO eDAO = new EmpleadoDAO();
                while (result.next()) {
                    suspenciones.add(new Suspencion(
                            result.getInt("id_suspencion"),
                            result.getInt("cantidad_dias"),
                            (suspencion.getEmpleado().getId()>0? suspencion.getEmpleado() : eDAO.buscarPorId(result.getInt("id_empleado"))),
                            result.getFloat("valor"),
                            result.getString("detalle"),
                            result.getBoolean("estado")));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return suspenciones;
    }
}