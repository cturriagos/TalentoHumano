/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import Model.DAO.TipoRubroDAO;
import java.util.Date;

/**
 *
 * @author kestradalp
 */
public class EmpleadoReserva {
    private Empleado empleado;
    private Date fechaSolicitud;
    private float formaPago;
    private String detalle;
    private TipoRubro tipoRubro;

    public EmpleadoReserva() {
        this.empleado = new Empleado();
        this.fechaSolicitud = new Date();
        this.formaPago = 0;
        this.detalle = "";
        inicializarTipo();
    }

    public EmpleadoReserva(Empleado empleado, Date fechaSolicitud, float formaPago, String detalle) {
        this.empleado = empleado;
        this.fechaSolicitud = fechaSolicitud;
        this.formaPago = formaPago;
        this.detalle = detalle;
        inicializarTipo();
    }

    private void inicializarTipo(){
        TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
        tipoRubro = tipoRubroDAO.buscarPorId(8);
    } 

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public float getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(float formaPago) {
        this.formaPago = formaPago;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }
}
