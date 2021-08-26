/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Amonestacion;
import Model.Entidad.Empleado;
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
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class AmonestacionDAO implements IDAO<Amonestacion> {

    private final Conexion conexion;
    private Amonestacion amonestacion;

    public AmonestacionDAO() {
        this.conexion = new Conexion();
        this.amonestacion = new Amonestacion();
    }

    public AmonestacionDAO(Conexion conexion) {
        this.conexion = conexion;
        this.amonestacion = new Amonestacion();
    }

    public AmonestacionDAO(Amonestacion amonestacion) {
        this.conexion = new Conexion();
        this.amonestacion = amonestacion;
    }

    public AmonestacionDAO(Conexion conexion, Amonestacion amonestacion) {
        this.conexion = conexion;
        this.amonestacion = amonestacion;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            return conexion.insertar("amonestacion", "id_empleado, tipo, valor, detalle, estado", 
                    amonestacion.getEmpleado().getId()+ ",'" + amonestacion.getTipo()+ "'," + amonestacion.getValor() + ",'"
                    + amonestacion.getDetalle() + "'," + amonestacion.isEstado(), "id_amonestacion");
        }
        return -1;
    }

    @Override
    public int insertar(Amonestacion entity) {
        this.amonestacion = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("amonestacion",
                    "tipo = '" + amonestacion.getTipo()+ "', valor = " + amonestacion.getValor() + ", detalle = '"
                    + amonestacion.getDetalle() + "', estado =" + amonestacion.isEstado(),
                    "id_amonestacion = " + amonestacion.getId() + " AND id_empleado = " + amonestacion.getEmpleado().getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Amonestacion entity) {
        this.amonestacion = entity;
        return actualizar();
    }

    @Override
    public Amonestacion buscarPorId(Object id) {
        List<Amonestacion> lista = buscar("id_amonestacion = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Amonestacion> Listar() {
        return buscar(null, null);
    }
    
    public List<Amonestacion> Listar(Empleado empleado) {
        List<Amonestacion> amonestaciones = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("amonestacion", "id_amonestacion, tipo, valor, detalle",
                        "id_empleado = " + empleado.getId() + " AND estado = true", null);
                while (result.next()) {
                    amonestaciones.add(new Amonestacion(
                            result.getInt("id_amonestacion"),
                            empleado,
                            result.getString("tipo"),
                            result.getString("detalle"),
                            result.getFloat("valor"), true));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return amonestaciones;
    }
    
    public Amonestacion buscar(Empleado empleado) {
        amonestacion = new Amonestacion(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("amonestacion", "id_amonestacion, tipo, valor, detalle",
                        "id_empleado = " + empleado.getId() + " AND estado = true", null);
                while (result.next()) {
                    amonestacion.setId(result.getInt("id_amonestacion"));
                    amonestacion.setTipo(result.getString("tipo"));
                    amonestacion.setDetalle( result.getString("detalle"));
                    amonestacion.setValor( result.getFloat("valor"));
                    amonestacion.setEstado(true);
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return amonestacion;
    }

    private List<Amonestacion> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Amonestacion> amonestaciones = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("amonestacion", "id_amonestacion, id_empleado, tipo, valor, detalle, estado",
                        restricciones, OrdenarAgrupar);
                EmpleadoDAO eDAO = new EmpleadoDAO();
                while (result.next()) {
                    amonestaciones.add(new Amonestacion(
                            result.getInt("id_amonestacion"),
                            (amonestacion.getEmpleado().getId()>0? amonestacion.getEmpleado() : eDAO.buscarPorId(result.getInt("id_empleado"))),
                            result.getString("tipo"),
                            result.getString("detalle"),
                            result.getFloat("valor"),
                            result.getBoolean("estado")));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return amonestaciones;
    }
}