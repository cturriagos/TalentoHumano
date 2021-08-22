/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CargoDAO;
import Model.Entidad.Cargo;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ClasK7
 */
@Named(value = "cargoView")
@ViewScoped
public class CargoControlller implements Serializable {

    private Cargo cargo;
    private CargoDAO cargoDAO;
    private List<Cargo> lista;

    public CargoControlller() {
        cargoDAO = new CargoDAO(new Cargo());
        lista = new ArrayList<>();
    }

    @PostConstruct
    public void constructorCargo() {
        lista = cargoDAO.Listar();
    }

    public List<Cargo> getLista() {
        return lista;
    }

    public void setLista(List<Cargo> lista) {
        this.lista = lista;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void abrirNuevo() {
        this.cargo = new Cargo();
    }

    /**
     * MÉTODO ENVIAR Hace un llamado al DAO para ejecutar el método
     * insertar o actualizar
     *
     */
    public void enviar() {
        cargoDAO.setCargo(cargo);
        if (cargo.getId() == 0) {
            if (cargoDAO.insertar() > 0) {
                mostrarMensajeInformacion("El cargo se ha guardado con éxito");
                lista.add(cargo);
            } else {
                mostrarMensajeError("El cargo no se pudo guardar");
            }
        } else {
            if (cargoDAO.actualizar() > 0) {
                mostrarMensajeInformacion("El cargo se ha editado con éxito");
            } else {
                mostrarMensajeError("El cargo no se pudo editar");
            }
        }
        PrimeFaces.current().executeScript("PF('manageCargoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-cargos");
    }

    public void onCancel(RowEditEvent<Cargo> event) {
        mostrarMensajeInformacion("Se canceló");
    }

    //  MENSAJE DE AVISO
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
