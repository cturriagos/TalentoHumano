/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.PersonaDAO;
import Model.Entidad.Persona;
import java.io.Serializable;
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
@Named(value = "personaView")
@ViewScoped
public class PersonaController implements Serializable {

    private Persona persona;

    @Inject
    private PersonaDAO personaDAO;

    public Persona getPersona() {
        return persona;
    }

    public void cargar(int id) {
        if (id > 0) {
            persona = personaDAO.buscarPorId(id);
        } else {
            persona = new Persona();
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personas");
    }

    public void enviar() {
        personaDAO.setPersona(persona);
        if (persona.getId() == 0) {
            if (personaDAO.insertar() > 0) {
                mostrarMensajeInformacion("La persona se ha guardado con éxito");
            } else {
                mostrarMensajeError("La persona no se pudo guardar");
            }
        } else {
            if (personaDAO.actualizar() > 0) {
                mostrarMensajeInformacion("La persona se ha editado con éxito");
            } else {
                mostrarMensajeError("La persona no se pudo editar");
            }
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-personas");
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
