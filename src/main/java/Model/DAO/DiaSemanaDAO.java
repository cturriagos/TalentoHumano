/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.DiaSemana;
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
public class DiaSemanaDAO implements IDAO<DiaSemana>{
    protected final Conexion conexion;
    protected DiaSemana diaSemana;

    public DiaSemanaDAO() {
        conexion = new Conexion();;
        diaSemana = new DiaSemana();
    }

    public DiaSemanaDAO(DiaSemana diaSemana) {
        conexion = new Conexion();
        this.diaSemana = diaSemana;
    }

    public DiaSemanaDAO(Conexion conexion) {
        this.conexion = conexion;
        diaSemana = new DiaSemana();
    }

    public DiaSemanaDAO(Conexion conexion, DiaSemana diaSemana) {
        this.conexion = conexion;
        this.diaSemana = diaSemana;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }
    
    @Override
    public DiaSemana buscarPorId(Object id) {
        List<DiaSemana> lista = buscar("id_dia = " + id, "nombre_dia");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<DiaSemana> Listar() {
        return buscar(null, "nombre_dia");
    }

    @Override
    public int insertar() {
           if (conexion.isEstado()) {
            diaSemana.setId(conexion.insertar("dia_semana", "nombre_dia", "'" + diaSemana.getNombre() + "'", "id_dia"));
            return diaSemana.getId();
        }
        return -1;
    }

    @Override
    public int insertar(DiaSemana entity) {
        this.diaSemana = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("dia_semana", "nombre_dia = '" + diaSemana.getNombre() + "'", "id_dia = " + diaSemana.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(DiaSemana entity) {
        this.diaSemana = entity;
        return actualizar();
    }
        
    private List<DiaSemana> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<DiaSemana> diasSemana;
            try {
                result = conexion.selecionar("dia_semana", "id_dia, nombre_dia", restricciones, OrdenarAgrupar);
                diasSemana = new ArrayList<>();
                while (result.next()) {
                    diasSemana.add(
                            new DiaSemana(
                                    result.getInt("id_dia"),
                                    result.getString("nombre_dia"))
                            );
                }
                result.close();
                return diasSemana;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}