/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

/**
 *
 * @author kestradalp
 */
public class Sucursal {
    private int id;
    private Empresa empresa;
    private Ciudad ciudad;
    private String direccion, detalle;

    public Sucursal() {
        this.id = 0;
        this.empresa = new Empresa();
        this.ciudad = new Ciudad();
        this.direccion = "";
        this.detalle = "";
    }

    public Sucursal(int id, Empresa empresa, Ciudad ciudad, String direccion, String detalle) {
        this.id = id;
        this.empresa = empresa;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
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
