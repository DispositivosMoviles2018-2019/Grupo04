package ec.edu.uce.ex2h_g04.modelo;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

/**

 */

public class Vehiculo implements Serializable {

    private int id;

    private String placa;
    private String marca;
    private Date fecFab;
    private Double costo;
    private Boolean matriculado;
    private String color;

    private Drawable foto;
    private Boolean estado;



    public Vehiculo(){
        super();
    }

    public Vehiculo(int id, String placa, String marca, Date fecFab, Double costo, Boolean matriculado, String color, Boolean estado, Drawable foto){
    super();
    this.id=id;
    this.placa=placa;
    this.marca=marca;
    this.fecFab=fecFab;
    this.costo=costo;
    this.matriculado=matriculado;
    this.color=color;
    this.estado=estado;
    this.foto=foto;
   }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getFoto() {
        return foto;
    }

    public void setFoto(Drawable foto) {
        this.foto = foto;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    @Override
    public String toString() {
        return "id:"+id+"\nplaca:"+placa+"\nmarca:"+marca+"\nfecha:"+fecFab+"\ncosto:"+costo+"\nmatriculado:"+matriculado+"\ncolor:"+color+"\nestado:"+estado;
    }
}
