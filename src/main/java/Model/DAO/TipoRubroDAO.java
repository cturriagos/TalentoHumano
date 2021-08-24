/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.TipoRubro;
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
public class TipoRubroDAO implements IDAO<TipoRubro> {

    private final Conexion conexion;
    private TipoRubro tipoRubro;

    public TipoRubroDAO() {
        this.conexion = new Conexion();
        this.tipoRubro = new TipoRubro();
    }

    public TipoRubroDAO(Conexion conexion) {
        this.conexion = conexion;
        this.tipoRubro = new TipoRubro();
    }

    public TipoRubroDAO(TipoRubro tipoRubro) {
        this.conexion = new Conexion();
        this.tipoRubro = tipoRubro;
    }

    public TipoRubroDAO(Conexion conexion, TipoRubro tipoRubro) {
        this.conexion = conexion;
        this.tipoRubro = tipoRubro;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            return conexion.insertar("tipo_rubro", "nombre, coeficiente", 
                    "'" + tipoRubro.getNombre()+ "'," + tipoRubro.getCoeficiente(), "id_tipo_rubro");
        }
        return -1;
    }

    @Override
    public int insertar(TipoRubro entity) {
        this.tipoRubro = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("tipo_rubro", 
                    "nombre = '" + tipoRubro.getNombre()+ "', coeficiente = " + tipoRubro.getCoeficiente(),
                    "id_tipo_rubro = " + tipoRubro.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(TipoRubro entity) {
        this.tipoRubro = entity;
        return actualizar();
    }

    @Override
    public TipoRubro buscarPorId(Object id) {
        List<TipoRubro> lista = buscar("id_tipo_rubro = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<TipoRubro> Listar() {
        return buscar(null, null);
    }

    private List<TipoRubro> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<TipoRubro> roles;
            try {
                result = conexion.selecionar("tipo_rubro", "id_tipo_rubro, nombre, coeficiente",
                        restricciones, OrdenarAgrupar);
                roles = new ArrayList<>();
                EmpleadoDAO eDAO = new EmpleadoDAO();
                while (result.next()) {
                    roles.add(new TipoRubro(result.getInt("id_tipo_rubro"), result.getInt("coeficiente"), result.getString("nombre")));
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