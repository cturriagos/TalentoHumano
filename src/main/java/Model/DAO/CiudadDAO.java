/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Ciudad;
import Model.Entidad.Provincia;
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
public class CiudadDAO implements IDAO<Ciudad>{
    private final Conexion conexion;
    private Ciudad ciudad;
    
    public CiudadDAO(){
        conexion = new Conexion();
        ciudad = new Ciudad();
    }
    
    public CiudadDAO(Conexion conexion){
        this.conexion = conexion;
        ciudad = new Ciudad();
    }
    
    public CiudadDAO(Ciudad ciudad){
        conexion = new Conexion();
        this.ciudad = ciudad;
    }
    
    public CiudadDAO(Conexion conexion, Ciudad ciudad){
        this.conexion = conexion;
        this.ciudad = ciudad;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            ciudad.setId(conexion.insertar("ciudad", "id_provincia, nombre, detalle",
                    ciudad.getProvincia().getId() + ",'" + ciudad.getNombre()+ "', '" + ciudad.getDetalle()+ "'", "id_ciudad"));
            return ciudad.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Ciudad entity) {
        this.ciudad = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("ciudad",
                    "id_provincia= " + ciudad.getProvincia().getId() + ", nombre = '" + ciudad.getNombre()+ "', detalle = '"
                    + ciudad.getDetalle()+ "'", "id_ciudad = " + ciudad.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Ciudad entity) {
        this.ciudad = entity;
        return actualizar();
    }

    @Override
    public Ciudad buscarPorId(Object id) {
        List<Ciudad> lista = buscar("id_ciudad = " + id, "nombre");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Ciudad> Listar() {
        return buscar(null, "nombre");
    }
    
    public List<Ciudad> Listar(Provincia provincia) {
        List<Ciudad> lista = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("ciudad", "id_ciudad, nombre, detalle", 
                        "id_provincia = " + provincia.getId(), "nombre DESC");
                while (result.next()) {
                    lista.add(new Ciudad(
                                    result.getInt("id_ciudad"), provincia,
                                    result.getString("nombre"),
                                    result.getString("detalle")
                                ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return lista;
    }
    
    private List<Ciudad> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        List<Ciudad> lista = new ArrayList<>();
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("ciudad", "id_ciudad, id_provincia, nombre, detalle", restricciones, OrdenarAgrupar);
                ProvinciaDAO pdao = new ProvinciaDAO();
                while (result.next()) {
                    lista.add(new Ciudad(
                                    result.getInt("id_ciudad"), 
                                    pdao.buscarPorId(result.getInt("id_provincia")),
                                    result.getString("nombre"),
                                    result.getString("detalle")
                                ));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return lista;
    }
}