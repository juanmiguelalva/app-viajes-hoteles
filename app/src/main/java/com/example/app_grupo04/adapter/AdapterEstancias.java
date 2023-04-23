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
import com.example.app_grupo04.modelo.Estancia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterEstancias extends ArrayAdapter {

    private Context context;
    private ArrayList<Estancia> datos;

    public AdapterEstancias(@NonNull Context context, ArrayList<Estancia> datos) {
        super(context, R.layout.adapter_estancia,datos);
        this.context=context;
        this.datos=datos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_estancia,null);
        TextView tv_entrada = view.findViewById(R.id.tv_fentrada);
        TextView tv_salida = view.findViewById(R.id.tv_fsalida);
        TextView tv_precio = view.findViewById(R.id.tv_precio);
        TextView tv_pension = view.findViewById(R.id.tv_pension);

        SimpleDateFormat sdf1 =  new SimpleDateFormat( "yyyy-MM-dd");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("EEE, dd MMM yyyy",new Locale("es", "ES"));
        try {
            Date fecha1 = sdf1.parse(datos.get(position).getFentrada());
            Date fecha2 = sdf1.parse(datos.get(position).getFsalida());

            tv_entrada.setText(sdf2.format(fecha1));
            tv_salida.setText(sdf2.format(fecha2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_pension.setText(datos.get(position).getPension());
        tv_precio.setText("PEN "+datos.get(position).getPrecio());

        return view;
    }
}
