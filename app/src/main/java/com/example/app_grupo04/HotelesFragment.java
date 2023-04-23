package com.example.app_grupo04;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.app_grupo04.modelo.VueloTurista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelesFragment extends Fragment implements AdapterView.OnItemClickListener, Response.Listener<JSONObject>,Response.ErrorListener{

    private ListView lv_hoteles;
    private HotelesAdapter hotelesAdapter;
    private ArrayList<Hotel> listaHoteles;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private EditText et_buscar;
    private TextView tv_resultados;
    private LinearLayout ll_busqueda;
    private int codigo_vuelo_turista = 0;
    private ProgressDialog progressDialog;
    private final String ip = "192.168.0.192";

    public HotelesFragment() {
    }

    public static HotelesFragment newInstance(String param1, String param2) {
        HotelesFragment fragment = new HotelesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_hoteles, container, false);
        ll_busqueda = view.findViewById(R.id.ll_busqueda);

        listaHoteles = new ArrayList<>();
        et_buscar = view.findViewById(R.id.et_buscar);
        tv_resultados = view.findViewById(R.id.tv_resultados);

        lv_hoteles = view.findViewById(R.id.lv_hoteles);
        lv_hoteles.setOnItemClickListener(this::onItemClick);
        request = Volley.newRequestQueue(getContext());

        view.findViewById(R.id.btn_buscar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarConsultaHoteles();
                et_buscar.setText("");
            }
        });
        view.findViewById(R.id.btn_ver_todo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
                et_buscar.setText("");
            }
        });
        cargarPreferencias();

        if (codigo_vuelo_turista==0){
            ll_busqueda.setVisibility(View.VISIBLE);
            cargarWebService();
        }else{
            ll_busqueda.setVisibility(View.GONE);
            consultaCodigoDestinoPorCodigoVueloTurista();

        }
        return view;
    }

    private void cargarPreferencias(){
        SharedPreferences preferences = getActivity().getSharedPreferences("Log",Context.MODE_PRIVATE);
        int cod_vuel_tur = preferences.getInt("codigoVueloTurista",0);
        codigo_vuelo_turista = cod_vuel_tur;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(),DescripcionHotelActivity.class);
        Bundle contenedor = new Bundle();
        contenedor.putInt("codigoHotel",hotelesAdapter.getPositionList(i));
        intent.putExtras(contenedor);
        startActivityForResult(intent,97);
    }
    public void ProgressDialog(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void consultaCodigoDestinoPorCodigoVueloTurista(){
        ProgressDialog();
        String url = "http://" + ip + "/bd_remota/JSONConsultaCodigoDestino.php?codigoVueloTurista="+codigo_vuelo_turista;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void cargarConsultaHoteles(){
        ProgressDialog();
        String url = "http://" + ip + "/bd_remota/JSONConsultaSearchHotel.php?texto="+et_buscar.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void cargarWebService(){
        ProgressDialog();
        String url = "http://" + ip + "/bd_remota/JSONConsultaHoteles.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        tv_resultados.setText("0 resultados");
        listaHoteles.clear();
        hotelesAdapter = new HotelesAdapter(getContext(),listaHoteles);
        lv_hoteles.setAdapter(hotelesAdapter);
        Toast.makeText(getContext(), "Sin resultados", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        Hotel hotel = null;
        JSONArray jsonArray = response.optJSONArray("hoteles");
        listaHoteles.clear();
        if (jsonArray!=null){
            try {
                if (jsonArray.length()==1){
                    tv_resultados.setText(jsonArray.length()+" resultado");
                }else{
                    tv_resultados.setText(jsonArray.length()+" resultados");
                }
                for (int i = 0;i<jsonArray.length();i++){
                    hotel = new Hotel();
                    JSONObject jsonObject = null;
                    jsonObject=jsonArray.getJSONObject(i);

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
                    listaHoteles.add(hotel);
                }
                hotelesAdapter = new HotelesAdapter(getContext(),listaHoteles);
                lv_hoteles.setAdapter(hotelesAdapter);

            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            progressDialog.hide();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK)  {
            Intent intent = new Intent(getContext(),MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id",1);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();
        }
    }


}