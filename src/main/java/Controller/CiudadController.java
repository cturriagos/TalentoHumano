/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CiudadDAO;
import Model.DAO.ProvinciaDAO;
import Model.Entidad.Ciudad;
import Model.Entidad.Provincia;
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
 * @author rturr
 */
@Named(value = "ciudadView")
@ViewScoped
public class CiudadController implements Serializable {

    @Inject
    private CiudadDAO ciudadDAO;
    private List<Ciudad> ciudades;
    private Ciudad ciudad;
    @Inject
    private ProvinciaDAO provinciaDAO;
    private List<Provincia> provincias;
    
    private int idProvincia;

    public CiudadController() {
        ciudad = new Ciudad();
        ciudades = new ArrayList<>();
        provincias = new ArrayList<>();
    }

    @PostConstruct
    public void constructorCiudad() {
        this.idProvincia = 0;
        this.ciudades = ciudadDAO.Listar();
        this.provincias = provinciaDAO.Listar();
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }
    
    public void editar(int id){
        idProvincia = id;
    }
    
    public void cancelar(){
        if (idProvincia != 0 ){
            ciudad.getProvincia().setId(idProvincia);
            idProvincia = 0;
        }
    }
    
    public void cargarProvincia(){
        for(Provincia provincia : provincias){
            if (provincia.getId() == ciudad.getProvincia().getId()){
                this.ciudad.getProvincia().setNombre(provincia.getNombre());
                break;
            }
        }
    }

    public void abrirNuevo() {
        this.ciudad = new Ciudad();
        idProvincia = ciudad.getProvincia().getId();
        PrimeFaces.current().ajax().update("form:messages", "form:manage-ciudad-content");
    }

    public void enviar() {
        cargarProvincia();
        ciudadDAO.setCiudad(ciudad);
        if (ciudad.getId() == 0) {
            if (ciudadDAO.insertar() > 0) {
                mostrarMensajeInformacion("La ciudad se ha guardado con éxito");
                ciudades.add(ciudad);
            } else {
                mostrarMensajeError("La ciudad no se pudo guardar");
            }
        } else {
            if (ciudadDAO.actualizar() > 0) {
                mostrarMensajeInformacion("La ciudad se ha editado con éxito");
            } else {
                ciudad.getProvincia().setId(idProvincia);
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