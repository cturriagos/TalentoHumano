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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.math3.util.Precision;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ClasK7
 */
@Named(value = "rolPagoView")
@ViewScoped
public class RolPagoController implements Serializable {

    @Inject
    private DetalleRolPagoDAO detalleRolPagoDAO;
    private RolPagos rolPagos;
    private List<DetalleRolPago> detalles;

    private Amonestacion amonestacion;
    private Sueldo sueldo;
    private EmpleadoReserva empleadoReserva;
    private Suspencion suspencion;
    private Multa multa;
    private float aportesIESS, decimoTercero, decimoCuarto, fondosReserva, subTotal, total;
    private int horasLaboradas, horasSuplementarias;

    public RolPagoController() {
        rolPagos = new RolPagos();
        sueldo = new Sueldo();
        empleadoReserva = new EmpleadoReserva();
        suspencion = new Suspencion();
        amonestacion = new Amonestacion();
        multa = new Multa();
        detalles = new ArrayList<>();
    }

    @PostConstruct
    public void constructorRolPago() {
        aportesIESS = 0;
        decimoTercero = 0;
        decimoCuarto = 0;
        fondosReserva = 0;
        subTotal = 0;
        total = 0;
        
    }

    public void postLoad(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
        RolPagosDAO rolPagosDAO = new RolPagosDAO(this.rolPagos);
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
                    decimoCuarto = rolPagosDAO.obtenerDecicmoCuarto();
                    break;
                case 7:
                    decimoTercero = rolPagosDAO.obtenerDecicmoTercero();
                    break;
                case 8:
                    empleadoReserva = empleadoReservaDAO.buscar(rolPagos.getEmpleado());
                    fondosReserva = empleadoReserva.getFormaPago() * sueldo.getValor();
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
        subTotal = fondosReserva * empleadoReserva.getTipoRubro().getCoeficiente() + rolPagos.getHorasLaboradas()
                + rolPagos.getHorasSuplemetarias() + decimoTercero + decimoCuarto;
        aportesIESS = (float) Precision.round(subTotal * porcentajeIESS, 2);
        total = subTotal - aportesIESS + (amonestacion.getValor() * amonestacion.getTipoRubro().getCoeficiente())
                + (suspencion.getValor() * suspencion.getTipoRubro().getCoeficiente())
                + (multa.getValor() * multa.getTipoRubro().getCoeficiente());
        PrimeFaces.current().ajax().update("form:messages", "form:rolPago");
    }

    public RolPagos getRolPagos() {
        return rolPagos;
    }

    public void setRolPagos(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
    }

    public float getAportesIESS() {
        return aportesIESS;
    }

    public void setAportesIESS(float aportesIESS) {
        this.aportesIESS = aportesIESS;
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

    public Amonestacion getAmonestacion() {
        return amonestacion;
    }

    public void setAmonestacion(Amonestacion amonestacion) {
        this.amonestacion = amonestacion;
    }

    public Sueldo getSueldo() {
        return sueldo;
    }

    public void setSueldo(Sueldo sueldo) {
        this.sueldo = sueldo;
    }

    public Suspencion getSuspencion() {
        return suspencion;
    }

    public void setSuspencion(Suspencion suspencion) {
        this.suspencion = suspencion;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
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
}
