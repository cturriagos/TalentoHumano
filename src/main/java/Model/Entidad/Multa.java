/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import Model.DAO.TipoRubroDAO;

/**
 *
 * @author kestradalp
 */
public class Multa {
    private int id;
    private Empleado empleado;
    private float porcentaje, valor;
    private String detalle;
    private boolean estado;
    private TipoRubro tipoRubro;

    public Multa() {
        this.id = 0;
        this.empleado = new Empleado();
        this.porcentaje = 0;
        this.valor = 0;
        this.detalle = "";
        this.estado = true;
        inicializarTipo();
    }

    public Multa(Empleado empleado) {
        this.id = 0;
        this.empleado = empleado;
        this.porcentaje = 0;
        this.valor = 0;
        this.detalle = "";
        this.estado = true;
        inicializarTipo();
    }

    public Multa(int id, Empleado empleado, float porcentaje, float valor, String detalle, boolean estado) {
        this.id = id;
        this.empleado = empleado;
        this.porcentaje = porcentaje;
        this.valor = valor;
        this.detalle = detalle;
        this.estado = estado;
        inicializarTipo();
    }

    private void inicializarTipo(){
        TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
        tipoRubro = tipoRubroDAO.buscarPorId(2);
    } 

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}