package ec.edu.uce.ex2h_g04.modelo;

import java.io.Serializable;


public class Usuario implements Serializable {

    int id;
    String usuario;
    String clave;

    public Usuario() {
        super();
    }

    public Usuario(int id, String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return "id: "+id +"\nusuario: "+usuario+"\nclave: "+clave;
    }
}
