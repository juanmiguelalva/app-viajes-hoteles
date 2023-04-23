package com.example.app_grupo04;

import java.util.Date;

public class Tiempo {

    public static String  TiempoT(Date date){

        Date hoy = new Date();

        long diferencia =   date.getTime()-hoy.getTime();

        long s = 1000;
        long m = s*60;
        long h = m*60;
        long d = h*24;

        long dias = diferencia/d;
        diferencia = diferencia%d;

        long horas = diferencia/h;
        diferencia = diferencia%h;

        long min = diferencia/m;

        if(dias>=1&&horas>=1){
            return dias + "d " + horas+"h ";
        }
        else if (dias>=1&&horas==0){
            return dias + "d "+ min+"m";
        }
        else if (dias==0&&horas>=1){
            return horas+"h " + min+"m";
        }
        else{
            return min+"m";
        }
    }
}
