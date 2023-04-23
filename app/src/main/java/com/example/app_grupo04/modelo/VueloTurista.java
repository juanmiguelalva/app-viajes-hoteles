package com.example.app_grupo04.modelo;

import java.io.Serializable;

public class VueloTurista implements Serializable {
    int codigo;
    String clase;
    double monto;
    int pasajeros;
    int numerovuelo;
    int cod_viajecontratados;
    private String fecha;
    private String hora;
    private String Destino;
    private String duracion;

    public VueloTurista() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(int pasajeros) {
        this.pasajeros = pasajeros;
    }

    public int getNumerovuelo() {
        return numerovuelo;
    }

    public void setNumerovuelo(int numerovuelo) {
        this.numerovuelo = numerovuelo;
    }

    public int getCod_viajecontratados() {
        return cod_viajecontratados;
    }

    public void setCod_viajecontratados(int cod_viajecontratados) {
        this.cod_viajecontratados = cod_viajecontratados;
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

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
