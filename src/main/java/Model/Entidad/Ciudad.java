/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

public class Ciudad {

    private int id;
    private Provincia provincia;
    private String nombre, detalle;

    public Ciudad() {
        this.id = 0;
        this.provincia = new Provincia();
        this.nombre = "";
        this.detalle = "";
    }

    public Ciudad(int id, Provincia provincia, String nombre, String detalle) {
        this.id = id;
        this.provincia = provincia;
        this.nombre = nombre;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
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
    
    public String resumen(){
        return nombre + "(" + provincia.getNombre() + ")";
    }
}