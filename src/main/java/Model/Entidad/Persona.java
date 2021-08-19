/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

/**
 *
 * @author rturr
 */
public class Persona {
    private int id;
    private String identificacion, tipoIdentificacion, correo, telefono1, telefono2, direccion;
    private boolean estado;

    public Persona() {
        this.id = 0;
        this.identificacion = "";
        this.tipoIdentificacion = "";
        this.correo = "";
        this.telefono1 = "";
        this.telefono2 = "";
        this.direccion = "";
        this.estado = true;
    }

    public Persona(int id, String identificacion, String tipoIdentificacion, String correo, String telefono1, String telefono2, String direccion, boolean estado) {
        this.id = id;
        this.identificacion = identificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.correo = correo;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.direccion = direccion;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String telefonos(){
        return telefono1 + (telefono2.isEmpty()? "" : " - " + telefono2);
    }
}