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
import com.example.app_grupo04.modelo.Hotel;

import java.util.ArrayList;

public class HotelesAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Hotel> datos;

    public HotelesAdapter(@NonNull Context context,ArrayList<Hotel> datos) {
        super(context, R.layout.item_list_hoteles,datos);
        this.context=context;
        this.datos=datos;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_hoteles,null);

        ImageView iv_imagen_hotel = view.findViewById(R.id.iv_imagen_hotel);
        TextView tv_nombre_hotel = view.findViewById(R.id.tv_nombre_hotel);
        TextView tv_ciudad_hotel = view.findViewById(R.id.tv_ciudad_hotel);
        TextView tv_telefono_hotel = view.findViewById(R.id.tv_telefono_hotel);
        TextView tv_plazas = view.findViewById(R.id.tv_plazas);

        iv_imagen_hotel.setImageBitmap(datos.get(position).getImagen());
        tv_nombre_hotel.setText(datos.get(position).getNombreHotel());
        tv_ciudad_hotel.setText(datos.get(position).getCiudadHotel());
        tv_telefono_hotel.setText(datos.get(position).getTelefonoHotel());
        tv_plazas.setText(datos.get(position).getPlazasHotel()+"");
        return view;
    }

    public int getPositionList(int Pos_Lista){
        return datos.get(Pos_Lista).getCodigoHotel();
    }
}
