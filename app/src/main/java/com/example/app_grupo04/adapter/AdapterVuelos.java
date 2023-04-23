package com.example.app_grupo04.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_grupo04.R;
import com.example.app_grupo04.Tiempo;
import com.example.app_grupo04.modelo.Vuelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterVuelos extends ArrayAdapter {

    private Context context;
    private ArrayList<Vuelo> datos;

    public AdapterVuelos(@NonNull Context context, ArrayList<Vuelo> datos) {
        super(context, R.layout.adapter_vuelo,datos);
        this.context=context;
        this.datos=datos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_vuelo,null);

        TextView tv_hora = view.findViewById(R.id.tv_hora);
        TextView tv_destino = view.findViewById(R.id.tv_destino);
        TextView tv_precio = view.findViewById(R.id.tv_precio);
        TextView tv_t = view.findViewById(R.id.tv_t);
        TextView tv_plazas = view.findViewById(R.id.tv_plazas);
        ImageView img = view.findViewById(R.id.img);

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
            Date time = sdf1.parse(datos.get(position).getHora());
            Date time2 = sdf1.parse(datos.get(position).getDuracion());

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

        tv_destino.setText(datos.get(position).getDestino());
        tv_precio.setText("PEN "+datos.get(position).getPrecio());
        tv_plazas.setText(datos.get(position).getPlazasturista()+"");

        img.setImageBitmap(datos.get(position).getImagen());

        try {
            String dateString = datos.get(position).getFecha()+"T"+datos.get(position).getHora()+"Z";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = sdf.parse(dateString);
            tv_t.setText(Tiempo.TiempoT(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
