/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

/**
 *
 * @author kestradalp
 */
public class DetalleRolPago {
    private RolPagos rolPagos;
    private TipoRubro tipoRubro;
    private int rubro;

    public DetalleRolPago() {
        this.rolPagos = new RolPagos();
        this.tipoRubro = new TipoRubro();
        this.rubro = 0;
    }

    public DetalleRolPago(RolPagos rolPagos, TipoRubro tipoRubro, int rubro) {
        this.rolPagos = rolPagos;
        this.tipoRubro = tipoRubro;
        this.rubro = rubro;
    }

    public int getRubro() {
        return rubro;
    }

    public void setRubro(int rubro) {
        this.rubro = rubro;
    }

    public RolPagos getRolPagos() {
        return rolPagos;
    }

    public void setRolPagos(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
    }

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }

    public void setTipoRubro(TipoRubro tipoRubro) {
        this.tipoRubro = tipoRubro;
    }
}
