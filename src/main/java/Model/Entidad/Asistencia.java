/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.util.Date;

/**
 *
 * @author kestradalp
 */
public class Asistencia {
    private EmpleadoPuesto empleadoPuesto;
    private String ingreso, salida;
    private Date fecha;
    private DetalleHorario detalleHorario;

    public Asistencia() {
        this.empleadoPuesto = new EmpleadoPuesto();
        this.ingreso = null;
        this.salida = null;
        this.fecha = new Date();
        this.detalleHorario = new DetalleHorario();
    }

    public Asistencia(EmpleadoPuesto empleadoPuesto, String ingreso, String salida, Date fecha, DetalleHorario detalleHorario) {
        this.empleadoPuesto = empleadoPuesto;
        this.ingreso = ingreso;
        this.salida = salida;
        this.fecha = fecha;
        this.detalleHorario = detalleHorario;
    }

    public EmpleadoPuesto getEmpleadoPuesto() {
        return empleadoPuesto;
    }

    public void setEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) {
        this.empleadoPuesto = empleadoPuesto;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public DetalleHorario getDetalleHorario() {
        return detalleHorario;
    }

    public void setDetalleHorario(DetalleHorario detalleHorario) {
        this.detalleHorario = detalleHorario;
    }
}