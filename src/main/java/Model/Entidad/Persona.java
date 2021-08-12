/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Entidad;

import java.util.Date;

/**
 *
 * @author rturr
 */
public class Persona {
    
    private int id_persona;
    private String nombre;
    private String apellido;
    private int cedula;
    private String Cargo;
    private String departamento;
    private double sueldoInicial;
    //private Date activoDesde;

    public Persona() {
    }

    public Persona(int id_persona, String nombre, String apellido, int cedula, String Cargo, String departamento, Double sueldoInicial) {
        super();
        this.id_persona = id_persona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.Cargo = Cargo;
        this.departamento = departamento;
        this.sueldoInicial = sueldoInicial;
        //this.activoDesde = activoDesde;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getCargo() {
        return Cargo;
    }

    public void setCargo(String Cargo) {
        this.Cargo = Cargo;
    }

    public double getSueldoInicial() {
        return sueldoInicial;
    }

    public void setSueldoInicial(int sueldoInicial) {
        this.sueldoInicial = sueldoInicial;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    
}
