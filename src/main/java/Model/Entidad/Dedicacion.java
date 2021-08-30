/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

public class Dedicacion {

    private int id;
    private String nombre, detalle;
    private float porcentajeUtilidad;

    public Dedicacion() {
        id = 1;
        nombre = "";
        detalle = "";
        porcentajeUtilidad = 0;
    }

    public Dedicacion(int id, String nombre, float porcentajeUtilidad, String detalle) {
        this.id = id;
        this.nombre = nombre;
        this.detalle = detalle;
        this.porcentajeUtilidad = porcentajeUtilidad;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public float getPorcentajeUtilidad() {
        return porcentajeUtilidad;
    }

    public void setPorcentajeUtilidad(float porcentajeUtilidad) {
        this.porcentajeUtilidad = porcentajeUtilidad;
    }
}
