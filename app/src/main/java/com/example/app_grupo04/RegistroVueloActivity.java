package com.example.app_grupo04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.modelo.Destino;
import com.example.app_grupo04.modelo.Vuelo;
import com.example.app_grupo04.modelo.VueloTurista;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistroVueloActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Bundle bundle = new Bundle();
    private TextView tv_destino;
    private ImageView img;
    private RequestQueue request;
    private JsonObjectRequest json;
    private NumberPicker np_adulto;
    private NumberPicker np_nino;
    private NumberPicker np_bebe;
    private AutoCompleteTextView spn_clases;
    private TextInputLayout til;
    private ArrayList<String> clases;
    private Vuelo objvuelo = new Vuelo();
    private ProgressDialog progressDialog;
    private double la = 0.0;
    private double lo = 0.0;
    private VueloTurista vtnew;

    private final String ip = "192.168.0.192";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        request = Volley.newRequestQueue(getBaseContext());
        setContentView(R.layout.activity_registro_vuelo);

        bundle=getIntent().getExtras();

        objvuelo= (Vuelo) bundle.getSerializable("objvuelo");

        tv_destino=findViewById(R.id.tv_destino);

        spn_clases=findViewById(R.id.spn_clases);
        til = findViewById(R.id.til);

        np_adulto=findViewById(R.id.np_adulto);
        np_nino=findViewById(R.id.np_nino);
        np_bebe=findViewById(R.id.np_bebe);

        img=findViewById(R.id.img);
        cargarWebService();

        findViewById(R.id.confirmar).setOnClickListener(this::onClick);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        clases = new ArrayList<>();

        clases.add("Primera clase");
        clases.add("Clase ejecutiva");
        clases.add("Turista");

        ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clases);

        spn_clases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                til.setErrorEnabled(false);
            }
        });

        spn_clases.setAdapter(myAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                setResult(RESULT_CANCELED);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarWebService() {
        String url = "http://" + ip + "/bd_remota/wsJSONConsultaDestino.php?cod="+objvuelo.getCod_destino();

        json= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(json);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Destino destino = new Destino();
        JSONArray jsonArray = response.optJSONArray("destino");
        JSONArray jsonArray2 = response.optJSONArray("codigovuelo");

        if(jsonArray!=null){
            try {
                JSONObject jsonobject = null;
                jsonobject=jsonArray.getJSONObject(0);
                destino.setCodigo(jsonobject.optInt("codigo"));
                destino.setNombre(jsonobject.optString("nombre"));
                destino.setLatitud(Double.parseDouble(jsonobject.optString("latitud")));
                destino.setLongitud(Double.parseDouble(jsonobject.optString("longitud")));
                destino.setDato(jsonobject.optString("img"));

            }catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }

            tv_destino.setText(destino.getNombre());
            la = destino.getLatitud();
            lo = destino.getLongitud();
            img.setImageBitmap(destino.getImagen());

            mMap.addMarker(new MarkerOptions().position(new LatLng(la,lo)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lo),12));
        }

        if(jsonArray2!=null){
            try {
                JSONObject jsonobject2 = null;
                jsonobject2=jsonArray2.getJSONObject(0);
                vtnew.setCodigo(jsonobject2.optInt("codigo"));

                Intent intent = new Intent(this,VueloConfirmado.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("vtnew",vtnew);
                intent.putExtras(bundle);
                startActivityForResult(intent,99);
            }catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.hide();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmar:
                int pasajeros = np_adulto.getValue()+np_nino.getValue()+np_bebe.getValue();
                if (pasajeros<=objvuelo.getPlazasturista()){
                    if(spn_clases.getText().toString().length()>0){
                        double monto=pasajeros*objvuelo.getPrecio();
                        String clase = spn_clases.getText().toString();
                        int numvuelo = objvuelo.getCodigo();
                        int resta = objvuelo.getPlazasturista()-pasajeros;

                        String url = "http://" + ip + "/bd_remota/wsJSONInsertarVueloTurista.php?clase="+clase+"&monto="+monto+"&cant="+pasajeros+"&numerovuelo="+numvuelo+"&resta="+resta;
                        json= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
                        request.add(json);

                        vtnew = new VueloTurista();
                        vtnew.setClase(clase);vtnew.setMonto(monto);vtnew.setPasajeros(pasajeros);vtnew.setNumerovuelo(numvuelo);

                    }else{
                        til.setError("Campo Vacío");
                    }
                }
                else{
                    Toast.makeText(this, "No hay plazas suficientes", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==  RESULT_OK){
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}