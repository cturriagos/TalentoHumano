/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Bean;

import Model.Entidad.Empleado;
import Model.Entidad.RolPagos;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author cturriagos
 */
@Named
@SessionScoped
public class PersonaBean implements Serializable{
    private int idPersona, idEmpleado, idRolDePago;
    private Empleado empleado;
    private RolPagos rolPago;
    private boolean nuevo;
    
    public PersonaBean() {
        idPersona = 0;
        idEmpleado = 0;
        idRolDePago = 0;
        nuevo = false;
        empleado = new Empleado();
        rolPago = new RolPagos();
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getIdRolDePago() {
        return idRolDePago;
    }

    public void setIdRolDePago(int idRolDePago) {
        this.idRolDePago = idRolDePago;
    }

    public RolPagos getRolPago() {
        return rolPago;
    }

    public void setRolPago(RolPagos rolPago) {
        this.rolPago = rolPago;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }
}