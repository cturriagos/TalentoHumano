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
    private int idPersona, idEmpleado, idNatural;
    private Empleado empleado;
    private RolPagos rolPago;
    
    public PersonaBean() {
        idPersona = 0;
        idEmpleado = 0;
        idNatural = 0;
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

    public int getIdNatural() {
        return idNatural;
    }

    public void setIdNatural(int idNatural) {
        this.idNatural = idNatural;
    }

    public RolPagos getRolPago() {
        return rolPago;
    }

    public void setRolPago(RolPagos rolPago) {
        this.rolPago = rolPago;
    }
}