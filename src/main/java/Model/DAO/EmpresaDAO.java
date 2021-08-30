/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Dedicacion;
import Model.Entidad.Empresa;
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
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class EmpresaDAO implements IDAO<Empresa> {

    private final Conexion conexion;
    private final String DEFAUL = "SISTEMA ERPCONTABLE";
    private Empresa empresa;

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

    public EmpresaDAO(Empresa empresa, Conexion conexion) {
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
                    "ruc, tipo, nombre, razon_social, detalle",
                    "'" + empresa.getRuc() + "', '" + empresa.getTipo()
                    + "', '" + empresa.getNombre() + "', '" + empresa.getRazonSocial()
                    + "', '" + empresa.getDetalle() + "'",
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
                    "ruc = '" + empresa.getRuc() + "', tipo = '"
                    + empresa.getTipo() + "', nombre = '" + empresa.getNombre() + "', razon_social = '"
                    + empresa.getRazonSocial() + "', detalle = '" + empresa.getDetalle() + "'",
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
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Empresa> Listar() {
        return buscar(null, "nombre ASC");
    }
    
    public Empresa cargar() {
        List<Empresa> lista = buscar(null, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }else{
            return new Empresa(-1, new Dedicacion(), "9999999999999", "Comercial", DEFAUL, DEFAUL, DEFAUL);
        }
    }

    private List<Empresa> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<Empresa> empresas;
            try {
                result = conexion.selecionar("empresa_matriz", "id_matriz, ruc, tipo, nombre, razon_social, detalle", restricciones, OrdenarAgrupar);
                empresas = new ArrayList<>();
                DedicacionDAO ddao = new DedicacionDAO();
                while (result.next()) {
                    empresas.add(new Empresa(result.getInt("id_matriz"),
                            new Dedicacion(),
                            result.getString("ruc"),
                            result.getString("tipo"),
                            result.getString("nombre"),
                            result.getString("razon_social"),
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