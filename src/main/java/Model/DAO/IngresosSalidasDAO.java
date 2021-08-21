/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.IngresosSalidas;
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
 * @author rturr
 */
@Named
@ApplicationScoped
public class IngresosSalidasDAO implements IDAO<IngresosSalidas>{
    private final Conexion conexion;
    private IngresosSalidas ingresosSalidas;

    public IngresosSalidasDAO() {
        conexion = new Conexion();
        ingresosSalidas = new IngresosSalidas();
    }

    public IngresosSalidasDAO(Conexion conexion) {
        this.conexion = conexion;
        ingresosSalidas = new IngresosSalidas();
    }

    public IngresosSalidasDAO(IngresosSalidas ingresosSalidas) {
        conexion = new Conexion();
        this.ingresosSalidas = ingresosSalidas;
    }

    public IngresosSalidasDAO(Conexion conexion, IngresosSalidas ingresosSalidas) {
        this.conexion = conexion;
        this.ingresosSalidas = ingresosSalidas;
    }

    public IngresosSalidas getIngresosSalidas() {
        return ingresosSalidas;
    }

    public void setIngresosSalidas(IngresosSalidas ingresosSalidas) {
        this.ingresosSalidas = ingresosSalidas;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            ingresosSalidas.setId(conexion.insertar("ingresos_salidas",
                    "hora_ingreso, hora_salida, observaciones",
                    "'" + ingresosSalidas.getHoraIngreso()+ "','" + ingresosSalidas.getHoraSalida() + "', '" + ingresosSalidas.getObservaciones()+ "'",
                    "id_ingreso_salida"));
            return ingresosSalidas.getId();
        }
        return -1;
    }

    @Override
    public int insertar(IngresosSalidas entity) {
        this.ingresosSalidas = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("ingresos_salidas",
                    "hora_ingreso= '" + ingresosSalidas.getHoraIngreso()+ "', hora_salida = '" + ingresosSalidas.getHoraSalida()+ "', observaciones = '" + ingresosSalidas.getObservaciones() + "'",
                    "id_ingreso_salida = " + ingresosSalidas.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(IngresosSalidas entity) {
        this.ingresosSalidas = entity;
        return actualizar();
    }

    @Override
    public IngresosSalidas buscarPorId(Object id) {
        List<IngresosSalidas> lista = buscar("id_ingreso_salida = " + id, "hora_ingreso, hora_salida");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<IngresosSalidas> Listar() {
       return buscar(null, "hora_ingreso, hora_salida");
    }
    
    private List<IngresosSalidas> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<IngresosSalidas> lista;
            try {
                result = conexion.selecionar("ingresos_salidas", "id_ingreso_salida, hora_ingreso, hora_salida, observaciones", restricciones, OrdenarAgrupar);
                lista = new ArrayList<>();
                while (result.next()) {
                    lista.add(
                            new IngresosSalidas(
                                    result.getInt("id_ingreso_salida"), 
                                    result.getString("hora_ingreso"),
                                    result.getString("hora_salida"),
                                    result.getString("observaciones")
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