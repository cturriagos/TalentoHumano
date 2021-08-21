/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.EmpleadoDAO;
import Model.DAO.EmpleadoPuestoDAO;
import Model.DAO.EmpleadoSucursalDAO;
import Model.DAO.HorarioLaboralDAO;
import Model.DAO.PersonaDAO;
import Model.DAO.PuestoLaboralDAO;
import Model.DAO.SucursalDAO;
import Model.DAO.SueldoDAO;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoPuesto;
import Model.Entidad.EmpleadoSucursal;
import Model.Entidad.HorarioLaboral;
import Model.Entidad.Persona;
import Model.Entidad.PuestoLaboral;
import Model.Entidad.Sucursal;
import Model.Entidad.Sueldo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.file.UploadedFiles;

/**
 *
 * @author cturriagos
 */
@Named(value = "empleadoCrearView")
@ViewScoped
public class EmpleadoCrearController implements Serializable {

    @Inject
    private EmpleadoDAO empleadoDAO;
    private Empleado empleado;

    @Inject
    private PersonaDAO personaDAO;
    private Persona persona;

    private EmpleadoSucursalDAO empleadoSucursalDAO;
    private EmpleadoSucursal empleadoSucursal;
    private EmpleadoPuestoDAO empleadoPuestoDAO;
    private EmpleadoPuesto empleadoPuesto;
    private SueldoDAO sueldoDAO;
    private Sueldo sueldo;

    @Inject
    private PuestoLaboralDAO puestoLaboralDAO;
    private List<PuestoLaboral> puestos;
    @Inject
    private HorarioLaboralDAO horarioLaboralDAO;
    private List<HorarioLaboral> horarios;
    @Inject
    private SucursalDAO sucursalDAO;
    private List<Sucursal> sucursales;
    private boolean saltar, newPersona, familia;
    private UploadedFiles file;

    public EmpleadoCrearController() {
        inicializar();
        puestos = new ArrayList<>();
        horarios = new ArrayList<>();
        sucursales = new ArrayList<>();
    }

    private void inicializar() {
        empleado = new Empleado();
        persona = new Persona();
        empleadoSucursal = new EmpleadoSucursal();
        empleadoPuesto = new EmpleadoPuesto();
        sueldo = new Sueldo();
    }

    @PostConstruct
    public void constructorEmpleado() {
        personaDAO.setConexion(empleadoDAO.obtenerConexion());
        empleadoPuestoDAO = new EmpleadoPuestoDAO(empleadoDAO.obtenerConexion());
        empleadoPuestoDAO.setConexion(empleadoDAO.obtenerConexion());
        empleadoSucursalDAO = new EmpleadoSucursalDAO(empleadoDAO.obtenerConexion());
        empleadoSucursalDAO.setConexion(empleadoDAO.obtenerConexion());
        sueldoDAO = new SueldoDAO(empleadoDAO.obtenerConexion());
        sueldoDAO.setConexion(empleadoDAO.obtenerConexion());
        puestos = puestoLaboralDAO.Activos();
        horarios = horarioLaboralDAO.Activos();
        sucursales = sucursalDAO.Listar();
    }

    public UploadedFiles getFile() {
        return file;
    }

    public void setFile(UploadedFiles file) {
        this.file = file;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<PuestoLaboral> getPuestos() {
        return puestos;
    }

    public void setPuestos(List<PuestoLaboral> puestos) {
        this.puestos = puestos;
    }

    public List<HorarioLaboral> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioLaboral> horarios) {
        this.horarios = horarios;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    public boolean isSaltar() {
        return saltar;
    }

    public void setSaltar(boolean saltar) {
        this.saltar = saltar;
    }

    public Sueldo getSueldo() {
        return sueldo;
    }

    public void setSueldo(Sueldo sueldo) {
        this.sueldo = sueldo;
    }

    public boolean isFamilia() {
        return familia;
    }

    public void setFamilia(boolean familia) {
        this.familia = familia;
    }

    public boolean isNewPersona() {
        return newPersona;
    }

    public void setNewPersona(boolean newPersona) {
        this.newPersona = newPersona;
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

    public void setEmplaPuesto(EmpleadoPuesto empleadoPuesto) {
        this.empleadoPuesto = empleadoPuesto;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String guardar() {
        String mensaje = "";
        boolean result = false;
        personaDAO.setPersona(persona);
        if (personaDAO.insertar() > 0) {
            persona.setId(personaDAO.getPersona().getId());
            empleado.setPersona(persona);
            empleadoDAO.setEmpleado(empleado);
            if (empleadoDAO.insertar() > 0) {
                empleado.setId(empleadoDAO.getEmpleado().getId());
                empleadoPuesto.setEmpleado(empleado);
                empleadoPuestoDAO.setEmpleadoPuesto(empleadoPuesto);
                if (empleadoPuestoDAO.insertar() > 0) {
                    empleadoPuesto.setId(empleadoPuestoDAO.getEmpleadoPuesto().getId());
                    empleadoSucursal.setEmpleado(empleado);
                    empleadoSucursalDAO.setEmpleadoSucursal(empleadoSucursal);
                    if (empleadoSucursalDAO.insertar() > 0) {
                        empleadoSucursal.setId(empleadoSucursalDAO.getEmpleadoSucursal().getId());
                        sueldo.setEmpleado(empleado);
                        sueldoDAO.setSueldo(sueldo);
                        if (sueldoDAO.insertar() > 0) {
                            sueldo.setId(sueldoDAO.getSueldo().getId());
                            result = true;
                        } else {
                            mensaje = "El sueldo no se pudo asignar";
                        }
                    } else {
                        mensaje = "La sucursal no se pudo asignar";
                    }
                } else {
                    mensaje = "El puesto no se pudo asignar";
                }
            } else {
                mensaje = "El empleado no se pudo guardar";
            }
        } else {
            mensaje = "La persona no se pudo guardar";
        }
        if (result) {
            mostrarMensajeInformacion("Datos guardados correctamente");
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
