/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Asistencia;
import Model.Entidad.DetalleHorario;
import Model.Entidad.EmpleadoPuesto;
import Model.Interfaces.IDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
public class AsistenciaDAO implements IDAO<Asistencia>{
    private final Conexion conexion;
    private Asistencia asistencia;

    public AsistenciaDAO() {
        this.conexion = new Conexion();
        this.asistencia = new Asistencia();
    }

    public AsistenciaDAO(Asistencia asistencia) {
        this.conexion = new Conexion();
        this.asistencia = asistencia;
    }

    public AsistenciaDAO(Conexion conexion) {
        this.conexion = conexion;
        this.asistencia = new Asistencia();
    }

    public AsistenciaDAO(Conexion conexion, Asistencia asistencia) {
        this.conexion = conexion;
        this.asistencia = asistencia;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            conexion.insertar("asistencia",
                    "id_empleado_puesto, reg_hora_ingreso, reg_hora_salida, fecha, id_detalle_horario",
                    asistencia.getEmpleadoPuesto().getId() + ", '" + asistencia.getIngreso()+ "', null, '" 
                    + asistencia.getFecha() + "', " + asistencia.getDetalleHorario().getId(), "id_empleado_puesto");
            return asistencia.getDetalleHorario().getId();
        }
        return -1;
    }

    @Override
    public int insertar(Asistencia entity) {
        this.asistencia = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("asistencia",
                    "reg_hora_salida = '" + asistencia.getSalida() + "'",
                    "id_empleado_puesto = " + asistencia.getEmpleadoPuesto().getId() + " AND reg_hora_ingreso = '" 
                    + asistencia.getIngreso()+ "' AND fecha = '" + asistencia.getFecha() 
                    + "' AND id_detalle_horario = " + asistencia.getDetalleHorario().getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Asistencia entity) {
        this.asistencia = entity;
        return actualizar();
    }

    @Override
    public Asistencia buscarPorId(Object id) {
        List<Asistencia> lista = buscar("id_cargaf = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new Asistencia(new EmpleadoPuesto(), null, null, new Date(), new DetalleHorario());
    }

    public Asistencia buscar(EmpleadoPuesto empleadoPuesto, Date fecha, DetalleHorario detalleHorario) {
        asistencia = new Asistencia(empleadoPuesto, null, null, fecha, detalleHorario);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("asistencia", "reg_hora_ingreso, reg_hora_salida",
                                             "id_empleado_puesto = " + empleadoPuesto.getId() + " AND fecha = '" 
                                             + asistencia.getFecha() + "' AND id_detalle_horario = " + detalleHorario.getId(), null);
                while (result.next()) {
                    asistencia.setIngreso(result.getString("reg_hora_ingreso"));
                    asistencia.setSalida(result.getString("reg_hora_salida"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return asistencia;
    }

    public List<Asistencia> buscar(EmpleadoPuesto empleadoPuesto) {
        List<Asistencia> asistencias = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("asistencia", "fecha, reg_hora_ingreso, reg_hora_salida, id_detalle_horario",
                                             "id_empleado_puesto = " + empleadoPuesto.getId(), "fecha DESC");
                DetalleHorarioDAO dhdao = new DetalleHorarioDAO();
                while (result.next()) {
                    asistencias.add(new Asistencia( empleadoPuesto, result.getString("reg_hora_ingreso"),
                                                    result.getString("reg_hora_salida"),  result.getDate("fecha"),
                                                    dhdao.buscarPorId(result.getInt("id_detalle_horario"),
                                                    empleadoPuesto.getHorarioLaboral())));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return asistencias;
    }

    @Override
    public List<Asistencia> Listar() {
        return buscar(null, "fecha DESC");
    }

    private List<Asistencia> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Asistencia> asistencias = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("asistencia",
                        "id_empleado_puesto, reg_hora_ingreso, reg_hora_salida, fecha, id_detalle_horario",
                        restricciones, OrdenarAgrupar);
                EmpleadoPuestoDAO epdao = new EmpleadoPuestoDAO();
                DetalleHorarioDAO dhdao = new DetalleHorarioDAO();
                while (result.next()) {
                    asistencias.add(new Asistencia(
                            epdao.buscarPorId(result.getInt("id_empleado_puesto")),
                            result.getString("reg_hora_ingreso"), result.getString("reg_hora_salida"),
                            result.getDate("fecha"), dhdao.buscarPorId(result.getInt("id_detalle_horario"))
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return asistencias;
    }
}