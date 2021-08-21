/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.EmpleadoDAO;
import Model.DAO.PersonaDAO;
import Model.Entidad.Empleado;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ClasK7
 */
@Named(value = "empleadoEditarView")
@ViewScoped
public class EmpleadoEditarController implements Serializable {

    @Inject
    private EmpleadoDAO empleadoDAO;
    private Empleado empleado;

    private PersonaDAO personaDAO;

    public EmpleadoEditarController() {
        inicializar();
    }

    private void inicializar() {
        empleado = new Empleado();
        personaDAO = new PersonaDAO();
    }

    @PostConstruct
    public void constructorEmpleado() {
        personaDAO.setConexion(empleadoDAO.obtenerConexion());
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void postLoad(int idEmpleado) {
        if(idEmpleado > 0) {
            empleado = empleadoDAO.buscarPorId(idEmpleado);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
        }
    }

    public String actualizar() {
        String mensaje = "";
        boolean result = false;
            empleadoDAO.setEmpleado(empleado);
            personaDAO.setPersona(empleado.getPersona());
            if (personaDAO.actualizar() > 0) {
                empleadoDAO.setEmpleado(empleado);
                if (empleadoDAO.actualizar() > 0) {
                    result = true;
                } else {
                    mensaje = "El empleado no se pudo actualizar";
                }
            } else {
                mensaje = "La persona no se pudo actualizar";
            }
        if (result) {
            mostrarMensajeInformacion("Datos actualizados correctamente");
            return "Empleado";
        } else {
            mostrarMensajeError(mensaje);
            return "";
        }
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
