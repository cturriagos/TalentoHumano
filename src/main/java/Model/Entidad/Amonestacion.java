/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import Model.DAO.TipoRubroDAO;

/**
 *
 * @author ClasK7
 */
public class Amonestacion {
    private int id;
    private Empleado empleado;
    private String tipo, detalle;
    private float valor;
    private boolean estado;
    private TipoRubro tipoRubro;

    public Amonestacion() {
        this.id = 0;
        this.empleado = new Empleado();
        this.tipo = "";
        this.detalle = "";
        this.valor = 0;
        this.estado = true;
        inicializarTipo();
    }

    public Amonestacion(Empleado empleado) {
        this.id = 0;
        this.empleado = empleado;
        this.tipo = "";
        this.detalle = "";
        this.valor = 0;
        this.estado = true;
        inicializarTipo();
    }

    public Amonestacion(int id, Empleado empleado, String tipo, String detalle, float valor, boolean estado) {
        this.id = id;
        this.empleado = empleado;
        this.tipo = tipo;
        this.detalle = detalle;
        this.valor = valor;
        this.estado = estado;
        inicializarTipo();
    }

    private void inicializarTipo(){
        TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
        tipoRubro = tipoRubroDAO.buscarPorId(2);
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public TipoRubro getTipoRubro() {
        return tipoRubro;
    }
}