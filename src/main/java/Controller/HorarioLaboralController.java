/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.HorarioLaboralDAO;
import Model.Entidad.HorarioLaboral;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author rturr
 */
@Named(value = "horarioLaboralView")
@ViewScoped
public class HorarioLaboralController implements Serializable {
    private HorarioLaboral horarioLaboral;
    private HorarioLaboralDAO horarioLaboralDAO;
    private List<HorarioLaboral> lista;
        
    public HorarioLaboralController() {
        horarioLaboral = new HorarioLaboral();
        horarioLaboralDAO = new HorarioLaboralDAO(horarioLaboral);
        lista = new ArrayList<>();
    }
    
   @PostConstruct
    public void constructorHorarioLaboral (){
        lista = horarioLaboralDAO.Listar();
   }
    
    public String enviar() {
        
        horarioLaboralDAO = new HorarioLaboralDAO(horarioLaboral);
        
        if (horarioLaboralDAO.insertar() > 0) {
            mostrarMensajeInformacion("El registro del horario laboral se ha guardado con éxito");
            horarioLaboral = new HorarioLaboral();
            lista = horarioLaboralDAO.Listar();
            return "IngresosSalidas";
        } else {
            mostrarMensajeError("El registro de del horario laboral no se pudo guardar");
            return "";
        }
       
    }
    
    public void onEditar(RowEditEvent<HorarioLaboral> event) {
        HorarioLaboral horarioLaboralEditado = event.getObject();
        horarioLaboralDAO.setHorarioLaboral(horarioLaboralEditado);
        
        if (horarioLaboralDAO.actualizar()> 0) {
            mostrarMensajeInformacion("El registro de horas de salida e ingreso  se ha editado con éxito");
            lista = horarioLaboralDAO.Listar();
        } else {
            mostrarMensajeError("El registro de horas de salida e ingreso  no se pudo editar");
        }
    }
    
    public void onCancel(RowEditEvent<HorarioLaboral> event) {
        mostrarMensajeInformacion("Se canceló");
    }

    public HorarioLaboral getHorarioLaboral() {
        return horarioLaboral;
    }

    public void setHorarioLaboral(HorarioLaboral horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
    }

    public List<HorarioLaboral> getLista() {
        return lista;
    }

    public void setLista(List<HorarioLaboral> horarios) {
        this.lista = horarios;
    }

    public void mostrarMensajeInformacion(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    //  MENSAJE DE ERROR
    
    public void mostrarMensajeError(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
