/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.IngresosSalidasDAO;
import Model.Entidad.IngresosSalidas;
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
 * @author rturr
 */
@Named(value = "ingresosSalidasView")
@ViewScoped
public class IngresoSalidaController implements Serializable {

    private IngresosSalidas ingresosSalidas;
    private IngresosSalidasDAO ingresosSalidasDAO;
    private List<IngresosSalidas> lista;

    public IngresoSalidaController() {
        ingresosSalidasDAO = new IngresosSalidasDAO(new IngresosSalidas());
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorIngresosSalidas() {
        lista = ingresosSalidasDAO.Listar();
    }

    public IngresosSalidas getIngresosSalidas() {
        return ingresosSalidas;
    }

    public void setIngresosSalidas(IngresosSalidas ingresosSalidas) {
        this.ingresosSalidas = ingresosSalidas;
    }

    public List<IngresosSalidas> getLista() {
        return lista;
    }

    public void setLista(List<IngresosSalidas> ingresosSalidas) {
        this.lista = ingresosSalidas;
    }

    public void abrirNuevo() {
        this.ingresosSalidas = new IngresosSalidas();
        PrimeFaces.current().ajax().update("form:messages", "form:manage-ingresosSalidas-content");
    }

    public void enviar() {
        ingresosSalidasDAO.setIngresosSalidas(ingresosSalidas);
        if (ingresosSalidas.getId() == 0) {
            if (ingresosSalidasDAO.insertar() > 0) {
                mostrarMensajeInformacion("El registro de horas de salida e ingreso se ha guardado con éxito");
                lista.add(ingresosSalidas);
            } else {
                mostrarMensajeError("El registro de horas de salida e ingreso no se pudo guardar");
            }
        } else {
            if (ingresosSalidasDAO.actualizar() > 0) {
                mostrarMensajeInformacion("El registro de horas de salida e ingreso  se ha editado con éxito");
            } else {
                mostrarMensajeError("El registro de horas de salida e ingreso  no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageIngresosSalidasDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-ingresosSalidas");
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
