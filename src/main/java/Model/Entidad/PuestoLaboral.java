/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.util.Date;

/**
 *
 * @author rturr
 */
public class PuestoLaboral {
    private int id;
    private Cargo cargo;
    private Departamento departamento;
    private Date fecha_creacion;
    private boolean estado;
    private String descripcion;

    public PuestoLaboral() {
    }

    public PuestoLaboral(int id, Cargo cargo, Departamento departamento, Date fecha_creacion, boolean estado, String descripcion) {
        this.id = id;
        this.cargo = cargo;
        this.departamento = departamento;
        this.fecha_creacion = fecha_creacion;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String resumen() {
        return cargo.getNombre() + " (" + departamento.getNombre() + ")";
    }
}
