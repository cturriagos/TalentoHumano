/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Bean.PersonaBean;
import Model.DAO.AmonestacionDAO;
import Model.DAO.DetalleRolPagoDAO;
import Model.DAO.EmpleadoDAO;
import Model.DAO.EmpleadoReservaDAO;
import Model.DAO.MultaDAO;
import Model.DAO.RolPagosDAO;
import Model.DAO.SueldoDAO;
import Model.DAO.SuspencionDAO;
import Model.DAO.TipoRubroDAO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
    private List<DetalleRolPago> detalles;
    @Inject
    private TipoRubroDAO tipoRubroDAO;
    private List<TipoRubro> tipos;

    private Amonestacion amonestacion;
    private Sueldo sueldo;
    private EmpleadoReserva empleadoReserva;
    private Suspencion suspencion;
    private Multa multa;
    private float aportesIESS, decimoTercero, decimoCuarto, fondosReserva, montoHLabboradas, montoHSuplem, total;
    private boolean checkdDecimoTercero, checkdDecimoCuarto;
    private int horasLaboradas, horasSuplementarias;
    private Date mes;
        
    public RolPagoCrearController() {
        rolPagos = new RolPagos();
        sueldo = new Sueldo();
        empleadoReserva = new EmpleadoReserva();
        suspencion = new Suspencion();
        amonestacion = new Amonestacion();
        multa = new Multa();
        detalles = new ArrayList<>();
        tipos = new ArrayList<>();
    }

    @PostConstruct
    public void constructorRolPago() {
        total =0;
    }
    
    public void postLoad(Empleado empleado){
        this.empleado = empleado;
        SueldoDAO sueldoDAO = new SueldoDAO();
        EmpleadoReservaDAO empleadoReservaDAO = new EmpleadoReservaDAO();
        SuspencionDAO suspencionDAO = new SuspencionDAO();
        AmonestacionDAO amonestacionDAO = new AmonestacionDAO();
        MultaDAO multaDAO = new MultaDAO();
        sueldo = sueldoDAO.Actual(empleado);
        empleadoReserva = empleadoReservaDAO.buscar(empleado);
        fondosReserva = empleadoReserva.getFormaPago() * sueldo.getValor();
        aportesIESS = (float) (sueldo.getValor() * 0.0945);
        suspencion = suspencionDAO.buscar(empleado);
        amonestacion = amonestacionDAO.buscar(empleado);
        multa = multaDAO.buacar(empleado);
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

    public Date getMes() {
        return mes;
    }

    public void setMes(Date mes) {
        this.mes = mes;
    }

    public boolean isCheckdDecimoCuarto() {
        return checkdDecimoCuarto;
    }

    public boolean isCheckdDecimoTercero() {
        return checkdDecimoTercero;
    }

    public void setCheckdDecimoTercero(boolean checkdDecimoTercero) {
        this.checkdDecimoTercero = checkdDecimoTercero;
    }

    public void setCheckdDecimoCuarto(boolean checkdDecimoCuarto) {
        this.checkdDecimoCuarto = checkdDecimoCuarto;
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
    
    public void checkedChangeTercero(){
        /*if(checkdDecimoTercero){
            decimoTercero = rolPagosDAO.obtenerDecicmoTercero();
        }else{
            decimoTercero = 0;
        }*/
    }
    
    public void checkedChangeCuarto(){
       /* if(checkdDecimoCuarto){
            decimoCuarto = rolPagosDAO.obtenerDecicmoCuarto();
        }else{
            decimoCuarto = 0;
        }*/
    }
    
    public void obtenerDatos(){
        rolPagosDAO.getRolPagos().setEmpleado(empleado);
        rolPagosDAO.getRolPagos().setFechaGenerado(mes);
        horasLaboradas = rolPagosDAO.obtenerHorasLaboradas();
        horasSuplementarias = rolPagosDAO.obtenerHorasSuplementarias();
        montoHLabboradas = horasLaboradas*((sueldo.getValor()/30)/8);
        montoHSuplem = (float) (horasSuplementarias*((sueldo.getValor()/30)/8)*1.5);
        decimoTercero = rolPagosDAO.obtenerDecicmoTercero();
        decimoCuarto = rolPagosDAO.obtenerDecicmoCuarto();
        calcularTotal();
    }
    
    public void calcularTotal(){
        total = fondosReserva + montoHLabboradas + montoHSuplem + (checkdDecimoTercero? decimoTercero : 0) + (checkdDecimoCuarto? decimoCuarto : 0);
        //total += 
    }
   /* private void resumenReserva(){
            if (empleadoReserva.getFormaPago() != 0) {
                resumeReserva = "$ " + (empleadoReserva.getFormaPago() * sueldo.getValor()) + " (" + (empleadoReserva.getFormaPago() == 1 ? "ANUAL" : "MENSUAL") + ")";
            } else {
                resumeReserva = "S/D";
            }
    }*/
}