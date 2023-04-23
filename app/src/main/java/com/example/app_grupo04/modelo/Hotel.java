package com.example.app_grupo04.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Hotel implements Serializable {
    public int CodigoHotel;
    public String NombreHotel;
    public String DireccionHotel;
    public String CiudadHotel;
    public String TelefonoHotel;
    public int PlazasHotel;
    public Bitmap Imagen;
    public int Destino_CodigoDestino;
    public Double LongitudHotel;
    public Double LatitudHotel;
    public String NombreDestino;
    public String Dato;

    public Hotel() {
    }



    public String getDato() {
        return Dato;
    }

    public void setDato(String dato) {
        Dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato,Base64.DEFAULT);
            Imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCodigoHotel() {
        return CodigoHotel;
    }

    public void setCodigoHotel(int codigoHotel) {
        CodigoHotel = codigoHotel;
    }

    public String getNombreHotel() {
        return NombreHotel;
    }

    public void setNombreHotel(String nombreHotel) {
        NombreHotel = nombreHotel;
    }

    public String getDireccionHotel() {
        return DireccionHotel;
    }

    public void setDireccionHotel(String direccionHotel) {
        DireccionHotel = direccionHotel;
    }

    public String getCiudadHotel() {
        return CiudadHotel;
    }

    public void setCiudadHotel(String ciudadHotel) {
        CiudadHotel = ciudadHotel;
    }

    public String getTelefonoHotel() {
        return TelefonoHotel;
    }

    public void setTelefonoHotel(String telefonoHotel) {
        TelefonoHotel = telefonoHotel;
    }

    public int getPlazasHotel() {
        return PlazasHotel;
    }

    public void setPlazasHotel(int plazasHotel) {
        PlazasHotel = plazasHotel;
    }

    public Bitmap getImagen() {
        return Imagen;
    }

    public void setImagen(Bitmap imagen) {
        Imagen = imagen;
    }

    public int getDestino_CodigoDestino() {
        return Destino_CodigoDestino;
    }

    public void setDestino_CodigoDestino(int destino_CodigoDestino) {
        Destino_CodigoDestino = destino_CodigoDestino;
    }

    public Double getLongitudHotel() {
        return LongitudHotel;
    }

    public void setLongitudHotel(Double longitudHotel) {
        LongitudHotel = longitudHotel;
    }

    public Double getLatitudHotel() {
        return LatitudHotel;
    }

    public void setLatitudHotel(Double latitudHotel) {
        LatitudHotel = latitudHotel;
    }

    public String getNombreDestino() {
        return NombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        NombreDestino = nombreDestino;
    }
}
