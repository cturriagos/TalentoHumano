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
import Model.Entidad.Empleado;
import Model.Entidad.EmpleadoReserva;
import Model.Entidad.Multa;
import Model.Entidad.RolPagos;
import Model.Entidad.Sueldo;
import Model.Entidad.Suspencion;
import Model.Entidad.TipoRubro;
import java.io.Serializable;
import java.util.Date;
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
 * @author ClasK7
 */
@Named(value = "rolPagoCrearView")
@ViewScoped
public class RolPagoCrearController implements Serializable {

    @Inject
    private RolPagosDAO rolPagosDAO;
    private RolPagos rolPagos;
    private Empleado empleado;
    @Inject
    private DetalleRolPagoDAO detalleRolPagoDAO;

    private Amonestacion amonestacion;
    private Sueldo sueldo;
    private EmpleadoReserva empleadoReserva;
    private Suspencion suspencion;
    private Multa multa;
    private float aportesIESS, decimoTercero, decimoCuarto, fondosReserva, montoHLabboradas, montoHSuplem, subTotal, total;
    private boolean checkdDecimoTercero, checkdDecimoCuarto, checkedMulta, checkedSuspencion, checkedAmonestacion;
    private int horasLaboradas, horasSuplementarias;
    private Date fechaPago;

    public RolPagoCrearController() {
        rolPagos = new RolPagos();
        sueldo = new Sueldo();
        empleadoReserva = new EmpleadoReserva();
        suspencion = new Suspencion();
        amonestacion = new Amonestacion();
        multa = new Multa();
    }

    @PostConstruct
    public void constructorRolPago() {
        subTotal = 0;
        total = 0;
        aportesIESS = 0;
        decimoTercero = 0;
        decimoCuarto = 0;
        checkdDecimoTercero = false;
        checkdDecimoCuarto = false;
        checkedMulta = false;
        checkedSuspencion = false;
        checkedAmonestacion = false;
    }

