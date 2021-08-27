/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.ProvinciaDAO;
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
 * @author kestradalp
 */
@Named(value = "provinciaView")
@ViewScoped
public class ProvinciaController implements Serializable {
    @Inject
    private ProvinciaDAO provinciaDAO;
    private Provincia provincia;
    private List<Provincia> provincias;

    public ProvinciaController() {
        provincia = new Provincia();
        provincias = new ArrayList<>();
    }

    @PostConstruct
    public void constructorRolPago() {
        provincias = provinciaDAO.Listar();
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public void nuevo() {
        this.provincia = new Provincia();
        PrimeFaces.current().ajax().update("form:messages", "form:manage-provincia-content");
    }

    public void enviar() {
        provinciaDAO.setProvincia(provincia);
        if (provincia.getId() == 0) {
            if (provinciaDAO.insertar() > 0) {
                mostrarMensajeInformacion("La provincia se ha guardado con éxito");
                provincias.add(provincia);
            } else {
                mostrarMensajeError("La provincia no se pudo guardar");
            }
        } else {
            if (provinciaDAO.actualizar() > 0) {
                mostrarMensajeInformacion("La provincia se ha editado con éxito");
            } else {
                mostrarMensajeError("La provincia no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageProvinciaDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-provincias");
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
