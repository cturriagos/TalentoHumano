/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empleado;
import Model.Entidad.RolPagos;
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
public class RolPagosDAO implements IDAO<RolPagos> {

    private final Conexion conexion;
    private RolPagos rolPagos;

    public RolPagosDAO() {
        this.conexion = new Conexion();
        this.rolPagos = new RolPagos();
    }

    public RolPagosDAO(Conexion conexion) {
        this.conexion = conexion;
        this.rolPagos = new RolPagos();
    }

    public RolPagosDAO(RolPagos rolPagos) {
        this.conexion = new Conexion();
        this.rolPagos = rolPagos;
    }

    public RolPagosDAO(Conexion conexion, RolPagos rolPagos) {
        this.conexion = conexion;
        this.rolPagos = rolPagos;
    }

    public RolPagos getRolPagos() {
        return rolPagos;
    }

    public void setRolPagos(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            rolPagos.setId( conexion.insertar("rol_de_pagos",
                    "id_empleado, generado_por, fecha_generado, detalle, estado, horas_laboradas, horas_suplemt, valor, codigo",
                    rolPagos.getEmpleado().getId() + ",'" + rolPagos.getUsuario() + "','" + rolPagos.getFechaGenerado() + "','"
                    + rolPagos.getDetalle() + "', '" + rolPagos.getEstado() + "', " + rolPagos.getHorasLaboradas() + "," 
                    + rolPagos.getHorasSuplemetarias() + "," + rolPagos.getValor() + ",'" + rolPagos.getCodigo() + "'",
                    "id_rol"));
        }
        return rolPagos.getId();
    }

    @Override
    public int insertar(RolPagos entity) {
        this.rolPagos = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("rol_de_pagos", "estado = '" + rolPagos.getFechaGenerado() + "'",
                    "id_rol = " + rolPagos.getId() + " AND id_empleado = " + rolPagos.getEmpleado().getId() + " AND generado_por = '"
                    + rolPagos.getUsuario() + " AND codigo = '" + rolPagos.getCodigo() + "'");
        }
        return -1;
    }

    @Override
    public int actualizar(RolPagos entity) {
        this.rolPagos = entity;
        return actualizar();
    }

    @Override
    public RolPagos buscarPorId(Object id) {
        List<RolPagos> lista = buscar("id_rol = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            rolPagos = lista.get(0);
            return rolPagos;
        }
        return null;
    }

    public RolPagos buscar(int id, Empleado empleado) {
        rolPagos = new RolPagos(empleado);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("rol_de_pagos",
                        "fecha_generado, fecha_aprobacion, fecha_pago, detalle, estado, horas_laboradas, horas_suplemt, valor, codigo",
                        "id_empleado = " + empleado.getId() + " AND generado_por = 'ADMINISTRADOR' AND id_rol = " + id, null);
                while (result.next()) {
                    rolPagos.setId(id);
                    rolPagos.setFechaGenerado(result.getDate("fecha_generado"));
                    rolPagos.setFechaAprobacion(result.getDate("fecha_aprobacion"));
                    rolPagos.setFechaPago(result.getDate("fecha_pago"));
                    rolPagos.setDetalle(result.getString("detalle"));
                    rolPagos.setEstado(result.getString("estado"));
                    rolPagos.setHorasLaboradas(result.getFloat("horas_laboradas"));
                    rolPagos.setHorasSuplemetarias(result.getFloat("horas_suplemt"));
                    rolPagos.setValor(result.getFloat("valor"));
                    rolPagos.setCodigo(result.getString("codigo"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return rolPagos;
    }

    public float obtenerDecicmoTercero() {
        float decimo = 0;
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.ejecutarConsulta("SELECT round(COALESCE(SUM(valor)/12, 0)::numeric, 2) AS decimo FROM rol_de_pagos WHERE id_empleado = " 
                        + rolPagos.getEmpleado().getId() + " AND fecha_generado between CONCAT(EXTRACT(YEAR FROM NOW())-1, '-12-01')::timestamp AND CONCAT(EXTRACT(YEAR FROM NOW()), '-11-30')::timestamp");
                while (result.next()) {
                    decimo = result.getFloat("decimo");
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return decimo;
    }

    public int obtenerHorasLaboradas() {
        int horas = 0;
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.ejecutarConsulta("SELECT COALESCE(horas_laboradas((SELECT empleado_puesto.id_empleado_puesto FROM empleado_puesto "
                        + "WHERE empleado_puesto.id_empleado = " + rolPagos.getEmpleado().getId()
                        + " AND estado = true), " + (rolPagos.getFechaGenerado().getMonth() + 1) + "), 0) AS horas;");
                while (result.next()) {
                    horas = result.getInt("horas");
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return horas;
    }

    public int obtenerHorasSuplementarias() {
        int decimo = 0;
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.ejecutarConsulta("SELECT COALESCE(horas_sumplementarias((SELECT empleado_puesto.id_empleado_puesto FROM empleado_puesto "
                        + "WHERE empleado_puesto.id_empleado = " + rolPagos.getEmpleado().getId()
                        + " AND estado = true), " + (rolPagos.getFechaGenerado().getMonth() + 1) + "), 0) AS horas;");
                while (result.next()) {
                    decimo = result.getInt("horas");
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return decimo;
    }

    public float obtenerDecicmoCuarto() {
        float decimo = 0;
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.ejecutarConsulta("SELECT round(horas_sumplementarias('" +  rolPagos.getFechaGenerado() 
                        + "'::DATE, " + rolPagos.getEmpleado().getId() + ", 400)::numeric, 2) AS decimo;");
                while (result.next()) {
                    decimo = result.getFloat("decimo");
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return decimo;
    }

    @Override
    public List<RolPagos> Listar() {
        return buscar(null, null);
    }

    public List<RolPagos> buscar(Empleado empleado) {
        List<RolPagos> roles = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("rol_de_pagos",
                        "id_rol, generado_por, fecha_generado, fecha_aprobacion, fecha_pago, detalle, estado, horas_laboradas, horas_suplemt, valor, codigo",
                        "id_empleado = " + empleado.getId(), null);
                while (result.next()) {
                    roles.add(new RolPagos(result.getInt("id_rol"), empleado, result.getString("detalle"),
                            result.getDate("fecha_generado"), result.getDate("fecha_aprobacion"),
                            result.getDate("fecha_pago"), result.getString("estado"),
                            result.getFloat("horas_laboradas"), result.getFloat("horas_suplemt"),
                            result.getFloat("valor"), result.getString("codigo")));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return roles;
    }

    private List<RolPagos> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<RolPagos> roles;
            try {
                result = conexion.selecionar("rol_de_pagos",
                        "id_rol, id_empleado, generado_por, fecha_generado, fecha_aprobacion, fecha_pago, detalle, estado, horas_laboradas, horas_suplemt, valor, codigo",
                        restricciones, OrdenarAgrupar);
                roles = new ArrayList<>();
                EmpleadoDAO eDAO = new EmpleadoDAO();
                while (result.next()) {
                    roles.add(new RolPagos(result.getInt("id_rol"), eDAO.buscarPorId(result.getInt("id_empleado")),
                            result.getString("detalle"), result.getDate("fecha_generado"),
                            result.getDate("fecha_aprobacion"), result.getDate("fecha_pago"),
                            result.getString("estado"), result.getFloat("horas_laboradas"),
                            result.getFloat("horas_suplemt"), result.getFloat("valor"),
                            result.getString("codigo")));
                }
                result.close();
                return roles;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}