/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.EmpleadoDAO;
import Model.Entidad.Empleado;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * @author kestradalp
 */
@Named(value = "empleadoListaView")
@ViewScoped
public class EmpleadoListaController implements Serializable {

    private Empleado empleado;
    private List<Empleado> lista;
    
    @Inject
    private EmpleadoDAO empleadoDAO;

    public EmpleadoListaController() {
        empleado = new Empleado();
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorEmpleadoLista() {
        lista = empleadoDAO.Listar();
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Empleado> getLista() {
        return lista;
    }

    public void setLista(List<Empleado> empleados) {
        lista = empleados;
    }
    
    public void nuevoEmpleado(){
        empleado = new Empleado();
    }

    public void postLoad(int idEmpleado) {
        if(idEmpleado > 0) {
            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = empleadoDAO.buscarPorId(idEmpleado);
            PrimeFaces.current().ajax().update(null, "form:dt-empleados");
        }
    }
    
    public String darFormato(Date fecha){
        return fecha != null? new SimpleDateFormat("dd/MM/yyyy").format(fecha):"";
    }
    
    public void cambiarEstado(Empleado empleado){
        empleadoDAO.setEmpleado(empleado);
        empleadoDAO.cambiarEstado();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empleados");
    }

    public void mostrarMensajeInformacion(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void mostrarMensajeError(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}