package com.example.app_grupo04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.adapter.HotelesAdapter;
import com.example.app_grupo04.modelo.Hotel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescripcionHotelActivity extends AppCompatActivity implements  View.OnClickListener, Response.Listener<JSONObject>,Response.ErrorListener,OnMapReadyCallback {

    private TextView tv_nombre_hotel,tv_ciudad_hotel,tv_telefono_hotel,tv_direccion_hotel,tv_plazas_hotel;
    private ImageView iv_imagen_hotel;
    private Button estancias;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private Bundle bundle;
    private CardView cv_telefono,cv_direccion,cv_plazas,cv_mapa;
    private Button btn_telefono,btn_direccion,btn_plazas,btn_mapa,btn_all;

    private int codigoHotel = 0;
    private GoogleMap mMap;
    private final String ip = "192.168.0.192";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_hotel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        bundle = getIntent().getExtras();
        codigoHotel = (Integer) bundle.getInt("codigoHotel");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cv_telefono = findViewById(R.id.cv_telefono);
        cv_direccion = findViewById(R.id.cv_direccion);
        cv_plazas = findViewById(R.id.cv_plazas);
        cv_mapa = findViewById(R.id.cv_mapa);

        cv_telefono.setVisibility(View.GONE);
        cv_direccion.setVisibility(View.GONE);
        cv_plazas.setVisibility(View.GONE);

        btn_telefono = findViewById(R.id.btn_telefono);
        btn_direccion = findViewById(R.id.btn_direccion);
        btn_plazas = findViewById(R.id.btn_plazas);
        btn_mapa = findViewById(R.id.btn_mapa);
        btn_all = findViewById(R.id.btn_all);

        btn_telefono.setOnClickListener(this::onClick);
        btn_direccion.setOnClickListener(this::onClick);
        btn_plazas.setOnClickListener(this::onClick);
        btn_mapa.setOnClickListener(this::onClick);
        btn_all.setOnClickListener(this::onClick);
        btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
        btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
        btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
        btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
        btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));


        tv_nombre_hotel = findViewById(R.id.tv_nombre_hotel);
        tv_ciudad_hotel = findViewById(R.id.tv_ciudad_hotel);
        tv_telefono_hotel = findViewById(R.id.tv_telefono_hotel);
        tv_direccion_hotel = findViewById(R.id.tv_direccion_hotel);
        tv_plazas_hotel = findViewById(R.id.tv_plazas_hotel);
        iv_imagen_hotel = findViewById(R.id.iv_imagen_hotel);
        estancias = findViewById(R.id.btn_estancias);
        estancias.setOnClickListener(this::onClick);

        request = Volley.newRequestQueue(DescripcionHotelActivity.this);
        cargarWebService();
    }

    private void cargarWebService(){
        String url = "http://" + ip + "/bd_remota/JSONConsultaDetalleHotel.php?codigoHotel="+codigoHotel;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_telefono:
                cv_telefono.setVisibility(View.VISIBLE);
                btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
                btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                cv_mapa.setVisibility(View.GONE);
                cv_direccion.setVisibility(View.GONE);
                cv_plazas.setVisibility(View.GONE);
                break;
            case R.id.btn_direccion:
                cv_direccion.setVisibility(View.VISIBLE);
                btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
                btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                cv_mapa.setVisibility(View.GONE);
                cv_telefono.setVisibility(View.GONE);
                cv_plazas.setVisibility(View.GONE);
                break;
            case R.id.btn_plazas:
                cv_plazas.setVisibility(View.VISIBLE);
                btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
                btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                cv_mapa.setVisibility(View.GONE);
                cv_direccion.setVisibility(View.GONE);
                cv_telefono.setVisibility(View.GONE);
                break;
            case R.id.btn_mapa:
                cv_mapa.setVisibility(View.VISIBLE);
                btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
                btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                cv_direccion.setVisibility(View.GONE);
                cv_plazas.setVisibility(View.GONE);
                cv_telefono.setVisibility(View.GONE);
                break;
            case R.id.btn_all:
                cv_mapa.setVisibility(View.VISIBLE);
                btn_mapa.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_direccion.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_plazas.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_telefono.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.gris));
                btn_all.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.Primary));
                cv_direccion.setVisibility(View.VISIBLE);
                cv_plazas.setVisibility(View.VISIBLE);
                cv_telefono.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_estancias:
                Intent intent = new Intent(this,EstanciasHotelActivity.class);
                Bundle contenedor = new Bundle();
                contenedor.putInt("codigoHotel",codigoHotel);
                intent.putExtras(contenedor);
                startActivityForResult(intent,96);
                finish();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Hotel hotel = new Hotel();
        JSONArray jsonArray = response.optJSONArray("hotel");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);

            hotel.setCodigoHotel(jsonObject.optInt("CodigoHotel"));
            hotel.setNombreHotel(jsonObject.optString("NombreHotel"));
            hotel.setDireccionHotel(jsonObject.optString("DireccionHotel"));
            hotel.setCiudadHotel(jsonObject.optString("CiudadHotel"));
            hotel.setTelefonoHotel(jsonObject.optString("TelefonoHotel"));
            hotel.setPlazasHotel(jsonObject.optInt("PlazasHotel"));
            hotel.setDato(jsonObject.optString("Imagen"));
            hotel.setDestino_CodigoDestino(jsonObject.optInt("Destino_CodigoDestino"));
            hotel.setLongitudHotel(jsonObject.optDouble("longitudHotel"));
            hotel.setLatitudHotel(jsonObject.optDouble("latitudHotel"));
            hotel.setNombreDestino(jsonObject.optString("Nombre_Destino"));

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        iv_imagen_hotel.setImageBitmap(hotel.getImagen());
        tv_nombre_hotel.setText(hotel.getNombreHotel());
        tv_ciudad_hotel.setText(hotel.getCiudadHotel());
        tv_direccion_hotel.setText(hotel.getDireccionHotel());
        tv_plazas_hotel.setText("Plazas disponibles: "+hotel.getPlazasHotel());
        tv_telefono_hotel.setText(hotel.getTelefonoHotel());
        Double lo = hotel.getLongitudHotel();
        Double la = hotel.getLatitudHotel();
        mMap.addMarker(new MarkerOptions().position(new LatLng(la,lo)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lo),16));
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}