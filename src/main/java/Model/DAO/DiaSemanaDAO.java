/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.DiaSemana;
import com.sun.istack.internal.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rturr
 */
public class DiaSemanaDAO {
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

    public Conexion obtenerConexion() {
        return conexion;
    }

    public DiaSemana buscarPorId(Object id) {
        List<DiaSemana> lista = buscar("id_dia = " + id, "nombre_dia");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    public List<DiaSemana> Listar() {
        return buscar(null, "nombre_dia");
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