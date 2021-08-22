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
public class IngresosSalidas {
    private int id;
    private String horaIngreso, horaSalida;
    private String observaciones;

    public IngresosSalidas() {}

    public IngresosSalidas(int id, String horaIngreso, String horaSalida, String observaciones) {
        this.id = id;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String horario() {
        return horaIngreso + "-" + horaSalida;
    }
    
    public String resumen() {
        return observaciones + " (" + horario() + ")";
    }
}