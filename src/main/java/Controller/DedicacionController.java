/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.DedicacionDAO;
import Model.Entidad.Dedicacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ClasK7
 */
@Named(value = "dedicacionView")
@ViewScoped
public class DedicacionController implements Serializable {

    private Dedicacion dedicacion;
    private DedicacionDAO dedicacionDAO;
    private List<Dedicacion> lista;

    public DedicacionController() {
        dedicacionDAO = new DedicacionDAO(new Dedicacion());
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorDedicacion() {
        lista = dedicacionDAO.Listar();
    }

    public Dedicacion getDedicacion() {
        return dedicacion;
    }

    public void setDedicacion(Dedicacion dedicacion) {
        this.dedicacion = dedicacion;
    }

    public List<Dedicacion> getLista() {
        return lista;
    }

    public void setLista(List<Dedicacion> dedicaciones) {
        this.lista = dedicaciones;
    }

    public void abrirNuevo() {
        this.dedicacion = new Dedicacion();
        PrimeFaces.current().ajax().update("form:messages", "form:manage-dedicacion-content");
    }

    public void enviar() {
        dedicacionDAO.setDedicacion(dedicacion);
        if (dedicacion.getId() == 0) {
            if (dedicacionDAO.insertar() > 0) {
                mostrarMensajeInformacion("La dedicacion se ha guardado con éxito");
                lista.add(dedicacion);
            } else {
                mostrarMensajeError("La dedicacion no se pudo guardar");
            }
        } else {
            if (dedicacionDAO.actualizar() > 0) {
                mostrarMensajeInformacion("La dedicacion se ha editado con éxito");
            } else {
                mostrarMensajeError("La dedicacion no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageDedicacionDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-dedicaciones");
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