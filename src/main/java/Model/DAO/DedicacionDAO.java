/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.Dedicacion;
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
public class DedicacionDAO  implements  IDAO<Dedicacion>{
    private final Conexion conexion;
    private Dedicacion dedicacion;
    
    public DedicacionDAO(){
        conexion = new Conexion();
        dedicacion = new Dedicacion();
    }
    
    public DedicacionDAO(Conexion conexion){
        this.conexion = conexion;
        dedicacion = new Dedicacion();
    }
    
    public DedicacionDAO(Dedicacion dedicacion){
        conexion = new Conexion();
        this.dedicacion = dedicacion;
    }
    
    public DedicacionDAO(Conexion conexion, Dedicacion dedicacion){
        this.conexion = conexion;
        this.dedicacion = dedicacion;
    }

    public Dedicacion getDedicacion() {
        return dedicacion;
    }

    public void setDedicacion(Dedicacion dedicacion) {
        this.dedicacion = dedicacion;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            dedicacion.setId(conexion.insertar("dedicacion", "nombre, porcentaje_utilidad, detalle",
                    "'" + dedicacion.getNombre()+ "','" + dedicacion.getPorcentajeUtilidad()+ "', '" + dedicacion.getDetalle()+ "'",
                    "id_dedicacion"));
            return dedicacion.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Dedicacion entity) {
        this.dedicacion = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("dedicacion",
                    "nombre = '" + dedicacion.getNombre()+ "', porcentaje_utilidad = '" + dedicacion.getPorcentajeUtilidad()+ "', detalle = '" + dedicacion.getDetalle()+ "'",
                    "id_dedicacion = " + dedicacion.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Dedicacion entity) {
        this.dedicacion = entity;
        return actualizar();
    }

    @Override
    public Dedicacion buscarPorId(Object id) {
        List<Dedicacion> lista = buscar("id_dedicacion = " + id, "nombre");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Dedicacion> Listar() {
        return buscar(null, "nombre");
    }
    
    private List<Dedicacion> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Dedicacion> lista;
            try {
                result = conexion.selecionar("dedicacion", "id_dedicacion, nombre, porcentaje_utilidad, detalle", restricciones, OrdenarAgrupar);
                lista = new ArrayList<>();
                while (result.next()) {
                    lista.add(
                            new Dedicacion(
                                    result.getInt("id_dedicacion"), 
                                    result.getString("nombre"),
                                    result.getFloat("porcentaje_utilidad"),
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
