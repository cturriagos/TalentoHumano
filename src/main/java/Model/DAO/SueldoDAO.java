/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.Sueldo;
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
 * @author kestradalp
 */
@Named
@ApplicationScoped
public class SueldoDAO  implements IDAO<Sueldo> {

    private Conexion conexion;
    private Sueldo sueldo;

    public SueldoDAO() {
        conexion = new Conexion();
        sueldo = new Sueldo();
    }

    public SueldoDAO(Sueldo sueldo) {
        conexion = new Conexion();
        this.sueldo = sueldo;
    }

    public SueldoDAO(Conexion conexion) {
        this.conexion = conexion;
        sueldo = new Sueldo();
    }

    public SueldoDAO(Sueldo sueldo, Conexion conexion) {
        this.conexion = conexion;
        this.sueldo = sueldo;
    }

    public Sueldo getSueldo() {
        return sueldo;
    }

    public void setSueldo(Sueldo sueldo) {
        this.sueldo = sueldo;
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
            sueldo.setId(conexion.insertar("sueldo",
                    "id_empleado, valor, fecha_actualizacion, estado",
                    sueldo.getEmpleado().getId() + ", " + sueldo.getValor()+ ", CURRENT_TIMESTAMP, " + sueldo.isEstado(),
                    "id_sueldo"));
            return sueldo.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Sueldo entity) {
        this.sueldo = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("sueldo",
                    "id_empleado = " + sueldo.getEmpleado().getId() + ", valor = " + sueldo.getValor()
                    + ", fecha_actualizacion = '" + sueldo.getFechaActualizacion()+ "', estado = " + sueldo.isEstado(),
                    "id_sueldo = " + sueldo.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Sueldo entity) {
        this.sueldo = entity;
        return actualizar();
    }

    public void desactivar() {
        if (conexion.isEstado()) {
            conexion.ejecutarProcedure("desactivarsueldo", "" + sueldo.getEmpleado().getId());
        }
    }

    @Override
    public Sueldo buscarPorId(Object id) {
        List<Sueldo> lista = buscar("id_sueldo = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new Sueldo();
    }

    @Override
    public List<Sueldo> Listar() {
        return buscar(null, "fecha_actualizacion DESC");
    }
    
    public Sueldo Actual(Empleado empleado) {
        sueldo = new Sueldo();
        sueldo.setEmpleado(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("sueldo", "id_sueldo, valor, fecha_actualizacion",
                                             "estado = true AND id_empleado = " + empleado.getId(), null);
                while (result.next()) {
                    sueldo.setId(result.getInt("id_sueldo"));
                    sueldo.setValor(result.getFloat("valor"));
                    sueldo.setFechaActualizacion(result.getDate("fecha_actualizacion"));
                    sueldo.setEstado(true);
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return sueldo;
    }
    
    public List<Sueldo> historial(Empleado empleado) {
        List<Sueldo> sueldos = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("sueldo", "id_sueldo, valor, fecha_actualizacion, estado",
                                             "id_empleado = " + empleado.getId(), "fecha_actualizacion DESC");
                while (result.next()) {
                    sueldos.add( new Sueldo(result.getInt("id_sueldo"), empleado, result.getFloat("valor"), 
                                        result.getDate("fecha_actualizacion"), result.getBoolean("estado")));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return sueldos;
    }

    private List<Sueldo> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Sueldo> sueldos = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("sueldo", "id_sueldo, id_empleado, valor, fecha_actualizacion, estado", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                while (result.next()) {
                    sueldos.add(new Sueldo(
                            result.getInt("id_sueldo"),
                            (sueldo.getEmpleado().getId()>0? sueldo.getEmpleado() : edao.buscarPorId(result.getInt("id_empleado"))),
                            result.getFloat("valor"),
                            result.getDate("fecha_actualizacion"),
                            result.getBoolean("estado")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return sueldos;
    }
}