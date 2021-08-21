/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.util.Date;

/**
 *
 * @author ClasK7
 */
public class EmpleadoSucursal {
    private int id;
    private Empleado empleado;
    private Sucursal sucursal;
    private Date fechaCambio;
    private boolean estado;
    private String detalle;

    public EmpleadoSucursal() {
        this.id = 0;
        this.empleado = new Empleado();
        this.sucursal = new Sucursal();
        this.fechaCambio = new Date();
        this.estado = true;
        this.detalle = "";
    }

    public EmpleadoSucursal(int id, Empleado empleado, Sucursal sucursal, Date fechaCambio, boolean estado, String detalle) {
        this.id = id;
        this.empleado = empleado;
        this.sucursal = sucursal;
        this.fechaCambio = fechaCambio;
        this.estado = estado;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}