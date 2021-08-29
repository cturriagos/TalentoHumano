/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Interfaces.IDAO;
import Model.Entidad.Departamento;
import org.jetbrains.annotations.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class DepartamentoDAO implements IDAO<Departamento> {

    protected final Conexion conexion;
    protected Departamento departamento;

    public DepartamentoDAO() {
        conexion = new Conexion();
        departamento = new Departamento();
    }

    public DepartamentoDAO(Departamento departamento) {
        conexion = new Conexion();
        this.departamento = departamento;
    }

    public DepartamentoDAO(Conexion conexion) {
        this.conexion = conexion;
        departamento = new Departamento();
    }

    public DepartamentoDAO(Departamento departamento, Conexion conexion) {
        this.conexion = conexion;
        this.departamento = departamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            departamento.setEstado(true);
            departamento.setId(conexion.insertar("departamento",
                    "nombre, estado, fecha_creacion, descripcion",
                    "'" + departamento.getNombre() + "', " + departamento.isEstado() + ", CURRENT_TIMESTAMP , '" + departamento.getDescripcion() + "'",
                    "id_departamento"));
            return departamento.getId();
        }
        return -1;
    }

    @Override
    public int insertar(Departamento entity) {
        this.departamento = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("departamento",
                    "nombre = '" + departamento.getNombre() + "', estado = " + departamento.isEstado() + ", descripcion = '" + departamento.getDescripcion() + "'",
                    "id_departamento = " + departamento.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(Departamento entity) {
        this.departamento = entity;
        return actualizar();
    }

    @Override
    public Departamento buscarPorId(Object id) {
        List<Departamento> lista = buscar("id_departamento = " + id, "nombre");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }

    @Override
    public List<Departamento> Listar() {
        return buscar(null, "nombre");
    }

    private List<Departamento> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Departamento> departamentos;
            try {
                result = conexion.selecionar("departamento", "id_departamento, nombre, estado, fecha_creacion, descripcion", restricciones, OrdenarAgrupar);
                departamentos = new ArrayList<>();
                while (result.next()) {
                    departamentos.add(new Departamento(result.getInt("id_departamento"),
                            result.getString("nombre"),
                            result.getBoolean("estado"),
                            result.getDate("fecha_creacion"),
                            result.getString("descripcion")
                    ));
                }
                result.close();
                return departamentos;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }

    public void cambiarEstado() {
        if (conexion.isEstado()) {
            conexion.modificar("departamento", "estado = NOT estado","id_departamento = " + departamento.getId());
        }
    }
}