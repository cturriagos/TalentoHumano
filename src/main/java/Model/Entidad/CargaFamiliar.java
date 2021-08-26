/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import Model.DAO.TipoRubroDAO;
import java.util.Date;

/**
 *
 * @author ClasK7
 */
public class CargaFamiliar {
    private TipoRubro tipoRubro;
    private int id, faliares;
    private Empleado empleado;
    private String conyuge, detalle, pathValidation;
    private Date fechaCambio;
    
    public CargaFamiliar() {
        this.id = 0;
        this.faliares = 0;
        this.empleado = new Empleado();
        this.conyuge = "";
        this.detalle = "";
        this.pathValidation = "";
        this.fechaCambio = new Date();
        inicializarTipo();
    }

    public CargaFamiliar(int id, int faliares, Empleado empleado, String conyuge, String detalle, String pathValidation, Date fechaCambio) {
        this.id = id;
        this.faliares = faliares;
        this.empleado = empleado;
        this.conyuge = conyuge;
        this.detalle = detalle;
        this.pathValidation = pathValidation;
        this.fechaCambio = fechaCambio;
        inicializarTipo();
    }

    private void inicializarTipo(){
        TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
        tipoRubro = tipoRubroDAO.buscarPorId(5);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFaliares() {
        return faliares;
    }

    public void setFaliares(int faliares) {
        this.faliares = faliares;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getConyuge() {
        return conyuge;
    }

    public void setConyuge(String conyuge) {
        this.conyuge = conyuge;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getPathValidation() {
        return pathValidation;
    }

    public void setPathValidation(String pathValidation) {
        this.pathValidation = pathValidation;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }    

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }
}
