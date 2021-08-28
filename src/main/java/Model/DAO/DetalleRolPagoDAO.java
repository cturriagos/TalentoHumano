/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Config.Conexion;
import Model.Entidad.DetalleRolPago;
import Model.Entidad.RolPagos;
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
 * @author ClasK7
 */
@Named
@ApplicationScoped
public class DetalleRolPagoDAO implements IDAO<DetalleRolPago> {

    private final Conexion conexion;
    private DetalleRolPago detalleRolPago;

    public DetalleRolPagoDAO() {
        this.conexion = new Conexion();
        this.detalleRolPago = new DetalleRolPago();
    }

    public DetalleRolPagoDAO(Conexion conexion) {
        this.conexion = conexion;
        this.detalleRolPago = new DetalleRolPago();
    }

    public DetalleRolPagoDAO(DetalleRolPago detalleRolPago) {
        this.conexion = new Conexion();
        this.detalleRolPago = detalleRolPago;
    }

    public DetalleRolPagoDAO(Conexion conexion, DetalleRolPago detalleRolPago) {
        this.conexion = conexion;
        this.detalleRolPago = detalleRolPago;
    }

    @Override
    public Conexion obtenerConexion() {
        return conexion;
    }

    public DetalleRolPago getDetalleRolPago() {
        return detalleRolPago;
    }

    public void setDetalleRolPago(DetalleRolPago detalleRolPago) {
        this.detalleRolPago = detalleRolPago;
    }

    @Override
    public int insertar() {
        if (conexion.isEstado()) {
            return conexion.insertar("detalle_rol", "id_rol, id_rubro, rubro",
                    detalleRolPago.getRolPagos().getId() + "," + detalleRolPago.getTipoRubro().getId() + "," + detalleRolPago.getRubro(),
                    "id_rol");
        }
        return -1;
    }

    @Override
    public int insertar(DetalleRolPago entity) {
        this.detalleRolPago = entity;
        return insertar();
    }

    @Override
    public int actualizar() {
        if (conexion.isEstado()) {
            return conexion.modificar("detalle_rol",
                    "id_rubro = " + detalleRolPago.getTipoRubro().getId(),
                    "id_rol = " + detalleRolPago.getRolPagos().getId());
        }
        return -1;
    }

    @Override
    public int actualizar(DetalleRolPago entity) {
        this.detalleRolPago = entity;
        return actualizar();
    }

    @Override
    public DetalleRolPago buscarPorId(Object id) {
        List<DetalleRolPago> lista = buscar("id_rol = " + id, null);
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public List<DetalleRolPago> Listar() {
        return buscar(null, null);
    }

    public List<DetalleRolPago> buscar(RolPagos rolPagos) {
        List<DetalleRolPago> detalles = new ArrayList<>();
        this.detalleRolPago.setRolPagos(rolPagos);
        if (conexion.isEstado()) {
            ResultSet result;
            try {
                result = conexion.selecionar("detalle_rol", "id_rubro, rubro",
                                             "id_rol = " + rolPagos.getId(), null);
                TipoRubroDAO tpdao = new TipoRubroDAO();
                while (result.next()) {
                    detalles.add(new DetalleRolPago(rolPagos, tpdao.buscarPorId(result.getInt("id_rubro")), result.getInt("rubro")));
                }
                result.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return detalles;
    }

    private List<DetalleRolPago> buscar(@Nullable String restricciones, @Nullable String OrdenarAgrupar) {
        if (conexion.isEstado()) {
            ResultSet result;
            List<DetalleRolPago> detalles;
            try {
                result = conexion.selecionar("detalle_rol", "id_rol, id_rubro, rubro", restricciones, OrdenarAgrupar);
                detalles = new ArrayList<>();
                RolPagosDAO rpdao = new RolPagosDAO();
                TipoRubroDAO tpdao = new TipoRubroDAO();
                while (result.next()) {
                    detalles.add(new DetalleRolPago(rpdao.buscarPorId(result.getInt("id_rol")), tpdao.buscarPorId(result.getInt("id_rol")), result.getInt("rubro")));
                }
                result.close();
                return detalles;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                conexion.cerrarConexion();
            }
        }
        return null;
    }
}