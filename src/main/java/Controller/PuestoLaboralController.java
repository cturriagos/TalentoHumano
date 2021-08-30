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
@Named(value = "puestoLaboralView")
@ViewScoped
public class PuestoLaboralController implements Serializable {

    private PuestoLaboral puestoLaboral;
    private List<PuestoLaboral> lista;
    private List<Departamento> departamentos;
    private List<Cargo> cargos;
    private int idDepartamento, idCargo;

    @Inject
    private CargoDAO cargoDAO;
    
    @Inject
    private PuestoLaboralDAO puestoLaboralDAO;
    
    @Inject
    private DepartamentoDAO departamentoDAO;
        
    public PuestoLaboralController() {
        lista = new ArrayList<>();
        departamentos = new ArrayList<>();
        cargos = new ArrayList<>();
    }

    @PostConstruct
    public void constructorDepartamento() {
        lista = puestoLaboralDAO.Listar();
        departamentos = departamentoDAO.Listar();
        cargos = cargoDAO.Listar();
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

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }
    
    public String darFormato(Date fecha){
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    public void abrirNuevo() {
        idCargo = 0;
        idDepartamento = 0;
        puestoLaboral = new PuestoLaboral(0, new Cargo(), new Departamento(), new Date(), true, "");
    }

    public void abrirEditar(int idCargo, int idDepartamento) {
        this.idCargo = idCargo;
        this.idDepartamento = idDepartamento;
    }
    
    
    public void asignarCargoDepartamento(){
        for(Cargo cargo : cargos){
            if(cargo.getId() == idCargo){
                puestoLaboral.setCargo(cargo);
                break;
            }
        }
        for(Departamento departamento : departamentos){
            if(departamento.getId() == idDepartamento){
                puestoLaboral.setDepartamento(departamento);
                break;
            }
        }
    }

    public void enviar() {
        if (idCargo != 0 && idDepartamento != 0) {
            asignarCargoDepartamento();
            puestoLaboralDAO.setPuestoLaboral(puestoLaboral);
            if (puestoLaboral.getId() == 0) {
                if (puestoLaboralDAO.insertar() > 0) {
                    mostrarMensajeInformacion("El Puesto Laboral se ha guardado con éxito");
                    lista.add(puestoLaboral);
                } else {
                    mostrarMensajeError("El Puesto Laboral no se pudo guardar");
                }
            } else {
                if (puestoLaboralDAO.actualizar() > 0) {
                    mostrarMensajeInformacion("El Puesto Laboral se ha editado con éxito");
                } else {
                    mostrarMensajeError("El Puesto Laboral no se pudo editar");
                }
            }
        }else{
            mostrarMensajeError("Debe de seleccionar un cargo y un departamento");
        }
        PrimeFaces.current().executeScript("PF('managePuestoLaboralDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
    }
    
    public void cambiarEstado(PuestoLaboral puestoLaboral){
        puestoLaboralDAO.setPuestoLaboral(puestoLaboral);
        puestoLaboralDAO.cambiarEstado();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-puestoLaborals");
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
