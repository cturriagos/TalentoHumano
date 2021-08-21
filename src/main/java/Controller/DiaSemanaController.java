/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.DiaSemanaDAO;
import Model.Entidad.DiaSemana;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author cturriagos
 */
@Named(value = "diaSemanaView")
@ViewScoped
public class DiaSemanaController implements Serializable {
    private DiaSemana diaSemana;
    private List<DiaSemana> lista;
    
    @Inject
    private DiaSemanaDAO diaSemanaDAO;

    public DiaSemanaController() {
        diaSemana = new DiaSemana();
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorDiaSemana() {
        lista = diaSemanaDAO.Listar();
    }

    public List<DiaSemana> getLista() {
        return lista;
    }

    public void setLista(List<DiaSemana> lista) {
        this.lista = lista;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setCargo(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void abrirNuevo() {
        diaSemana = new DiaSemana();
    }

    /**
     * MÉTODO ENVIAR Hace un llamado al DAO para ejecutar el método
     * insertar o actualizar
     *
     */
    public void enviar() {
        diaSemanaDAO.setDiaSemana(diaSemana);
        if (diaSemana.getId() == 0) {
            if (diaSemanaDAO.insertar() > 0) {
                mostrarMensajeInformacion("El dia/semana se ha guardado con éxito");
                lista.add(diaSemana);
            } else {
                mostrarMensajeError("El dia/semana no se pudo guardar");
            }
        } else {
            if (diaSemanaDAO.actualizar() > 0) {
                mostrarMensajeInformacion("El dia/semana se ha editado con éxito");
            } else {
                mostrarMensajeError("El dia/semana no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageDiaSemanaDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-diaSemanas");
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