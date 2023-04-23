package com.example.app_grupo04.modelo;

import java.io.Serializable;

public class Estancia implements Serializable {
    int codigo;
    String pension;
    String fentrada;
    String fsalida;
    int codigohotel;
    double precio;

    public Estancia() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getFentrada() {
        return fentrada;
    }

    public void setFentrada(String fentrada) {
        this.fentrada = fentrada;
    }

    public String getFsalida() {
        return fsalida;
    }

    public void setFsalida(String fsalida) {
        this.fsalida = fsalida;
    }

    public int getCodigohotel() {
        return codigohotel;
    }

    public void setCodigohotel(int codigohotel) {
        this.codigohotel = codigohotel;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
