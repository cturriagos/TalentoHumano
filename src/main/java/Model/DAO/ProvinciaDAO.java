/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Provincia;
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
public class ProvinciaDAO implements IDAO<Provincia>{
    private final Conexion conexion;
    private Provincia provincia;
    
    public ProvinciaDAO(){
        conexion = new Conexion();
        provincia = new Provincia();
    }
    
    public ProvinciaDAO(Conexion conexion){
        this.conexion = conexion;
        provincia = new Provincia();
    }
    
    public ProvinciaDAO(Provincia provincia){
        conexion = new Conexion();
        this.provincia = provincia;
    }
    
    public ProvinciaDAO(Conexion conexion, Provincia provincia){
        this.conexion = conexion;
        this.provincia = provincia;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            provincia.setId(conexion.insertar("provincia", "provincia",
                    "'" + provincia.getNombre()+ "'", "cod"));
            return provincia.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Provincia entity) {
        this.provincia = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("provincia",
                    "provincia = '" + provincia.getNombre()+ "'",
                    "cod = " + provincia.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Provincia entity) {
        this.provincia = entity;
        return actualizar();
    }

    @Override
    public Provincia buscarPorId(Object id) {
        List<Provincia> lista = buscar("cod = " + id, "provincia");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Provincia> Listar() {
        return buscar(null, "provincia");
    }
    
    private List<Provincia> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Provincia> lista;
            try {
                result = conexion.selecionar("provincia", "cod, provincia", restricciones, OrdenarAgrupar);
                lista = new ArrayList<>();
                while (result.next()) {
                    lista.add(new Provincia(
                                    result.getInt("cod"), 
                                    result.getString("provincia")
                                ));
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
