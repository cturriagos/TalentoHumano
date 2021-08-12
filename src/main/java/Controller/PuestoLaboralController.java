/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CargoDAO;
import Model.DAO.DepartamentoDAO;
import Model.DAO.PuestoLaboralDAO;
import Model.Entidad.Cargo;
import Model.Entidad.Departamento;
import Model.Entidad.PuestoLaboral;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Arialdo
 */
@Named(value = "puestoLaboralView")
@ViewScoped
public class PuestoLaboralController implements Serializable {
    private PuestoLaboral puestoLaboral;
    private PuestoLaboralDAO puestoLaboralDAO;
    private CargoDAO cargoDAO;
    private DepartamentoDAO departamentoDAO;
    private List<PuestoLaboral> lista;
    private List<Departamento> departamentos;
    private List<Cargo> cargos;
    private int idCargo,idDepartamento;
    
    public PuestoLaboralController(){
        puestoLaboral = new PuestoLaboral(0, new Cargo(), new Departamento(), new Date(), true, "");
        puestoLaboralDAO = new PuestoLaboralDAO();
        departamentoDAO = new DepartamentoDAO();
        cargoDAO = new CargoDAO();
        lista = new ArrayList<>();
        departamentos = new ArrayList<>();
        cargos = new ArrayList<>();;
    }
    
   @PostConstruct
    public void constructorDepartamento (){
        lista = puestoLaboralDAO.Listar();
        departamentos = departamentoDAO.Listar();
        cargos = cargoDAO.Listar();
   }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    
    public String enviar() {
        puestoLaboral.getCargo().setId(idCargo);
        puestoLaboral.getDepartamento().setId(idDepartamento);
        puestoLaboralDAO = new PuestoLaboralDAO(puestoLaboral);
        
        if (puestoLaboralDAO.insertar() > 0) {
            mostrarMensajeInformacion("El Puesto Laboral se ha guardado con éxito");
            puestoLaboral = new PuestoLaboral();
            lista = puestoLaboralDAO.Listar();
            return "PuestosLaborales";
        } else {
            mostrarMensajeInformacion("El Puesto Laboral no se pudo guardar");
            return "";
        }
       
    }
    
    public void onEditar(RowEditEvent<PuestoLaboral> event) {
        PuestoLaboral puestoLaboralEditado = event.getObject();
        puestoLaboralDAO = new PuestoLaboralDAO(puestoLaboralEditado);
        
        if (puestoLaboralDAO.actualizar()> 0) {
            mostrarMensajeInformacion("El Puesto Laboral se ha editado con éxito");
            lista = puestoLaboralDAO.Listar();
        } else {
            mostrarMensajeInformacion("El Puesto Laboral no se pudo editar");
        }
    }
    
    public void onCancel(RowEditEvent<PuestoLaboral> event) {
        mostrarMensajeInformacion("Se canceló la edición");
    }
    
    public String anular() {
        puestoLaboralDAO = new PuestoLaboralDAO(puestoLaboral);
        if (puestoLaboralDAO.cambiarEstado()> 0) {
            mostrarMensajeInformacion("El Puesto Laboral se ha " + (puestoLaboral.isEstado() ? "Deshabilitado" : "Habilitado") + " con éxito");
            lista = puestoLaboralDAO.Listar();
            return "puestosLaborales";
        } else {
            mostrarMensajeError("El Puesto Laboral no se pudo anular");
            return "";
        }
    }

    public PuestoLaboral getPuestoLaboral() {
        return puestoLaboral;
    }

    public void setPuestoLaboral(PuestoLaboral puestoLaboral) {
        this.puestoLaboral = puestoLaboral;
    }

    public List<PuestoLaboral> getLista() {
        return lista;
    }

    public void setLista(List<PuestoLaboral> puestos) {
        this.lista = puestos;
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
