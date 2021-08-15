/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

public class Empresa {
    private int id;
    private Dedicacion dedicacion;
    private Ciudad ciudad;
    private String ruc, tipo, nombre, razonSocial, direccion, detalle;

    public Empresa() {
        this.id = 0;
        this.dedicacion = new Dedicacion();
        this.ciudad = new Ciudad();
        this.ruc = "";
        this.tipo = "";
        this.nombre = "";
        this.razonSocial = "";
        this.direccion = "";
        this.detalle = "";
    }

    public Empresa(int id, Dedicacion dedicacion, Ciudad ciudad, String ruc, String tipo, String nombre, String razonSocial, String direccion, String detalle) {
        this.id = id;
        this.dedicacion = dedicacion;
        this.ciudad = ciudad;
        this.ruc = ruc;
        this.tipo = tipo;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dedicacion getDedicacion() {
        return dedicacion;
    }

    public void setDedicacion(Dedicacion dedicacion) {
        this.dedicacion = dedicacion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    
}
