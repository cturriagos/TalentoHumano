/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.util.Date;


public class Departamento {
    private int id;
    private String nombre;
    private boolean estado;
    private Date fecha_creacion;
    private String descripcion;

    public Departamento() {
        this.id = 0;
        this.nombre = "";
        this.estado = true;
        this.fecha_creacion = new Date();
        this.descripcion = "";
    }

    public Departamento(int id, String nombre, boolean estado, Date fecha_creacion, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
