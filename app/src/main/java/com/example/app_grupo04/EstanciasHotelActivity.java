package com.example.app_grupo04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.adapter.AdapterEstancias;
import com.example.app_grupo04.modelo.Estancia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstanciasHotelActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemClickListener {

    int codhotel=0;
    private int codigoVueloTurista;
    private Bundle bundle;
    private ListView listestancia;
    RequestQueue request;
    JsonObjectRequest json;
    ArrayList<Estancia> lestancias;
    private final String ip = "192.168.0.192";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estancias_hotel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Seleccionar su Estancia");

        bundle=getIntent().getExtras();
        if (bundle!=null){
            codhotel = bundle.getInt("codigoHotel");
        }
        cargarPreferencias();
        listestancia=findViewById(R.id.listestancia);

        request = Volley.newRequestQueue(getBaseContext());
        lestancias = new ArrayList<>();
        listestancia.setOnItemClickListener(this::onItemClick);
        cargarWebService();

    }

    private void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        int codigoVuTur = preferences.getInt("codigoVueloTurista",0);
        codigoVueloTurista = codigoVuTur;
    }

    private void cargarWebService() {
        String url = "http://" + ip + "/bd_remota/estancias.php?codhotel="+codhotel;
        json= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(json);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No hay estancias", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Estancia estancia = null;
        JSONArray jsonArray = response.optJSONArray("estancias");

        if(jsonArray!=null){
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    estancia = new Estancia();
                    JSONObject jsonobject = null;
                    jsonobject=jsonArray.getJSONObject(i);
                    estancia.setCodigo(jsonobject.optInt("codigo"));
                    estancia.setPension(jsonobject.optString("pension"));
                    estancia.setFentrada(jsonobject.optString("fentrada"));
                    estancia.setFsalida(jsonobject.optString("fsalida"));
                    estancia.setCodigohotel(jsonobject.optInt("codigohotel"));
                    estancia.setPrecio(jsonobject.optDouble("precio"));
                    lestancias.add(estancia);
                }
                AdapterEstancias adapter = new AdapterEstancias(this,lestancias);
                listestancia.setAdapter(adapter);


            }catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Está seguro de reservar esta estancia?").
                setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        int codigoturista=preferences.getInt("codigo",0);
                        int codigoestancia=lestancias.get(position).getCodigo();
                        int codigohotel= lestancias.get(position).getCodigohotel();
                        String url = "http://" + ip + "/bd_remota/wsJSONInsertarViajesContratados.php?codestancia="+codigoestancia+
                                "&codturista="+codigoturista+"&codvueloturista="+codigoVueloTurista+"&codhotel="+codigohotel;

                        json= new JsonObjectRequest(Request.Method.GET,url,null,null,null);
                        request.add(json);

                        if(codigoVueloTurista!=0){
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("codigoVueloTurista",0);
                            editor.commit();
                        }
                        finish();
                        Toast.makeText(EstanciasHotelActivity.this, "Estancia reservada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder1.show();
    }
}