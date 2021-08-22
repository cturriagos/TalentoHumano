/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.io.Serializable;

/**
 *
 * @author rturr
 */
public class DetalleHorario implements Serializable{

    private int id;
    private IngresosSalidas ingresoSalida;
    private HorarioLaboral horarioLaboral;
    private DiaSemana diaSemana;
    private boolean estado;

    public DetalleHorario() {
        id = 0;
        ingresoSalida = new IngresosSalidas();
        horarioLaboral = new HorarioLaboral();
        diaSemana = new DiaSemana();
        estado = true;
    }

    public DetalleHorario(int id, IngresosSalidas ingresoSalida, HorarioLaboral horarioLaboral, DiaSemana diaSemana, boolean estado) {
        this.id = id;
        this.ingresoSalida = ingresoSalida;
        this.horarioLaboral = horarioLaboral;
        this.diaSemana = diaSemana;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IngresosSalidas getIngresoSalida() {
        return ingresoSalida;
    }

    public void setIngresoSalida(IngresosSalidas ingresoSalida) {
        this.ingresoSalida = ingresoSalida;
    }

    public HorarioLaboral getHorarioLaboral() {
        return horarioLaboral;
    }

    public void setHorarioLaboral(HorarioLaboral horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
