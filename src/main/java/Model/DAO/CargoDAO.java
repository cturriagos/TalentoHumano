/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Interfaces.IDAO;
import Model.Entidad.Cargo;
import org.jetbrains.annotations.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class CargoDAO implements IDAO<Cargo>{
    private final Conexion conexion;
    private Cargo cargo;
    
    public CargoDAO() {
        this.conexion = new Conexion();
        this.cargo = new Cargo();
    }

    public CargoDAO(Cargo cargo) {
        this.conexion = new Conexion();
        this.cargo = cargo;
    }

    public CargoDAO(Conexion conexion) {
        this.conexion = conexion;
        this.cargo = new Cargo();
    }

    public CargoDAO(Cargo cargo, Conexion conexion) {
        this.conexion = conexion;
        this.cargo = cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    @Override
    public Conexion obtenerConexion() {
        return this.conexion;
    }

    @Override
    public int insertar(Cargo entity) {
        this.cargo = entity;
      return insertar();
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            cargo.setId(conexion.insertar("cargo", "nombre", "'" + cargo.getNombre() + "'", "id_cargo"));
            return cargo.getId();
        }
        return -1;
    }

    @Override
    public int actualizar(Cargo entity) {
        this.cargo = entity;
        return actualizar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("cargo", "nombre = '" + cargo.getNombre() + "'", "id_cargo = " + cargo.getId());
        }
        return -1;
    }

    @Override
    public Cargo buscarPorId(Object id) {
        List<Cargo> lista = buscar("id_cargo = " + id, "nombre");
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }
    
    @Override
    public List<Cargo> Listar() {
        return buscar(null, "nombre");
    }

    private List<Cargo> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<Cargo> cargos;
            try {
                result = conexion.selecionar("cargo", "id_cargo, nombre", restricciones, OrdenarAgrupar);
                cargos = new ArrayList<>();
                while (result.next()) {
                    cargos.add(new Cargo(result.getInt("id_cargo"),result.getString("nombre")));
                }
                result.close();
                return cargos;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}