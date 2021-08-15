/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CiudadDAO;
import Model.Entidad.Ciudad;
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
@Named(value = "ciudadView")
@ViewScoped
public class CiudadController implements Serializable {

    private Ciudad ciudad;
    private CiudadDAO ciudadDAO;
    private List<Ciudad> lista;

    public CiudadController() {
        ciudadDAO = new CiudadDAO(new Ciudad());
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorCiudad() {
        lista = ciudadDAO.Listar();
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Ciudad> getLista() {
        return lista;
    }

    public void setLista(List<Ciudad> ciudades) {
        this.lista = ciudades;
    }

    public void abrirNuevo() {
        this.ciudad = new Ciudad();
        PrimeFaces.current().ajax().update("form:messages", "form:manage-ciudad-content");
    }

    public void enviar() {
        ciudadDAO.setCiudad(ciudad);
        if (ciudad.getId() == 0) {
            if (ciudadDAO.insertar() > 0) {
                mostrarMensajeInformacion("La ciudad se ha guardado con éxito");
                lista.add(ciudad);
            } else {
                mostrarMensajeError("La ciudad no se pudo guardar");
            }
        } else {
            if (ciudadDAO.actualizar() > 0) {
                mostrarMensajeInformacion("La ciudad se ha editado con éxito");
            } else {
                mostrarMensajeError("La ciudad no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageCiudadDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-ciudades");
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