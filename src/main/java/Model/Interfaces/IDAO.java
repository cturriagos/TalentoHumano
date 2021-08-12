/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Interfaces;

import Config.Conexion;
import java.util.List;

/**
 *
 * @author rturr
 */
public interface IDAO<T> {
    public Conexion obtenerConexion();
    public int insertar();
    public int insertar(T entity);
    public int actualizar();
    public int actualizar(T entity);
    public T buscarPorId(Object id);
    public List<T> Listar();
}
