package uce.edu.ec.gcoello_ex_1h.modelo;

import java.io.Serializable;
import java.util.Date;

public class Vehiculo implements Serializable {

    private String placa;
    private String marca;
    private Date fecFab;
    private Double costo;
    private Boolean matriculado;
    private String color;

    public Vehiculo(){
        super();
    }

    public Vehiculo(String placa, String marca, Date fecFab, Double costo, Boolean matriculado, String color) {
        this.placa = placa;
        this.marca = marca;
        this.fecFab = fecFab;
        this.costo = costo;
        this.matriculado = matriculado;
        this.color = color;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getFecFab() {
        return fecFab;
    }

    public void setFecFab(Date fecFab) {
        this.fecFab = fecFab;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Boolean getMatriculado() {
        return matriculado;
    }

    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
