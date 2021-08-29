/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.CargaFamiliarDAO;
import Model.DAO.EmpleadoDAO;
import Model.DAO.EmpleadoPuestoDAO;
import Model.DAO.EmpleadoReservaDAO;
import Model.DAO.EmpleadoSucursalDAO;
import Model.DAO.SueldoDAO;
import Model.Entidad.CargaFamiliar;
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoPuesto;
import Model.Entidad.EmpleadoReserva;
import Model.Entidad.EmpleadoSucursal;
import Model.Entidad.Sueldo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author cturriagos
 */
@Named(value = "empleadoView")
@ViewScoped
public class EmpleadoController implements Serializable {

    private UploadedFile originalFile;
    private StreamedContent file;
    private Empleado empleado;
    private String resumeReserva;

    @Inject
    private EmpleadoSucursalDAO empleadoSucursalDAO;
    private EmpleadoSucursal empleadoSucursal;

    @Inject
    private EmpleadoReservaDAO empleadoReservaDAO;
    private EmpleadoReserva empleadoReserva;

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
        empleadoReserva = new EmpleadoReserva();
        cargaFamiliar = new CargaFamiliar();
        sueldo = new Sueldo();
    }

    public void postLoad(int idEmpleado) {
        if (idEmpleado > 0) {
            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleado = empleadoDAO.buscarPorId(idEmpleado);
            empleadoSucursal = empleadoSucursalDAO.buscar(empleado);
            empleadoReserva = empleadoReservaDAO.buscar(empleado);
            empleadoPuesto = empleadoPuestoDAO.buscar(empleado);
            cargaFamiliar = cargaFamiliarDAO.buscar(empleado);
            sueldo = sueldoDAO.Actual(empleado);
            resumenReserva();
            PrimeFaces.current().ajax().update(null, "form:dt-empleado");
        }
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

    public EmpleadoReserva getEmpleadoReserva() {
        return empleadoReserva;
    }

    public void setEmpleadoReserva(EmpleadoReserva empleadoReserva) {
        this.empleadoReserva = empleadoReserva;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public List<Sueldo> historialSueldo() {
        return sueldoDAO.historial(empleado);
    }
    
    private void resumenReserva(){
            if (empleadoReserva.getFormaPago() != 0) {
                resumeReserva = "$ " + (empleadoReserva.getFormaPago() * sueldo.getValor()) + " (" + (empleadoReserva.getFormaPago() == 1 ? "ANUAL" : "MENSUAL") + ")";
            } else {
                resumeReserva = "S/D";
            }
    }

    public String getResumenReserva() {
        return resumeReserva;
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.originalFile = null;
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalFile = file;
            FacesMessage msg = new FacesMessage("Exito", this.originalFile.getFileName() + " esta subido.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void cambiarSueldo() {
        sueldoDAO.setSueldo(sueldo);
        sueldoDAO.desactivar();
        if (sueldoDAO.insertar() > 0) {
            sueldo.setId(sueldoDAO.getSueldo().getId());
            empleadoPuestoDAO.setEmpleadoPuesto(empleadoPuesto);
            empleadoPuestoDAO.actualizar();
            mostrarMensajeInformacion("Se ha cambiado el sueldo con éxito");
        }
        PrimeFaces.current().executeScript("PF('manageSueldoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
    }

    public String getPathSistem() {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            return ctx.getRealPath("/");
        } catch (Exception e) {
            mostrarMensajeError("getPath() " + e.getLocalizedMessage());
        }
        return "";
    }

    public void guardarCargaFamiliar() {
        try {
            cargaFamiliar.setPathValidation("empleado-CF-" + empleado.getPersona().getIdentificacion() + ".pdf");
            cargaFamiliarDAO.setCargaFamiliar(cargaFamiliar);
            boolean guardarFile = false;
            if (cargaFamiliar.getId() < 1) {
                if (cargaFamiliarDAO.insertar() > 0) {
                    cargaFamiliar.setId(cargaFamiliarDAO.getCargaFamiliar().getId());
                    mostrarMensajeInformacion("Los datos de la carga familiar se ha guardado con éxito");
                    guardarFile = true;
                } else {
                    mostrarMensajeError("Los datos de la carga familiar se pudieron guardar");
                }
            } else {
                if (cargaFamiliarDAO.actualizar() > 0) {
                    mostrarMensajeInformacion("Los datos de la carga familiar se ha actualizado con éxito");
                    guardarFile = true;
                } else {
                    mostrarMensajeError("Los datos de la carga familiar se pudieron actualizar");
                }
            }
            if (guardarFile) {
                byte[] contenido = originalFile.getContent();
                File archivo = new File(getPathSistem() + "resources/documentos/" + cargaFamiliar.getPathValidation());
                archivo.createNewFile();
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(contenido);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarMensajeError(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarMensajeError(ex.getMessage());
        }
        PrimeFaces.current().executeScript("PF('manageCargaFamiliarDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
    }

    public void guardarReserva() {
        empleadoReservaDAO.setEmpleadoReserva(empleadoReserva);
        if (resumeReserva.equalsIgnoreCase("S/D")) {
            if (empleadoReservaDAO.insertar() > 0) {
                mostrarMensajeInformacion("Los datos se guardarón con éxito");
            }else{
                mostrarMensajeError("No se pudierón guardar los datos");
            }
        }else{
            if (empleadoReservaDAO.actualizar() > 0) {
                mostrarMensajeInformacion("Los datos se actualizarón con éxito");
            }else{
                mostrarMensajeError("No se pudierón actualizar los datos");
            }
        }
        resumenReserva();
        PrimeFaces.current().executeScript("PF('manageReservaDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
    }
    
    public String darFormato(Date fecha){
        return fecha != null? new SimpleDateFormat("dd/MM/yyyy").format(fecha):"";
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
