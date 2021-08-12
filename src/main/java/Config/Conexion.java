package Config;

import com.sun.istack.internal.Nullable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;

public class Conexion {

    String consulta = "nada";
    private Connection conex;
    private java.sql.Statement st;
    private ResultSet lector;
    private boolean estado;
    private String mensaje;
    private boolean transaccionIniciada;
    private FacesMessage.Severity tipoMensaje;

    /*
"INFO", WARN", "ERROR", "FATAL";
     */
 /*private String url = "jdbc:postgresql://localhost:5432/visorpdf";
    private String usuario = "postgres";
    private String clave = "sinClave";
        private String classForName = "org.postgresql.Driver";
     */
    //private String url = "jdbc:sqlserver://localhost\\WIN-J31JOLHOPG2\\SQLEXPRESS01:3128;databaseName=VisorPdf";
    private String url = "jdbc:postgresql://localhost:5432/BDPRO";
    private String usuario = "Proyecto_TH";
    private String clave = "123456";
    private String classForName = "org.postgresql.Driver";

    public Conexion() {
        estado = true;
    }

    public Conexion(String user, String pass, String url) {
        usuario = user;
        clave = pass;
        this.url = url;
        estado = true;
    }

    public boolean abrirConexion() {
        try {
            if (conex == null || !(conex.isClosed())) {
                Class.forName(classForName);
                conex = DriverManager.getConnection(url, usuario, clave);
                st = conex.createStatement();
                estado = true;
            }
        } catch (ClassNotFoundException | SQLException exSQL) {
            mensaje = exSQL.getMessage();
            System.out.println(mensaje);
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            return false;
        }
        return true;
    }

    public void cerrarConexion() {
        try {
            if (conex != null && !conex.isClosed()) {
                conex.close();
                conex = null;
            }
            if (st != null && !st.isClosed()) {
                st.close();
                st = null;
            }
            if (lector != null && !lector.isClosed()) {
                lector.close();
                lector = null;
            }
        } catch (SQLException e) {
            mensaje = e.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println("ERROR: " + mensaje);
        }
    }

