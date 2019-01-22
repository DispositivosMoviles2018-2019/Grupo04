package ec.edu.uce.ex2h_g04.modelo;

import java.io.Serializable;
import java.util.Date;


public class Reserva implements Serializable {
    private Integer numeroReserva;
    private String email;
    private String celular;
    private Date fecReserva;
    private Date fecEntrega;
    private Double valor;

    private int user;       //id de usuario que hizo la reserva
    private String placa;   //placa del carro reservado

    public Reserva() {
        super();
    }

    public Reserva(Integer numeroReserva, String email, String celular, Date fecReserva, Date fecEntrega, Double valor, int user, String placa) {
        this.numeroReserva = numeroReserva;
        this.email = email;
        this.celular = celular;
        this.fecReserva = fecReserva;
        this.fecEntrega = fecEntrega;
        this.valor = valor;
        this.user = user;
        this.placa = placa;
    }

    public Integer getNumeroReserva() {
        return numeroReserva;
    }

    public void setNumeroReserva(Integer numeroReserva) {
        this.numeroReserva = numeroReserva;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getFecReserva() {
        return fecReserva;
    }

    public void setFecReserva(Date fecReserva) {
        this.fecReserva = fecReserva;
    }

    public Date getFecEntrega() {
        return fecEntrega;
    }

    public void setFecEntrega(Date fecEntrega) {
        this.fecEntrega = fecEntrega;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }



    @Override
    public String toString() {
        return "Num. Reserva: "+numeroReserva+"\nemail: "+email+"\ncelular: "+celular+"\nFecha reserva: "+fecReserva+"\nFecha entrega: "+fecEntrega+"\nvalor: "+valor+"\nUsuario: "+user+"\nPlaca de vehiculo: "+placa;
    }
}
