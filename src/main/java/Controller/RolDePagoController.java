/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.AmonestacionDAO;
import Model.DAO.DetalleRolPagoDAO;
import Model.DAO.EmpleadoReservaDAO;
import Model.DAO.MultaDAO;
import Model.DAO.RolPagosDAO;
import Model.DAO.SueldoDAO;
import Model.DAO.SuspencionDAO;
import Model.Entidad.Amonestacion;
import Model.Entidad.DetalleRolPago;
import Model.Entidad.EmpleadoReserva;
import Model.Entidad.Multa;
import Model.Entidad.RolPagos;
import Model.Entidad.Sueldo;
import Model.Entidad.Suspencion;
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
import org.apache.commons.math3.util.Precision;
import org.primefaces.PrimeFaces;

/**
 *
 * @author kestradalp
 */
@Named(value = "rolDePagoView")
@ViewScoped
public class RolDePagoController implements Serializable{
    
    @Inject
    private DetalleRolPagoDAO detalleRolPagoDAO;
    
    private EmpleadoReserva empleadoReserva;
    private List<DetalleRolPago> detalles;
    private Amonestacion amonestacion;
    private Suspencion suspencion;
    private RolPagos rolPagos;
    private Sueldo sueldo;
    private Multa multa;
    
    private float aportesIESS, decimoTercero, decimoCuarto, fondosReserva, subTotal, total;
    private int horasLaboradas, horasSuplementarias;

    public RolDePagoController() {
        rolPagos = new RolPagos();
        sueldo = new Sueldo();
        empleadoReserva = new EmpleadoReserva();
        suspencion = new Suspencion();
        amonestacion = new Amonestacion();
        multa = new Multa();
        detalles = new ArrayList<>();
    }
    
    @PostConstruct
    public void constructorRolDePago() {
        aportesIESS = 0;
        decimoTercero = 0;
        decimoCuarto = 0;
        fondosReserva = 0;
        subTotal = 0;
        total = 0;
    }
    
    public void postLoad(int idRolDePago, boolean nuevo) {
        RolPagosDAO rolPagosDAO = new RolPagosDAO();
        this.rolPagos = rolPagosDAO.buscarPorId(idRolDePago);
        detalles = detalleRolPagoDAO.buscar(rolPagos);
        SueldoDAO sueldoDAO = new SueldoDAO();
        EmpleadoReservaDAO empleadoReservaDAO = new EmpleadoReservaDAO();
        SuspencionDAO suspencionDAO = new SuspencionDAO();
        AmonestacionDAO amonestacionDAO = new AmonestacionDAO();
        MultaDAO multaDAO = new MultaDAO();
        for (DetalleRolPago detalle : detalles) {
            if (detalle.getTipoRubro().getId() == 11) {
                sueldo.setEmpleado(rolPagos.getEmpleado());
                sueldo = sueldoDAO.buscarPorId(detalle.getRubro());
                break;
            }
        }
        horasLaboradas = (int) (rolPagos.getHorasLaboradas()/((sueldo.getValor()/30)/8));
        horasSuplementarias = (int) (rolPagos.getHorasSuplemetarias()/(((sueldo.getValor()/30)/8)*1.5));
        float porcentajeIESS = 0;
        for (DetalleRolPago detalle : detalles) {
            switch (detalle.getTipoRubro().getId()) {
                case 2:
                    amonestacion.setEmpleado(rolPagos.getEmpleado());
                    amonestacion = amonestacionDAO.buscarPorId(detalle.getRubro());
                    break;
                case 3:
                    porcentajeIESS = (float) 0.0945;
                    break;
                case 6:
                    decimoCuarto = Precision.round(rolPagosDAO.obtenerDecicmoCuarto(),2);
                    break;
                case 7:
                    decimoTercero = Precision.round(rolPagosDAO.obtenerDecicmoTercero(), 2);
                    break;
                case 8:
                    empleadoReserva = empleadoReservaDAO.buscar(rolPagos.getEmpleado());
                    fondosReserva = Precision.round(empleadoReserva.getFormaPago() * sueldo.getValor(), 2);
                    break;
                case 9:
                    multa.setEmpleado(rolPagos.getEmpleado());
                    multa = multaDAO.buscarPorId(detalle.getRubro());
                    break;
                case 12:
                    suspencion.setEmpleado(rolPagos.getEmpleado());
                    suspencion = suspencionDAO.buscarPorId(detalle.getRubro());
                    break;
            }
        }
        subTotal = Precision.round((fondosReserva * empleadoReserva.getTipoRubro().getCoeficiente() + rolPagos.getHorasLaboradas()
                + rolPagos.getHorasSuplemetarias() + decimoTercero + decimoCuarto), 2);
        aportesIESS = (float) Precision.round(subTotal * porcentajeIESS, 2);
        total = Precision.round((subTotal - aportesIESS + (amonestacion.getValor() * amonestacion.getTipoRubro().getCoeficiente())
                + (suspencion.getValor() * suspencion.getTipoRubro().getCoeficiente())
                + (multa.getValor() * multa.getTipoRubro().getCoeficiente())), 2);
        if (nuevo){
            mostrarMensajeInformacion("Pago Generado");
        }
        PrimeFaces.current().ajax().update("form:messages", "form:rolPago");
    }

    public RolPagos getRolPagos() {
        return rolPagos;
    }

    public void setRolPagos(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
    }

    public Sueldo getSueldo() {
        return sueldo;
    }

    public void setSueldo(Sueldo sueldo) {
        this.sueldo = sueldo;
    }

    public float getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(float fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public int getHorasLaboradas() {
        return horasLaboradas;
    }

    public void setHorasLaboradas(int horasLaboradas) {
        this.horasLaboradas = horasLaboradas;
    }

    public int getHorasSuplementarias() {
        return horasSuplementarias;
    }

    public void setHorasSuplementarias(int horasSuplementarias) {
        this.horasSuplementarias = horasSuplementarias;
    }

    public float getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(float decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public float getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(float decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public float getAportesIESS() {
        return aportesIESS;
    }

    public void setAportesIESS(float aportesIESS) {
        this.aportesIESS = aportesIESS;
    }

    public Suspencion getSuspencion() {
        return suspencion;
    }

    public void setSuspencion(Suspencion suspencion) {
        this.suspencion = suspencion;
    }

    public Amonestacion getAmonestacion() {
        return amonestacion;
    }

    public void setAmonestacion(Amonestacion amonestacion) {
        this.amonestacion = amonestacion;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }
    
    public String darFormato(Date fecha){
        return fecha != null? new SimpleDateFormat("dd/MM/yyyy").format(fecha) : "";
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void mostrarMensajeInformacion(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
