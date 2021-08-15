/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

public class Ciudad {

    private int id;
    private String provincia, nombre, detalle;

    public Ciudad() {
        this.id = 0;
        this.provincia = "";
        this.nombre = "";
        this.detalle = "";
    }

    public Ciudad(int id, String provincia, String nombre, String detalle) {
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
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
        return nombre + "(" + provincia + ")";
    }

}
