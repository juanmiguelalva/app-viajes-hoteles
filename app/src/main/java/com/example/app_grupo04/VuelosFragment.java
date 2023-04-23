package com.example.app_grupo04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.adapter.AdapterVuelos;
import com.example.app_grupo04.modelo.Vuelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VuelosFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener,AdapterView.OnItemClickListener{

    private ListView listavuelo;
    private ArrayList<Vuelo> vuelos;

    private RequestQueue request;
    private JsonObjectRequest json;

    private ProgressDialog progressDialog;

    private final String ip = "192.168.0.192";

    public VuelosFragment() {
    }

    public static VuelosFragment newInstance(String param1, String param2) {
        VuelosFragment fragment = new VuelosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vuelos, container, false);

        listavuelo=view.findViewById(R.id.listavuelo);
        listavuelo.setOnItemClickListener(this::onItemClick);
        request = Volley.newRequestQueue(getContext());
        vuelos = new ArrayList<>();
        cargarWebService();
        return view;
    }

    private void cargarWebService() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String url = "http://" + ip + "/bd_remota/wsJSONConsultaVuelos.php";

        json= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(json);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Vuelo vuelo = null;
        JSONArray jsonArray = response.optJSONArray("vuelo");
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                vuelo=new Vuelo();
                JSONObject jsonobject = null;
                jsonobject=jsonArray.getJSONObject(i);
                vuelo.setCodigo(jsonobject.optInt("numero"));
                vuelo.setFecha(jsonobject.optString("fecha"));
                vuelo.setHora(jsonobject.optString("hora"));
                vuelo.setDuracion(jsonobject.optString("duracion"));
                vuelo.setOrigen(jsonobject.optString("origen"));
                vuelo.setDestino(jsonobject.optString("destino"));
                vuelo.setPlazastotales(jsonobject.optInt("plazas"));
                vuelo.setPlazasturista(jsonobject.optInt("pturista"));
                vuelo.setPrecio(jsonobject.optDouble("precio"));
                vuelo.setCod_destino(jsonobject.optInt("coddestino"));
                vuelo.setDato(jsonobject.optString("img"));
                vuelos.add(vuelo);
            }

            AdapterVuelos adaptervuelo = new AdapterVuelos(getContext(),vuelos);
            listavuelo.setAdapter(adaptervuelo);
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
        progressDialog.hide();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),RegistroVueloActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objvuelo",vuelos.get(position));
        intent.putExtras(bundle);
        startActivityForResult(intent,98);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getActivity(), resultCode+"", Toast.LENGTH_SHORT).show();
        if(resultCode ==  getActivity().RESULT_OK){

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_vuelos_to_nav_hoteles);
        }
    }
}