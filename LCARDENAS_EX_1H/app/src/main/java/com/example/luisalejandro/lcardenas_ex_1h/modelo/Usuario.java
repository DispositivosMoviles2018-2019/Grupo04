package com.example.luisalejandro.lcardenas_ex_1h.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    String usuario;
    String clave;

    public Usuario() {
        super();
    }

    public Usuario(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
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
        return "usuario: "+usuario+"\nclave: "+clave;
    }
}
