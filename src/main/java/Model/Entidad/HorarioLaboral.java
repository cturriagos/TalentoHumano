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
public class HorarioLaboral {
    private int id;
    private String nombre;
    private boolean estado;
    private String observaciones;
    private Date fechaVigencia;

    public HorarioLaboral(){
        this.id = 0;
        this.nombre = "";
        this.estado = true;
        this.observaciones = "";
        this.fechaVigencia = new Date();
    }
    
    public HorarioLaboral(int id, String nombre, boolean estado, String observaciones, Date fechaVigencia) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaVigencia = fechaVigencia;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }
}