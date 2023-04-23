package com.example.app_grupo04.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_grupo04.R;
import com.example.app_grupo04.Tiempo;
import com.example.app_grupo04.modelo.VueloTurista;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterVueloTurista extends ArrayAdapter {
    private Context context;
    private ArrayList<VueloTurista> datos;

    public AdapterVueloTurista(@NonNull Context context, ArrayList<VueloTurista> datos) {
        super(context, R.layout.adapter_vueloturista,datos);
        this.context=context;
        this.datos=datos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_vueloturista,null);

        TextView tv_fecha = view.findViewById(R.id.tv_fechas);
        TextView tv_hora = view.findViewById(R.id.tv_horas);
        TextView tv_destino = view.findViewById(R.id.tv_destinos);
        TextView tv_pasajero = view.findViewById(R.id.tv_pasajeros);

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
            Date time = sdf1.parse(datos.get(position).getHora()+"");
            Date time2 = sdf1.parse(datos.get(position).getDuracion()+"");

            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.add(Calendar.HOUR, time2.getHours());
            cal.add(Calendar.MINUTE, time2.getMinutes());

            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
            String hora = sdf2.format(time);
            tv_hora.setText(hora+" - "+sdf2.format(cal.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
            Date time = sdf1.parse(datos.get(position).getHora());
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
            String hora = sdf2.format(time);
            tv_hora.setText(hora);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_destino.setText(datos.get(position).getDestino()+"");
        tv_pasajero.setText(datos.get(position).getPasajeros()+"");
        tv_fecha.setText(datos.get(position).getFecha());


        return view;
    }
}
