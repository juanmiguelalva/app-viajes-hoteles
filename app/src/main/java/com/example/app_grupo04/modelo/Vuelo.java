package com.example.app_grupo04.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Vuelo implements Serializable {
    private int codigo;
    private String fecha;
    private String hora;
    private String duracion;
    private String Origen;
    private String Destino;
    private int cod_destino;
    private int plazastotales;
    private int plazasturista;
    private double precio;
    private String dato;
    private transient Bitmap imagen;

    public Vuelo() {
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;

        try{
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            this.imagen=BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public int getPlazastotales() {
        return plazastotales;
    }

    public void setPlazastotales(int plazastotales) {
        this.plazastotales = plazastotales;
    }

    public int getPlazasturista() {
        return plazasturista;
    }

    public void setPlazasturista(int plazasturista) {
        this.plazasturista = plazasturista;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCod_destino() {
        return cod_destino;
    }

    public void setCod_destino(int cod_destino) {
        this.cod_destino = cod_destino;
    }
}
