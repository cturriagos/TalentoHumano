/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.AsistenciaDAO;
import Model.DAO.DetalleHorarioDAO;
import Model.DAO.EmpleadoDAO;
import Model.DAO.EmpleadoPuestoDAO;
import Model.Entidad.Asistencia;
import Model.Entidad.DetalleHorario;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoPuesto;
import java.awt.event.ActionEvent;
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
 * @author kestradalp
 */
@Named(value = "asistenciaView")
@ViewScoped
public class AsistenciaController implements Serializable {

    @Inject
    private AsistenciaDAO asistenciaDAO;
    private Asistencia asistencia;
    @Inject
    private EmpleadoPuestoDAO empleadoPuestoDAO;
    @Inject
    private EmpleadoDAO empleadoDAO;
    private List<Empleado> empleados;
    @Inject
    private DetalleHorarioDAO detalleHorarioDAO;
    private List<DetalleHorario> horarios;

    public AsistenciaController() {
        asistencia = new Asistencia();
        empleados = new ArrayList<>();
        horarios = new ArrayList<>();
    }

    @PostConstruct
    public void constructorAsistencia() {
        empleados = empleadoDAO.activos();
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<DetalleHorario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<DetalleHorario> horarios) {
        this.horarios = horarios;
    }

    public void empleadoSeleccionado() {
        asistencia.setEmpleadoPuesto(empleadoPuestoDAO.buscar(asistencia.getEmpleadoPuesto().getEmpleado()));
        horarios = detalleHorarioDAO.buscar(asistencia.getEmpleadoPuesto());
        cargarDatos();
    }
    
    public void cargarDatos(){
        asistencia = asistenciaDAO.buscar(asistencia.getEmpleadoPuesto(), asistencia.getFecha(), asistencia.getDetalleHorario());
    }

    public void guardar() {
        asistenciaDAO.setAsistencia(asistencia);
        if (!asistencia.getIngreso().isEmpty() && asistencia.getSalida().isEmpty()) {
            if (asistenciaDAO.insertar() > 0) {
                mostrarMensajeInformacion("Se marco la hora de ingreso");
            } else {

                mostrarMensajeError("No se pudo marcar la hora de ingreso");
            }
        } else {
            if (!asistencia.getIngreso().isEmpty() && !asistencia.getSalida().isEmpty()) {
                if (asistenciaDAO.actualizar()> 0) {
                    mostrarMensajeInformacion("Se marco la hora de salida");
                } else {

                    mostrarMensajeError("No se pudo marcar la hora de salida");
                }
            }else{
                mostrarMensajeError("Debe de ingresar un valor en los campos");
            }
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-asistencia");
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