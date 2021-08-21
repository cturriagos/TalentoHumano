/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.CargaFamiliar;
import Model.Entidad.Empleado;
import Model.Interfaces.IDAO;
import org.jetbrains.annotations.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class CargaFamiliarDAO implements IDAO<CargaFamiliar> {

    private final Conexion conexion;
    private CargaFamiliar cargaFamiliar;

    public CargaFamiliarDAO() {
        conexion = new Conexion();
        cargaFamiliar = new CargaFamiliar();
    }

    public CargaFamiliarDAO(CargaFamiliar cargaFamiliar) {
        conexion = new Conexion();
        this.cargaFamiliar = cargaFamiliar;
    }

    public CargaFamiliarDAO(Conexion conexion) {
        this.conexion = conexion;
        cargaFamiliar = new CargaFamiliar();
    }

    public CargaFamiliarDAO(CargaFamiliar cargaFamiliar, Conexion conexion) {
        this.conexion = conexion;
        this.cargaFamiliar = cargaFamiliar;
    }

    public CargaFamiliar getCargaFamiliar() {
        return cargaFamiliar;
    }

    public void setCargaFamiliar(CargaFamiliar cargaFamiliar) {
        this.cargaFamiliar = cargaFamiliar;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            cargaFamiliar.setId(conexion.insertar("carga_familiar",
                    "id_empleado, cantidad_carga, fecha_cambio, nombre, documento_validacion, detalle",
                    cargaFamiliar.getEmpleado().getId() + ", " + cargaFamiliar.getFaliares() + ", CURRENT_TIMESTAMP, '" + cargaFamiliar.getConyuge() + "', '"
                    + cargaFamiliar.getPathValidation() + "', '" + cargaFamiliar.getDetalle() + "'",
                    "id_cargaf"));
            return cargaFamiliar.getId();
        }
        return -1;
    }

    @Override
    public int insertar(CargaFamiliar entity) {
        this.cargaFamiliar = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("carga_familiar",
                    "id_empleado = " + cargaFamiliar.getEmpleado().getId() + ", cantidad_carga = " + cargaFamiliar.getFaliares()
                    + ", fecha_cambio = '" + cargaFamiliar.getFechaCambio() + "', nombre = '" + cargaFamiliar.getConyuge()
                    + "', documento_validacion = '" + cargaFamiliar.getPathValidation() + "', detalle = '" + cargaFamiliar.getDetalle() + "'",
                    "id_cargaf = " + cargaFamiliar.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(CargaFamiliar entity) {
        this.cargaFamiliar = entity;
        return actualizar();
    }

    @Override
    public CargaFamiliar buscarPorId(Object id) {
        List<CargaFamiliar> lista = buscar("id_cargaf = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return new CargaFamiliar(0,0, new Empleado(), "N/D", "N/D", "N/D", new Date() );
    }

    public CargaFamiliar buscar(Empleado empleado) {
        cargaFamiliar = new CargaFamiliar(-1,0, empleado, "N/D", "N/D", "N/D", new Date() );
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("carga_familiar",
                        "id_cargaf, cantidad_carga, fecha_cambio, nombre, documento_validacion, detalle",
                        "id_empleado = " + empleado.getId(), null);
                while (result.next()) {
                    cargaFamiliar.setId(result.getInt("id_cargaf"));
                    cargaFamiliar.setFaliares(result.getInt("cantidad_carga"));
                    cargaFamiliar.setConyuge(result.getString("nombre"));
                    cargaFamiliar.setDetalle(result.getString("detalle"));
                    cargaFamiliar.setPathValidation(result.getString("documento_validacion"));
                    cargaFamiliar.setFechaCambio(result.getDate("fecha_cambio"));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return cargaFamiliar;
    }

    @Override
    public List<CargaFamiliar> Listar() {
        return buscar(null, "fecha_cambio DESC");
    }

    private List<CargaFamiliar> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        List<CargaFamiliar> cargaFamiliares = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("carga_familiar", "id_cargaf, id_empleado, cantidad_carga, fecha_cambio, nombre, documento_validacion, detalle", restricciones, OrdenarAgrupar);
                EmpleadoDAO edao = new EmpleadoDAO();
                while (result.next()) {
                    cargaFamiliares.add(new CargaFamiliar(
                            result.getInt("id_cargaf"), result.getInt("cantidad_carga"),
                            edao.buscarPorId(result.getInt("id_empleado")),
                            result.getString("nombre"), result.getString("detalle"),
                            result.getString("documento_validacion"),
                            result.getDate("fecha_cambio")
                    ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return cargaFamiliares;
    }
}
