/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Ciudad;
import Model.Interfaces.IDAO;
import com.sun.istack.internal.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arialdo
 */
public class CiudadDAO implements  IDAO<Ciudad>{
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
            ciudad.setId(conexion.insertar("ciudad", "provincia, nombre, detalle",
                    "'" + ciudad.getProvincia()+ "','" + ciudad.getNombre()+ "', '" + ciudad.getDetalle()+ "'", "id_ciudad"));
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
                    "provincia= '" + ciudad.getProvincia()+ "', nombre = '" + ciudad.getNombre()+ "', detalle = '" + ciudad.getDetalle()+ "'",
                    "id_ciudad = " + ciudad.getId());
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
    
    private List<Ciudad> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Ciudad> lista;
            try {
                result = conexion.selecionar("ciudad", "id_ciudad, provincia, nombre, detalle", restricciones, OrdenarAgrupar);
                lista = new ArrayList<>();
                while (result.next()) {
                    lista.add(
                            new Ciudad(
                                    result.getInt("id_ciudad"), 
                                    result.getString("provincia"),
                                    result.getString("nombre"),
                                    result.getString("detalle")
                            )
                    );
                }
                result.close();
                return lista;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    
    }
}
