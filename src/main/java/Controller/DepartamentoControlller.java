/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.DepartamentoDAO;
import Model.Entidad.Departamento;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author rturr
 */
@Named(value = "departamentoView")
@ViewScoped
public class DepartamentoControlller implements Serializable {

    private Departamento departamento = new Departamento();
    private DepartamentoDAO departamentoDAO = new DepartamentoDAO();
    private List<Departamento> lista;

    public DepartamentoControlller() {
        departamento = new Departamento();
        departamentoDAO = new DepartamentoDAO(new Departamento());
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorDepartamento() {
        lista = departamentoDAO.Listar();
    }

    /**
     * MÉTODO ENVIAR Hace un llamado al DAO para ejecutar el método
     * insertarDepartamento
     *
     */
    public void enviar() {
        departamentoDAO.setDepartamento(departamento);
        if (departamento.getId() == 0) {
            if (departamentoDAO.insertar() > 0) {
                departamento.setId(departamentoDAO.getDepartamento().getId());
                mostrarMensajeInformacion("El departamento se ha guardado con éxito");
                lista.add(departamento);
            } else {
                mostrarMensajeError("El departamento no se pudo guardar");
            }
        } else {
            if (departamentoDAO.actualizar() > 0) {
                mostrarMensajeInformacion("El departamento se ha editado con éxito");
            } else {
                mostrarMensajeError("El departamento no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageDepartamentoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-departamentos");
    }
    
    public void cambiarEstado(Departamento departamento){
        departamentoDAO.setDepartamento(departamento);
        departamentoDAO.cambiarEstado();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
    }
    
    public String darFormato(Date fecha){
        return fecha != null? new SimpleDateFormat("dd/MM/yyyy").format(fecha):"";
    }

    public void abrirNuevo() {
        this.departamento = new Departamento();
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Departamento> getLista() {
        return lista;
    }

    public void setLista(List<Departamento> departamentos) {
        this.lista = departamentos;
    }

    //  MENSAJE DE AVISO
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