    public void postLoad(Empleado empleado) {
        this.empleado = empleado;
        rolPagos.setEmpleado(this.empleado);
        SueldoDAO sueldoDAO = new SueldoDAO();
        EmpleadoReservaDAO empleadoReservaDAO = new EmpleadoReservaDAO();
        SuspencionDAO suspencionDAO = new SuspencionDAO();
        AmonestacionDAO amonestacionDAO = new AmonestacionDAO();
        MultaDAO multaDAO = new MultaDAO();
        sueldo = sueldoDAO.Actual(empleado);
        empleadoReserva = empleadoReservaDAO.buscar(empleado);
        fondosReserva = Precision.round(empleadoReserva.getFormaPago() * sueldo.getValor(), 2);
        suspencion = suspencionDAO.buscar(empleado);
        amonestacion = amonestacionDAO.buscar(empleado);
        multa = multaDAO.buacar(empleado);
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
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

    public EmpleadoReserva getEmpleadoReserva() {
        return empleadoReserva;
    }

    public void setEmpleadoReserva(EmpleadoReserva empleadoReserva) {
        this.empleadoReserva = empleadoReserva;
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

    public RolPagos getRolPagos() {
        return rolPagos;
    }

    public void setRolPagos(RolPagos rolPagos) {
        this.rolPagos = rolPagos;
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

    public boolean isCheckdDecimoTercero() {
        return checkdDecimoTercero;
    }

    public void setCheckdDecimoTercero(boolean checkdDecimoTercero) {
        this.checkdDecimoTercero = checkdDecimoTercero;
        calcularTotal();
    }

    public boolean isCheckdDecimoCuarto() {
        return checkdDecimoCuarto;
    }

    public void setCheckdDecimoCuarto(boolean checkdDecimoCuarto) {
        this.checkdDecimoCuarto = checkdDecimoCuarto;
        calcularTotal();
    }

    public boolean isCheckedMulta() {
        return checkedMulta;
    }

    public void setCheckedMulta(boolean checkedMulta) {
        this.checkedMulta = checkedMulta;
        calcularTotal();
    }

    public boolean isCheckedSuspencion() {
        return checkedSuspencion;
    }

    public void setCheckedSuspencion(boolean checkedSuspencion) {
        this.checkedSuspencion = checkedSuspencion;
        calcularTotal();
    }

    public boolean isCheckedAmonestacion() {
        return checkedAmonestacion;
    }

    public void setCheckedAmonestacion(boolean checkedAmonestacion) {
        this.checkedAmonestacion = checkedAmonestacion;
        calcularTotal();
    }

    public float getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(float fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public float getMontoHLabboradas() {
        return montoHLabboradas;
    }

    public void setMontoHLabboradas(float montoHLabboradas) {
        this.montoHLabboradas = montoHLabboradas;
    }

    public float getMontoHSuplem() {
        return montoHSuplem;
    }

    public void setMontoHSuplem(float montoHSuplem) {
        this.montoHSuplem = montoHSuplem;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public void obtenerDatos() {
        rolPagos.setFechaGenerado(fechaPago);
        rolPagosDAO.setRolPagos(rolPagos);
        horasLaboradas = rolPagosDAO.obtenerHorasLaboradas();
        horasSuplementarias = rolPagosDAO.obtenerHorasSuplementarias();
        montoHLabboradas = Precision.round(horasLaboradas * ((sueldo.getValor() / 30) / 8), 2);
        montoHSuplem = (float) Precision.round((horasSuplementarias * ((sueldo.getValor() / 30) / 8) * 1.5), 2);
        decimoTercero = Precision.round(rolPagosDAO.obtenerDecicmoTercero(), 2);
        decimoCuarto = Precision.round(rolPagosDAO.obtenerDecicmoCuarto(), 2);
        calcularTotal();
    }

    public void calcularTotal() {
        subTotal = Precision.round((fondosReserva * empleadoReserva.getTipoRubro().getCoeficiente() + montoHLabboradas + montoHSuplem + (checkdDecimoTercero ? decimoTercero : 0)
                + (checkdDecimoCuarto ? decimoCuarto : 0)), 2);
        aportesIESS = (float) Precision.round(subTotal * 0.0945, 2);
        total = Precision.round((subTotal - aportesIESS + (checkedAmonestacion ? (amonestacion.getValor() * amonestacion.getTipoRubro().getCoeficiente()) : 0)
                + (checkedSuspencion ? (suspencion.getValor() * suspencion.getTipoRubro().getCoeficiente()) : 0)
                + (checkedMulta ? (multa.getValor() * multa.getTipoRubro().getCoeficiente()) : 0)), 2);
        PrimeFaces.current().ajax().update("form:messages", "form:DATOS");
    }

    public String guardar() {
        String mensaje = "";
        boolean result = false;
        rolPagos.setEstado("GENERADO");
        rolPagos.setCodigo(java.util.UUID.randomUUID().toString());
        rolPagos.setValor(total);
        rolPagos.setHorasLaboradas(montoHLabboradas);
        rolPagos.setHorasSuplemetarias(montoHSuplem);
        rolPagosDAO.setRolPagos(rolPagos);
        if (rolPagosDAO.insertar() > 0) {
            rolPagos.setId(rolPagosDAO.getRolPagos().getId());
            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, sueldo.getTipoRubro(), sueldo.getId()));
            if (detalleRolPagoDAO.insertar() > 0) {
                detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, empleadoReserva.getTipoRubro(), empleadoReserva.getEmpleado().getId()));
                if (detalleRolPagoDAO.insertar() > 0) {
                    detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, new TipoRubro(3, 1, ""), 1));
                    if (detalleRolPagoDAO.insertar() > 0) {
                        if (checkdDecimoTercero) {
                            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, new TipoRubro(7, 1, ""), 1));
                            result = true;
                            if (detalleRolPagoDAO.insertar() < 1) {
                                mensaje = "El detalle no se pudo asignar";
                                result = false;
                            }
                        }
                        if (checkdDecimoCuarto && result) {
                            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, new TipoRubro(6, 1, ""), 1));
                            result = true;
                            if (detalleRolPagoDAO.insertar() < 1) {
                                mensaje = "El detalle no se pudo asignar";
                                result = false;
                            }
                        }
                        if (checkedAmonestacion && result) {
                            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, amonestacion.getTipoRubro(), amonestacion.getId()));
                            result = true;
                            if (detalleRolPagoDAO.insertar() < 1) {
                                mensaje = "El detalle no se pudo asignar";
                                result = false;
                            }
                        }
                        if (checkedMulta && result) {
                            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, multa.getTipoRubro(), multa.getId()));
                            result = true;
                            if (detalleRolPagoDAO.insertar() < 1) {
                                mensaje = "El detalle no se pudo asignar";
                                result = false;
                            }
                        }
                        if (checkedSuspencion && result) {
                            detalleRolPagoDAO.setDetalleRolPago(new DetalleRolPago(rolPagos, suspencion.getTipoRubro(), suspencion.getId()));
                            result = true;
                            if (detalleRolPagoDAO.insertar() < 1) {
                                mensaje = "El detalle no se pudo asignar";
                                result = false;
                            }
                        }
                    } else {
                        mensaje = "La aportación al IESS no se pudo asignar al detalle";
                    }
                } else {
                    mensaje = "El fondo de reserva no se pudo asignar al detalle";
                }
            } else {
                mensaje = "El sueldo no se pudo guardar al detalle";
            }
        } else {
            mensaje = "El rol de pagos no se pudo guardar";
        }
        if (result) {
            mostrarMensajeInformacion("Datos guardados correctamente");
        } else {
            mostrarMensajeError(mensaje);
        }
        PrimeFaces.current().ajax().update("form:messages", "form:DATOS");
        return result ? "RolDePago" : "";
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
