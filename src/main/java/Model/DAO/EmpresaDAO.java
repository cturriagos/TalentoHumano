/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Empresa;
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
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class EmpresaDAO  implements IDAO<Empresa>{
    protected final Conexion conexion;
    protected Empresa empresa;

    public EmpresaDAO() {
        conexion = new Conexion();
        empresa = new Empresa();
    }

    public EmpresaDAO(Empresa empresa) {
        conexion = new Conexion();
        this.empresa = empresa;
    }

    public EmpresaDAO(Conexion conexion) {
        this.conexion = conexion;
        empresa = new Empresa();
    }

    public EmpresaDAO(Empresa empresa,Conexion conexion) {
        this.conexion = conexion;
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            empresa.setId(conexion.insertar("empresa_matriz",
                    "id_dedicacion, id_ciudad, ruc, tipo, nombre, razon_social, direccion, detalle",
                    empresa.getDedicacion().getId()+ "," + empresa.getCiudad().getId() + ", '" + empresa.getRuc() +
                            "', '" + empresa.getTipo() + "', '" + empresa.getNombre() + "', '" + empresa.getRazonSocial() +
                            "', '" + empresa.getDireccion() + "', '" + empresa.getDetalle()+ "'",
                    "id_matriz"));
            return empresa.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Empresa entity) {
        this.empresa = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("empresa_matriz",
                    "id_dedicacion = " + empresa.getDedicacion().getId()+ ", id_ciudad = " + empresa.getCiudad().getId() +
                            ", ruc = '" + empresa.getRuc()+ "', estado = '" + empresa.getTipo() + "', nombre = '" +
                            empresa.getNombre() + "', razon_social = '" + empresa.getRazonSocial() + "', direccion = '" +
                            empresa.getDireccion() + "', detalle = '" + empresa.getDetalle() + "'" ,
                    "id_matriz = " + empresa.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Empresa entity) {
        this.empresa = entity;
        return actualizar();
    }

    @Override
    public Empresa buscarPorId(Object id) {
        List<Empresa> lista = buscar("id_matriz = " + id, null);
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Empresa> Listar() {
        return buscar(null, "nombre ASC");
    }
    
    private List<Empresa> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Empresa> empresas;
            try {
                result = conexion.selecionar("puesto_laboral", "id_matriz, id_dedicacion, id_ciudad, ruc, tipo, nombre, razon_social, direccion, detalle", restricciones, OrdenarAgrupar);
                empresas = new ArrayList<>();
                DedicacionDAO ddao = new DedicacionDAO();
                CiudadDAO cdao = new CiudadDAO();
                while (result.next()) {
                    empresas.add(new Empresa(result.getInt("id_matriz"),
                            ddao.buscarPorId(result.getInt("id_dedicacion")),
                            cdao.buscarPorId(result.getInt("id_ciudad")),
                            result.getString("ruc"),
                            result.getString("tipo"),
                            result.getString("nombre"),
                            result.getString("razon_social"),
                            result.getString("direccion"),
                            result.getString("detalle")
                    ));
                }
                result.close();
                return empresas;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}