/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.Persona;
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
public class EmpleadoDAO implements IDAO<Empleado> {

    private Conexion conexion;
    private Empleado empleado;
    private PersonaDAO personaDAO;

    public EmpleadoDAO() {
        conexion = new Conexion();
        empleado = new Empleado();
        personaDAO = new PersonaDAO(conexion);
    }

    public EmpleadoDAO(Empleado empleado) {
        conexion = new Conexion();
        this.empleado = empleado;
    }

    public EmpleadoDAO(Conexion conexion) {
        this.conexion = conexion;
        empleado = new Empleado();
    }

    public EmpleadoDAO(Empleado empleado, Conexion conexion) {
        this.conexion = conexion;
        this.empleado = empleado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
            empleado.setId(conexion.insertar("empleado_bck_rrhh",
                    "id_persona, nombre1, nombre2, apellido1, apellido2, sexo, genero, detalle, fecha_nacimiento, fecha_ingreso, fecha_egreso",
                    empleado.getPersona().getId() + ", '" + empleado.getNombre1() + "', '" + empleado.getNombre2() + "', '" + empleado.getApellido1() 
                    + "', '" + empleado.getApellido2() + "', '" + empleado.getSexo() + "', '" + empleado.getGenero() + "', '" + empleado.getDetalle()
                    + "', '" + empleado.getFechaNacimiento() + "', CURRENT_TIMESTAMP, NULL",
                    "id_empleado"));
            return empleado.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Empleado entity) {
        this.empleado = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("empleado_bck_rrhh",
                    "id_persona = " + empleado.getPersona().getId() + ", nombre1 = '" + empleado.getNombre1() + "', nombre2 = '"
                    + empleado.getNombre2() + "', apellido1 = '" + empleado.getApellido1() + "', apellido2 = '" + empleado.getApellido2() + "', sexo = '" 
                    + empleado.getSexo() + "', genero = '" + empleado.getGenero() + "', detalle = '" + empleado.getDetalle() + "', fecha_nacimiento = '" 
                    + empleado.getFechaNacimiento() + "', fecha_ingreso = '" + empleado.getFechaIngreso() + "'",
                    "id_empleado = " + empleado.getId());
        }
        return -1;
    }
    public void cambiarEstado() {
        if (conexion.isEstado()) {
            conexion.modificar("persona_bck_rrhh",
                               "estado = NOT estado", "id_persona = " + empleado.getPersona().getId());
        }
    }

    @Override
    public int actualizar(Empleado entity) {
        this.empleado = entity;
        return actualizar();
    }

    @Override
    public Empleado buscarPorId(Object id) {
        List<Empleado> lista = buscar("id_empleado = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new Empleado();
    }

    @Override
    public List<Empleado> Listar() {
        return buscar(null, "nombre1, nombre2, apellido1, apellido2 ASC");
    }

    public List<Empleado> resumen() {
        List<Empleado> empleados = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_bck_rrhh", "id_empleado, nombre1, nombre2, apellido1, apellido2, fecha_ingreso, fecha_egreso", null, "nombre1, nombre2, apellido1, apellido2 ASC");
                
                while (result.next()) {
                    empleados.add(new Empleado(
                            result.getInt("id_empleado"),
                            new Persona(),
                            result.getString("nombre1"),
                            result.getString("nombre2"),
                            result.getString("apellido1"),
                            result.getString("apellido2"),
                            "","","", null,
                            result.getDate("fecha_ingreso"),
                            result.getDate("fecha_egreso")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleados;
    }
    
    public List<Empleado> activos() {
        List<Empleado> empleados = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_bck_rrhh AS e INNER JOIN public.persona_bck_rrhh AS p ON p.id_persona = e.id_persona",
                                             "id_empleado, e.id_persona, nombre1, nombre2, apellido1, apellido2, sexo, genero, detalle, fecha_nacimiento, fecha_ingreso, fecha_egreso",
                                             "p.estado = true",
                                             "nombre1, nombre2, apellido1, apellido2 ASC");
                while (result.next()) {
                    empleados.add(new Empleado(
                            result.getInt("id_empleado"),
                            personaDAO.buscarPorId(result.getInt("id_persona")),
                            result.getString("nombre1"),
                            result.getString("nombre2"),
                            result.getString("apellido1"),
                            result.getString("apellido2"),
                            result.getString("sexo"),
                            result.getString("genero"),
                            result.getString("detalle"),
                            result.getDate("fecha_nacimiento"),
                            result.getDate("fecha_ingreso"),
                            result.getDate("fecha_egreso")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleados;
    }

    private List<Empleado> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<Empleado> empleados = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("empleado_bck_rrhh", "id_empleado, id_persona, nombre1, nombre2, apellido1, apellido2, sexo, genero, detalle, fecha_nacimiento, fecha_ingreso, fecha_egreso", restricciones, OrdenarAgrupar);
                while (result.next()) {
                    empleados.add(new Empleado(
                            result.getInt("id_empleado"),
                            personaDAO.buscarPorId(result.getInt("id_persona")),
                            result.getString("nombre1"),
                            result.getString("nombre2"),
                            result.getString("apellido1"),
                            result.getString("apellido2"),
                            result.getString("sexo"),
                            result.getString("genero"),
                            result.getString("detalle"),
                            result.getDate("fecha_nacimiento"),
                            result.getDate("fecha_ingreso"),
                            result.getDate("fecha_egreso")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return empleados;
    }
}