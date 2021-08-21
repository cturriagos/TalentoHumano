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
import Model.DAO.PuestoLaboralDAO;
import Model.DAO.SucursalDAO;
import Model.Entidad.EmpleadoPuesto;
import Model.Entidad.EmpleadoSucursal;
import Model.Entidad.HorarioLaboral;
import Model.Entidad.PuestoLaboral;
import Model.Entidad.Sucursal;
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
@Named(value = "empleadoPuestoView")
@ViewScoped
public class EmpleadoPuestoController implements Serializable {
    private EmpleadoPuesto empleadoPuesto;
    private EmpleadoSucursal empleadoSucursal;
    
    @Inject
    private EmpleadoPuestoDAO empleadoPuestoDAO;
    private List<PuestoLaboral> puestos;
    private List<HorarioLaboral> horarios;
    @Inject
    private EmpleadoSucursalDAO empleadoSucursalDAO;
    private List<Sucursal> sucursales;
    
    private int idPuesto, idHorario, idSucursal;

    public EmpleadoPuestoController() {
        empleadoPuesto = new EmpleadoPuesto();
        empleadoSucursal = new EmpleadoSucursal();
        puestos = new ArrayList<>();
        horarios = new ArrayList<>();
        sucursales = new ArrayList<>();
    }

    @PostConstruct
    public void constructorEmpleadoPuestoEditar() {
        PuestoLaboralDAO puestoLaboralDAO = new PuestoLaboralDAO();
        puestos = puestoLaboralDAO.Activos();
        HorarioLaboralDAO horarioLaboralDAO = new HorarioLaboralDAO();
        horarios = horarioLaboralDAO.Activos();
        SucursalDAO sucursalDAO = new SucursalDAO();
        sucursales = sucursalDAO.Listar();
        
    }

    public EmpleadoPuesto getEmpleadoPuesto() {
        return empleadoPuesto;
    }

    public void setEmpleadoPuesto(EmpleadoPuesto empleadoPuesto) {
        this.empleadoPuesto = empleadoPuesto;
    }

    public EmpleadoSucursal getEmpleadoSucursal() {
        return empleadoSucursal;
    }

    public void setEmpleadoSucursal(EmpleadoSucursal empleadoSucursal) {
        this.empleadoSucursal = empleadoSucursal;
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

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }
    
    public void postLoad(int idEmpleado) {
        if (idEmpleado > 0 ) {
            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleadoDAO.setEmpleado(empleadoDAO.buscarPorId(idEmpleado));
            empleadoSucursal = empleadoSucursalDAO.buscar(empleadoDAO.getEmpleado());
            empleadoPuesto = empleadoPuestoDAO.buscar(empleadoDAO.getEmpleado());
            idPuesto = empleadoPuesto.getPuestoLaboral().getId();
            idHorario = empleadoPuesto.getHorarioLaboral().getId();
            idSucursal = empleadoSucursal.getSucursal().getId();
            PrimeFaces.current().ajax().update(null, "form:dt-puesto");
        }
    }

    public void guardar() {
        if (empleadoPuesto.getPuestoLaboral().getId() != idPuesto || empleadoPuesto.getHorarioLaboral().getId() != idHorario) {
            empleadoPuestoDAO.desactivar();
            empleadoPuesto.getPuestoLaboral().setId(idPuesto);
            empleadoPuesto.getHorarioLaboral().setId(idHorario);
            empleadoPuesto.setId(0);
            if (empleadoPuestoDAO.insertar(empleadoPuesto) > 0) {
                empleadoPuesto.setId(empleadoPuestoDAO.getEmpleadoPuesto().getId());
                mostrarMensajeInformacion("Los datos del puesto laboral se ha guardado con éxito");
            } else {
                mostrarMensajeError("Los datos del puesto laboral no se pudieron guardar");
            }
        } else {
            if (empleadoPuestoDAO.actualizar(empleadoPuesto) > 0) {
                mostrarMensajeInformacion("Los datos del puesto laboral se ha actualizado con éxito");
            } else {
                mostrarMensajeError("Los datos del puesto laboral no se pudieron actualizar");
            }
        }
        if (empleadoSucursal.getSucursal().getId() != idSucursal) {
            empleadoSucursalDAO.desactivar();
            empleadoSucursal.getSucursal().setId(idSucursal);
            empleadoSucursal.setId(0);
            if (empleadoSucursalDAO.insertar(empleadoSucursal) > 0) {
                empleadoSucursal.setId(empleadoSucursalDAO.getEmpleadoSucursal().getId());
                mostrarMensajeInformacion("Los datos de la sucursal se ha guardado con éxito");
            } else {
                mostrarMensajeError("Los datos de la sucursal no se pudieron guardar");
            }
        } else {
            if (empleadoPuestoDAO.actualizar(empleadoPuesto) > 0) {
                mostrarMensajeInformacion("Los datos de la sucursal se ha actualizado con éxito");
            } else {
                mostrarMensajeError("Los datos de la sucursal no se pudieron actualizar");
            }
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puesto");
    }

    //  MENSAJE DE ERROR
    public void mostrarMensajeError(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
