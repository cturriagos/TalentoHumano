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
public class RolPagos {

    private int id;
    private Empleado empleado;
    private final String usuario = "ADMINISTRADOR";
    private String estado, detalle, codigo;
    private Date fechaGenerado, fechaAprobacion, fechaPago;
    private float horasLaboradas, horasSuplemetarias, valor;

    public RolPagos() {
        this.id = 0;
        this.empleado = new Empleado();
        this.detalle = "";
        this.fechaGenerado = new Date();
        this.fechaAprobacion = null;
        this.fechaPago = null;
        this.estado = "";
        this.horasLaboradas = 0;
        this.horasSuplemetarias = 0;
        this.valor = 0;
        this.codigo = "";
    }

    public RolPagos(Empleado empleado) {
        this.id = 0;
        this.empleado = empleado;
        this.detalle = "";
        this.fechaGenerado = new Date();
        this.fechaAprobacion = new Date();
        this.fechaPago = new Date();
        this.estado = "";
        this.horasLaboradas = 0;
        this.horasSuplemetarias = 0;
        this.valor = 0;
        this.codigo = "";
    }

    public RolPagos(Empleado empleado, float valor) {
        this.id = 0;
        this.empleado = empleado;
        this.detalle = "";
        this.fechaGenerado = new Date();
        this.fechaAprobacion = new Date();
        this.fechaPago = new Date();
        this.estado = "";
        this.horasLaboradas = 0;
        this.horasSuplemetarias = 0;
        this.valor = valor;
        this.codigo = "";
    }

    public RolPagos(int id, Empleado empleado, String detalle, Date fechaGenerado, Date fechaAprobacion, Date fechaPago, String estado, float horasLaboradas, float horasSuplemetarias, float valor, String codigo) {
        this.id = id;
        this.empleado = empleado;
        this.detalle = detalle;
        this.fechaGenerado = fechaGenerado;
        this.fechaAprobacion = fechaAprobacion;
        this.fechaPago = fechaPago;
        this.estado = estado;
        this.horasLaboradas = horasLaboradas;
        this.horasSuplemetarias = horasSuplemetarias;
        this.valor = valor;
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFechaGenerado() {
        return fechaGenerado;
    }

    public void setFechaGenerado(Date fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public float getHorasLaboradas() {
        return horasLaboradas;
    }

    public void setHorasLaboradas(float horasLaboradas) {
        this.horasLaboradas = horasLaboradas;
    }

    public float getHorasSuplemetarias() {
        return horasSuplemetarias;
    }

    public void setHorasSuplemetarias(float horasSuplemetarias) {
        this.horasSuplemetarias = horasSuplemetarias;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
