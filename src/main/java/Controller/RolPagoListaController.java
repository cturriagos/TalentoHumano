/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.DAO.EmpleadoDAO;
import Model.DAO.RolPagosDAO;
import Model.Entidad.Empleado;
import Model.Entidad.RolPagos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ClasK7
 */
@Named(value = "rolPagoListaView")
@ViewScoped
public class RolPagoListaController implements Serializable {
    
    private Empleado empleado;
    private List<Empleado> empleados;
    private List<RolPagos> pagos;
    
    @Inject
    private RolPagosDAO rolPagosDAO;
    @Inject
    private EmpleadoDAO empleadoDAO;

    public RolPagoListaController() {
        empleado = new Empleado();
        empleados = new ArrayList<>();
        pagos = new ArrayList<>();
    }

    @PostConstruct
    public void constructorRolPagoLista() {
        empleados = empleadoDAO.Listar();
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<RolPagos> getPagos() {
        return pagos;
    }

    public void setPagos(List<RolPagos> pagos) {
        this.pagos = pagos;
    }
    
    public void nuevoEmpleado(){
        empleado = new Empleado();
    }

    public void empleadoSeleccionado() {
        empleado = empleadoDAO.buscarPorId(empleado.getId());
        pagos = rolPagosDAO.buscar(empleado);
    }
}
