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
import com.sun.istack.internal.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Lenin
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
                    + ", fecha_actualizacion = '" + sueldo.getFechaActualizacion()+ "', , estado = " + sueldo.isEstado(),
                    "id_sueldo = " + sueldo.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Sueldo entity) {
        this.sueldo = entity;
        return actualizar();
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
    
    public Sueldo buscar(Empleado empleado) {
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("sueldo", "id_sueldo, valor, fecha_actualizacion",
                                             "estado = true AND id_empleado = " + empleado.getId(), null);
                PuestoLaboralDAO pldao = new PuestoLaboralDAO();
                HorarioLaboralDAO hldao = new HorarioLaboralDAO();
                while (result.next()) {
                    sueldo = new Sueldo(result.getInt("id_sueldo"), empleado, result.getFloat("valor"), 
                                        result.getDate("fecha_actualizacion"), true);
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

    private List<Sueldo> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Sueldo> sueldos = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("sueldo", "id_sueldo, id_empleado, valor, fecha_actualizacion, estado", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                PuestoLaboralDAO pldao = new PuestoLaboralDAO();
                HorarioLaboralDAO hldao = new HorarioLaboralDAO();
                while (result.next()) {
                    sueldos.add(new Sueldo(
                            result.getInt("id_sueldo"),
                            edao.buscarPorId(result.getInt("id_empleado")),
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