    public int ejecutar(String sql) {
        int retorno = -1;
        try {
            if (abrirConexion()) {
                retorno = st.executeUpdate(sql);
                mensaje = "Se guardó correctamente : ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
        } finally {
            cerrarConexion();
        }
        return retorno;
    }

    public ResultSet ejecutarConsulta(String sql) {
        try {
            if (abrirConexion()) {
                lector = st.executeQuery(sql);
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
            cerrarConexion();
        }
        return lector;

    }
    
    public boolean iniciarTransaccion() throws SQLException, ClassNotFoundException{
        if (conex == null || !(conex.isClosed())) { 
            Class.forName(classForName);
            conex = DriverManager.getConnection(url, usuario, clave);
            conex.setAutoCommit(false); 
            st = conex.createStatement();
            estado = true;
            transaccionIniciada = true;
            return estado;
        }else{
            return false;
        }
    }
    
    public void ejecutarInsertarToTrnasaccion(String tabla, String campos, String valores) throws SQLException {
        if(transaccionIniciada){
            st.executeUpdate("INSERT INTO public." + tabla + "(" + campos + ")" + "VALUES(" + valores +")");
        }else{
            throw new SQLException("Debe ejecutar primero la funcion iniciarTransaccion()");
        }
    }
    
    public void finalizarTransaccion(boolean conmit) throws SQLException{
        if(conmit){
            conex.commit();
        }else{
            conex.rollback();
        }
        cerrarConexion();
    }

    public ResultSet selecionar(String tabla, String campos, @Nullable String restrinciones, @Nullable String ordenar) {
        String sql = "SELECT " + campos +" FROM public." + tabla;
        if (restrinciones != null){
            sql = sql + " WHERE " + restrinciones ;
        }
        if (ordenar != null){
            sql = sql + " ORDER BY " + ordenar ;
        }
        try {
            if (abrirConexion()) {
                lector = st.executeQuery(sql);
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
            cerrarConexion();
        }
        return lector;

    }

    public int insertar(String tabla, String campos, String valores, boolean returnId) {
        int retorno = -1;
        String sql = "INSERT INTO public." + tabla + " (" + campos + ")" + " VALUES(" + valores +")";
        try {
            if (abrirConexion()) {
                if(returnId){
                    retorno = st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                    ResultSet resultSet =  st.getGeneratedKeys();
                    if(resultSet.next()){
                        retorno = resultSet.getInt(1);
                    }
                }else{
                    retorno = st.executeUpdate(sql);
                }
                mensaje = "Se insertó correctamente : ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
        } finally {
            cerrarConexion();
        }
        return retorno;
    }

   /* public int insertar(String tabla, String[] campos, String[] valores) {
        String fields = "", values = "";
        int retorno = -1;
        for (int i = 0; i < campos.length; i++) {
            fields += campos[i] + ", ";
            values += "'" + valores[i] + "', ";
        }
        fields = fields.substring(0, fields.length() - 2);
        values = values.substring(0, values.length() - 2);

        String sentencia = "INSERT INTO " + tabla + " (" + fields + ")\n"
                + "VALUES(" + values + ")";
        System.out.println(sentencia);
        try {
            if (abrirConexion()) {

                retorno = st.executeUpdate(sentencia);
                cerrarConexion();
                mensaje = "Se insertó correctamente : ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(sentencia);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);

        } finally {
            cerrarConexion();
        }
        return retorno;
    }

    public int modificar(String tabla, String[] campos, String[] valores, String condicion) {
        String cadena = "";
        int retorno = -1;
        for (int i = 0; i < campos.length; i++) {
            cadena += campos[i] + " = '" + valores[i] + "', ";
        }
        cadena = cadena.substring(0, cadena.length() - 2);

        String sentencia = "UPDATE " + tabla + " \nSET " + cadena + "\nWHERE " + condicion;
        System.out.println(sentencia);
        try {
            if (abrirConexion()) {

                retorno = st.executeUpdate(sentencia);
                cerrarConexion();
                mensaje = "Se modificó correctamente: ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(cadena);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);

        } finally {
            cerrarConexion();
        }
        return retorno;
    }*/

    public int modificar(String tabla, String camposModificados, String restrinciones) {
        int retorno = -1;
        String sql = "UPDATE " + tabla + " SET " + camposModificados + " WHERE " + restrinciones;
        try {
            if (abrirConexion()) {
                retorno = st.executeUpdate(sql);
                cerrarConexion();
                mensaje = "Se modifico correctamente : ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
        }
        return retorno;
    }

    public int eliminar(String sql) {
        int retorno = -1;
        try {
            if (abrirConexion()) {
                retorno = st.executeUpdate(sql);
                cerrarConexion();
                mensaje = "Se insertó correctamente : ";
                tipoMensaje = FacesMessage.SEVERITY_INFO;
            }
        } catch (SQLException exc) {
            System.out.println(sql);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);
        } finally {
            cerrarConexion();
        }
        return retorno;
    }

    public boolean existeRegistro(String consulta) {
        boolean valor = false;
        try {
            if (abrirConexion()) {
                st = conex.createStatement();
                lector = st.executeQuery(consulta);
                valor = lector.next();
            }
        } catch (SQLException exc) {
            System.out.println(consulta);
            mensaje = exc.getMessage();
            tipoMensaje = FacesMessage.SEVERITY_FATAL;
            System.out.println(mensaje);

        } finally {
            cerrarConexion();
        }
        return valor;
    }

    public boolean inyeccionSQL(String user, String pass) {
        return user.contains("'") || pass.contains("'") || user.contains(" or ") || pass.contains(" or ") || user.contains(" and ") || pass.contains(" and ");
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isEstado() {
        return estado;
    }

    public Connection getConexion() {
        return conex;
    }

    public ResultSet getLector() {
        return lector;
    }

    public FacesMessage.Severity getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(FacesMessage.Severity tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

}
