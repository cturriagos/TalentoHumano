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
public class DetalleHorario {
    private IngresosSalidas ingresoSalida;
    private HorarioLaboral horarioLaboral;
    private DiaSemana diaSemana;
    
    public DetalleHorario() {
        ingresoSalida = new IngresosSalidas();
        horarioLaboral = new HorarioLaboral();
        diaSemana = new DiaSemana();
    }
    
    public DetalleHorario(IngresosSalidas ingresoSalida, HorarioLaboral horarioLaboral, DiaSemana diaSemana) {
        this.ingresoSalida = ingresoSalida;
        this.horarioLaboral = horarioLaboral;
        this.diaSemana = diaSemana;
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
}
