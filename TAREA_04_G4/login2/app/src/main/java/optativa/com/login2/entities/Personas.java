package optativa.com.login2.entities;

import java.io.Serializable;
import java.util.Date;

public class Personas implements Serializable {

    private String usuario;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private int telefono;
    private boolean genero;
    private Date fecha;
    private String materias;
    private boolean beca;

    public Personas() {
        super();
    }

    public Personas(String usuario, String password, String nombre, String apellido, String email, int telefono, boolean genero, Date fecha, String materias, boolean beca) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.genero = genero;
        this.fecha = fecha;
        this.materias = materias;
        this.beca = beca;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMaterias() {
        return materias;
    }

    public void setMaterias(String materias) {
        this.materias = materias;
    }

    public boolean isBeca() {
        return beca;
    }

    public void setBeca(boolean beca) {
        this.beca = beca;
    }
}
