/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Sucursal;
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
public class SucursalDAO implements IDAO<Sucursal> {

    protected final Conexion conexion;
    protected Sucursal sucursal;

    public SucursalDAO() {
        conexion = new Conexion();
        sucursal = new Sucursal();
    }

    public SucursalDAO(Sucursal sucursal) {
        conexion = new Conexion();
        this.sucursal = sucursal;
    }

    public SucursalDAO(Conexion conexion) {
        this.conexion = conexion;
        sucursal = new Sucursal();
    }

    public SucursalDAO(Sucursal sucursal, Conexion conexion) {
        this.conexion = conexion;
        this.sucursal = sucursal;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            sucursal.setId(conexion.insertar("sucursal", "id_matriz, id_ciudad, direccion, detalle",
                    sucursal.getEmpresa().getId() + "," + sucursal.getCiudad().getId() + ", '" + sucursal.getDireccion()
                    + "', '" + sucursal.getDetalle() + "'", "id_sucursal"));
            return sucursal.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Sucursal entity) {
        this.sucursal = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("sucursal", "id_matriz = " + sucursal.getEmpresa().getId() + ", id_ciudad = "
                    + sucursal.getCiudad().getId() + ", direccion = '" + sucursal.getDireccion() + "', detalle = '"
                    + sucursal.getDetalle() + "'", "id_sucursal = " + sucursal.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Sucursal entity) {
        this.sucursal = entity;
        return actualizar();
    }

    @Override
    public Sucursal buscarPorId(Object id) {
        List<Sucursal> lista = buscar("id_sucursal = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Sucursal> Listar() {
        return buscar(null, "id_sucursal ASC");
    }

    private List<Sucursal> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<Sucursal> sucursales;
            try {
                result = conexion.selecionar("sucursal", "id_sucursal, id_matriz, id_ciudad, direccion, detalle", restricciones, OrdenarAgrupar);
                sucursales = new ArrayList<>();
                EmpresaDAO edao = new EmpresaDAO();
                CiudadDAO cdao = new CiudadDAO();
                while (result.next()) {
                    sucursales.add(new Sucursal(result.getInt("id_sucursal"),
                            edao.buscarPorId(result.getInt("id_matriz")),
                            cdao.buscarPorId(result.getInt("id_ciudad")),
                            result.getString("direccion"),
                            result.getString("detalle")
                    ));
                }
                result.close();
                return sucursales;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}
