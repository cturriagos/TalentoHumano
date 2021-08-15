/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CiudadDAO;
import Model.DAO.DedicacionDAO;
import Model.DAO.EmpresaDAO;
import Model.DAO.SucursalDAO;
import Model.Entidad.Ciudad;
import Model.Entidad.Dedicacion;
import Model.Entidad.Empresa;
import Model.Entidad.Sucursal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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
@Named(value = "empresaView")
@ViewScoped
public class EmpresaController implements Serializable {

    private Empresa empresa;
    private Sucursal sucursal;
    private List<Sucursal> sucursales;
    private List<Ciudad> ciudades;
    private List<Dedicacion> dedicaciones;
    private int idCiudad, idDedicacion;

    @Inject
    private EmpresaDAO empresaDAO;

    @Inject
    private SucursalDAO sucursalDAO;

    @Inject
    private CiudadDAO ciudadDAO;

    @Inject
    private DedicacionDAO dedicacionDAO;

    public EmpresaController() {
        sucursales = new ArrayList<>();
        ciudades = new ArrayList<>();
        dedicaciones = new ArrayList<>();
    }

    @PostConstruct
    public void constructorEmpresa() {
        empresa = empresaDAO.cargar();
        sucursales = sucursalDAO.Listar();
        dedicaciones = dedicacionDAO.Listar();
        ciudades = ciudadDAO.Listar();
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }

    public List<Dedicacion> getDedicaciones() {
        return dedicaciones;
    }

    public void setDedicaciones(List<Dedicacion> dedicaciones) {
        this.dedicaciones = dedicaciones;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public int getIdDedicacion() {
        return idDedicacion;
    }

    public void setIdDedicacion(int idDedicacion) {
        this.idDedicacion = idDedicacion;
    }

    public void editar(int idDedicacion) {
        this.idDedicacion = idDedicacion;
    }

    public void postLoad() {
        if (empresa.getId() == -1 ) {
            mostrarMensajeError("Debe ingresar los datos de la Empresa");
            PrimeFaces.current().executeScript("PF('manageEmpresaDialog').show()");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa");
        }
    }

    public void nuevaSucursal() {
        idCiudad = 0;
        sucursal = new Sucursal();
        sucursal.setEmpresa(empresa);
    }

    public void editarSucursal(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public void guardarEmpresa() {
        if (idDedicacion != 0) {
            empresa.getDedicacion().setId(idDedicacion);
            empresaDAO.setEmpresa(empresa);
            if (empresa.getId() < 1) {
                if (empresaDAO.insertar() > 0) {
                    empresa.setId(empresaDAO.getEmpresa().getId());
                    mostrarMensajeInformacion("Los datos de la empresa se ha guardado con éxito");
                } else {
                    mostrarMensajeError("Los datos de la empresa no se pudo guardar");
                }
            } else {
                if (empresaDAO.actualizar() > 0) {
                    mostrarMensajeInformacion("Los datos de la empresa se ha editado con éxito");
                } else {
                    mostrarMensajeError("Los datos de la empresa no se pudo editar");
                }
            }
        } else {
            mostrarMensajeError("Debe indicar la dedicación de la empresa");
        }
        PrimeFaces.current().executeScript("PF('manageEmpresaDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empresa");
    }

    public void guardarSucursal() {
        if (idCiudad != 0) {
            sucursal.getCiudad().setId(idCiudad);
            sucursal.setEmpresa(empresa);
            sucursalDAO.setSucursal(sucursal);
            if (sucursal.getId() == 0) {
                if (sucursalDAO.insertar() > 0) {
                    mostrarMensajeInformacion("La sucursal se ha guardado con éxito");
                    sucursales.add(sucursal);
                } else {
                    mostrarMensajeError("La sucursal no se pudo guardar");
                }
            } else {
                if (sucursalDAO.actualizar() > 0) {
                    mostrarMensajeInformacion("La sucursal se ha editado con éxito");
                } else {
                    mostrarMensajeError("La sucursal no se pudo editar");
                }
            }
        } else {
            mostrarMensajeError("Debe de seleccionar la ciudad en la que esta la empresa");
        }
        PrimeFaces.current().executeScript("PF('manageSucursalDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-sucursales");
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
