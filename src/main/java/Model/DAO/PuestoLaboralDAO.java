/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.PuestoLaboral;
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
 * @author kestradalp
 */
@Named
@ApplicationScoped
public class PuestoLaboralDAO implements IDAO<PuestoLaboral>{
    protected final Conexion conexion;
    protected PuestoLaboral puestoLaboral;

    public PuestoLaboralDAO() {
        conexion = new Conexion();
        puestoLaboral = new PuestoLaboral();
    }

    public PuestoLaboralDAO(PuestoLaboral puestoLaboral) {
        conexion = new Conexion();
        this.puestoLaboral = puestoLaboral;
    }

    public PuestoLaboralDAO(Conexion conexion) {
        this.conexion = conexion;
        puestoLaboral = new PuestoLaboral();
    }

    public PuestoLaboralDAO(PuestoLaboral puestoLaboral,Conexion conexion) {
        this.conexion = conexion;
        this.puestoLaboral = puestoLaboral;
    }

    public PuestoLaboral getPuestoLaboral() {
        return puestoLaboral;
    }

    public void setPuestoLaboral(PuestoLaboral puestoLaboral) {
        this.puestoLaboral = puestoLaboral;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            puestoLaboral.setEstado(true);
            puestoLaboral.setId(conexion.insertar("puesto_laboral",
                    "id_cargo, id_departamento, fecha_creacion, estado, descripcion",
                    puestoLaboral.getCargo().getId()+ "," + puestoLaboral.getDepartamento().getId() + ", CURRENT_DATE, " + puestoLaboral.isEstado() + ", '" + puestoLaboral.getDescripcion() + "'",
                    "id_puesto_laboral"));
            return puestoLaboral.getId();
        }
        return -1;
    }

    @Override
    public int insertar(PuestoLaboral entity) {
        this.puestoLaboral = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("puesto_laboral",
                    "id_cargo= " + puestoLaboral.getCargo().getId()+ ", id_departamento = " + puestoLaboral.getDepartamento().getId()+ ", descripcion = '" + puestoLaboral.getDescripcion() + "', estado = " + puestoLaboral.isEstado(),
                    "id_puesto_laboral = " + puestoLaboral.getId());
        }
        return -1;
    }

    @Override
    public int actualizar(PuestoLaboral entity) {
        this.puestoLaboral = entity;
        return actualizar();
    }

    @Override
    public PuestoLaboral buscarPorId(Object id) {
        List<PuestoLaboral> lista = buscar("id_puesto_laboral = " + id, null);
        if(lista != null && !lista.isEmpty()){
                return lista.get(0);
        }
        return null;
    }
    
    public void cambiarEstado(){
        if (conexion.isEstado()) {
            conexion.modificar("puesto_laboral", "estado = NOT estado", "id_puesto_laboral = " + puestoLaboral.getId());
        }
    }

    @Override
    public List<PuestoLaboral> Listar() {
        return buscar(null, null);
    }
    
    public List<PuestoLaboral> Activos() {
        return buscar("estado = true", null);
    }
    
    private List<PuestoLaboral> buscar( @Nullable String restricciones, @Nullable String OrdenarAgrupar){
        if (conexion.isEstado()) {
            ResultSet result;
            List<PuestoLaboral> puestoLaborales;
            try {
                result = conexion.selecionar("puesto_laboral", "id_puesto_laboral, id_cargo, id_departamento, fecha_creacion, estado, descripcion", restricciones, OrdenarAgrupar);
                puestoLaborales = new ArrayList<>();
                DepartamentoDAO ddao = new DepartamentoDAO();
                CargoDAO cdao = new CargoDAO();
                while (result.next()) {
                    puestoLaborales.add(new PuestoLaboral(result.getInt("id_puesto_laboral"),
                            cdao.buscarPorId(result.getInt("id_cargo")),
                            ddao.buscarPorId(result.getInt("id_departamento")),
                            result.getDate("fecha_creacion"),
                            result.getBoolean("estado"),
                            result.getString("descripcion")));
                }
                result.close();
                return puestoLaborales;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}