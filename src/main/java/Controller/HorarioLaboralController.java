/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.DetalleHorarioDAO;
import Model.DAO.DiaSemanaDAO;
import Model.DAO.HorarioLaboralDAO;
import Model.DAO.IngresosSalidasDAO;
import Model.Entidad.DetalleHorario;
import Model.Entidad.DiaSemana;
import Model.Entidad.HorarioLaboral;
import Model.Entidad.IngresosSalidas;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author rturr
 */
@Named(value = "horarioLaboralView")
@SessionScoped
public class HorarioLaboralController implements Serializable {

    private HorarioLaboral horarioLaboral;
    private DetalleHorario detalleHorario;
    private List<HorarioLaboral> lista;
    private List<DetalleHorario> horarios;
    private List<DiaSemana> dias;
    private List<IngresosSalidas> horas;
    private int idDia, idIngresoSalida;
    private final String INICIO = "HorarioLaboral";

    @Inject
    private HorarioLaboralDAO horarioLaboralDAO;

    @Inject
    private DetalleHorarioDAO detalleHorarioDAO;

    @Inject
    private IngresosSalidasDAO ingresosSalidasDAO;

    @Inject
    private DiaSemanaDAO diaSemanaDAO;

    public HorarioLaboralController() {
        lista = new ArrayList<>();
        horarioLaboral = new HorarioLaboral();
        horarios = new ArrayList<>();
        dias = new ArrayList<>();
        horas = new ArrayList<>();
    }

    @PostConstruct
    public void constructorHorarioLaboral() {
        lista = horarioLaboralDAO.Listar();
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

    public void setLista(List<HorarioLaboral> lista) {
        this.lista = lista;
    }

    public DetalleHorario getDetalleHorario() {
        return detalleHorario;
    }

    public void setDetalleHorario(DetalleHorario detalleHorario) {
        this.detalleHorario = detalleHorario;
    }

    public List<DetalleHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<DetalleHorario> horarios) {
        this.horarios = horarios;
    }

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public int getIdIngresoSalida() {
        return idIngresoSalida;
    }

    public void setIdIngresoSalida(int idIngresoSalida) {
        this.idIngresoSalida = idIngresoSalida;
    }

    public List<DiaSemana> getDias() {
        return dias;
    }

    public void setDias(List<DiaSemana> dias) {
        this.dias = dias;
    }

    public List<IngresosSalidas> getHoras() {
        return horas;
    }

    public void setHoras(List<IngresosSalidas> horas) {
        this.horas = horas;
    }

    public void postLoadDetalle() {
        horarios = detalleHorarioDAO.Listar(horarioLaboral.getId());
        dias = diaSemanaDAO.Listar();
        horas = ingresosSalidasDAO.Listar();
        if(horarios.isEmpty()){
             mostrarMensajeInformacion("Debe definir los los dias y horas de este horario");
        }
    }
    
    public String darFormato(Date fecha){
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    public void nuevoHorario() {
        horarioLaboral = new HorarioLaboral();
    }

    public void nuevoDetalle() {
        idDia = 0;
        idIngresoSalida = 0;
        detalleHorario = new DetalleHorario();
        detalleHorario.setHorarioLaboral(horarioLaboral);
    }
    
    public String volver(){
        horarios.clear();
        dias.clear();
        horas.clear();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
        return INICIO;
    }

    public void editarDetalle(int idDia, int idIngresoSalida) {
        this.idDia = idDia;
        this.idIngresoSalida = idIngresoSalida;
    }

    public void guardarHorario() {
        horarioLaboralDAO.setHorarioLaboral(horarioLaboral);
        if (horarioLaboral.getId() == 0) {
            if (horarioLaboralDAO.insertar() > 0) {
                horarioLaboral.setId(horarioLaboralDAO.getHorarioLaboral().getId());
                lista.add(horarioLaboral);
            } else {
                mostrarMensajeError("El horario laboral no se pudo guardar");
            }
        } else {
            if (horarioLaboralDAO.actualizar() > 0) {
                mostrarMensajeInformacion("El horario laboral se ha editado con éxito");
            } else {
                mostrarMensajeError("El horario laboral no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('managePuestoLaboralDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
    }

    public void enviar() {
        if (idDia != 0 && idIngresoSalida != 0) {
            detalleHorario.getDiaSemana().setId(idDia);
            detalleHorario.getIngresoSalida().setId(idIngresoSalida);
            detalleHorario.setHorarioLaboral(horarioLaboral);
            detalleHorarioDAO.setDetalleHorario(detalleHorario);
            if (detalleHorario.getId() == 0) {
                if (detalleHorarioDAO.insertar() > 0) {
                    mostrarMensajeInformacion("El horario se ha guardado con éxito");
                    horarios.add(detalleHorario);
                } else {
                    mostrarMensajeError("El horario no se pudo guardar");
                }
            } else {
                if (detalleHorarioDAO.actualizar() > 0) {
                    mostrarMensajeInformacion("El horario se ha editado con éxito");
                } else {
                    mostrarMensajeError("El horario no se pudo editar");
                }
            }
        } else {
            mostrarMensajeError("Debe de seleccionar un dia y un horaria");
        }
        PrimeFaces.current().executeScript("PF('manageDetalleHorarioDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-detalleHorarios");
    }
    
    public void cambiarEstado(HorarioLaboral horarioLaboral){
        horarioLaboralDAO.setHorarioLaboral(horarioLaboral);
        horarioLaboralDAO.cambiarEstado();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
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
