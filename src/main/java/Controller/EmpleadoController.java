/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CargaFamiliarDAO;
import Model.DAO.EmpleadoDAO;
import Model.DAO.EmpleadoPuestoDAO;
import Model.DAO.EmpleadoSucursalDAO;
import Model.DAO.SueldoDAO;
import Model.Entidad.CargaFamiliar;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoPuesto;
import Model.Entidad.EmpleadoSucursal;
import Model.Entidad.Sueldo;
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
 * @author cturriagos
 */
@Named(value = "empleadoView")
@ViewScoped
public class EmpleadoController implements Serializable {

    private Empleado empleado;
    
    @Inject
    private EmpleadoSucursalDAO empleadoSucursalDAO;
    private EmpleadoSucursal empleadoSucursal;
    
    @Inject
    private EmpleadoPuestoDAO empleadoPuestoDAO;
    private EmpleadoPuesto empleadoPuesto;
    
    @Inject
    private CargaFamiliarDAO cargaFamiliarDAO;
    private CargaFamiliar cargaFamiliar;
    
    @Inject
    private SueldoDAO sueldoDAO;
    private Sueldo sueldo;

    public EmpleadoController() {
        empleado = new Empleado();
        empleadoPuesto = new EmpleadoPuesto();
        empleadoSucursal = new EmpleadoSucursal();
        cargaFamiliar = new CargaFamiliar();
        sueldo = new Sueldo();
    }

    @PostConstruct
    public void constructorEmpleado() {
        
    }

    public void mostrarMensajeInformacion(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public EmpleadoSucursal getEmpleadoSucursal() {
        return empleadoSucursal;
    }

    public void setEmpleadoSucursal(EmpleadoSucursal empleadoSucursal) {
        this.empleadoSucursal = empleadoSucursal;
    }

    public EmpleadoPuesto getEmpleadoPuesto() {
        return empleadoPuesto;
    }

    public void setEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) {
        this.empleadoPuesto = empleadoPuesto;
    }

    public CargaFamiliar getCargaFamiliar() {
        return cargaFamiliar;
    }

    public void setCargaFamiliar(CargaFamiliar cargaFamiliar) {
        this.cargaFamiliar = cargaFamiliar;
    }

    public Sueldo getSueldo() {
        return sueldo;
    }

    public void setSueldo(Sueldo sueldo) {
        this.sueldo = sueldo;
    }

    public void postLoad(int idEmpleado) {
        if(idEmpleado > 0) {
            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = empleadoDAO.buscarPorId(idEmpleado);
            empleadoSucursal = empleadoSucursalDAO.buscar(empleado);
            empleadoPuesto = empleadoPuestoDAO.buscar(empleado);
            sueldo = sueldoDAO.buscar(empleado);
            PrimeFaces.current().ajax().update(null, "form:DATOS");
        }
    }
    
    public void cambiarSueldo(){
        sueldoDAO.setSueldo(sueldo);
        sueldoDAO.desactivar();
        if (sueldoDAO.insertar()>0){
            sueldo.setId(sueldoDAO.getSueldo().getId());
            empleadoPuestoDAO.setEmpleadoPuesto(empleadoPuesto);
            empleadoPuestoDAO.actualizar();
            mostrarMensajeInformacion("Se ha cambiado el sueldo con éxito");
        }
        PrimeFaces.current().executeScript("PF('manageSueldoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
    }

    //  MENSAJE DE ERROR
    public void mostrarMensajeError(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
