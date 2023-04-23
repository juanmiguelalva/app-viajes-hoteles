package com.example.app_grupo04;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.adapter.AdapterVueloTurista;
import com.example.app_grupo04.modelo.VueloTurista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VuelosTuristaFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private ListView listavueloturista;
    private ArrayList<VueloTurista> vuelosturista;

    private RequestQueue request;
    private JsonObjectRequest json;

    private ProgressDialog progressDialog;

    private final String ip = "192.168.0.192";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public VuelosTuristaFragment() {
    }

    public static VuelosTuristaFragment newInstance(String param1, String param2) {
        VuelosTuristaFragment fragment = new VuelosTuristaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vuelos_turista, container, false);

        listavueloturista=view.findViewById(R.id.listavueloturista);

        request = Volley.newRequestQueue(getContext());
        vuelosturista = new ArrayList<>();
        cargarWebService();
        return view;
    }

    private void cargarWebService() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String url = "http://" + ip + "/bd_remota/wsJSONVuelosTurista.php";

        json= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(json);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        VueloTurista vueloTurista = null;

        JSONArray jsonArray = response.optJSONArray("vueloturista");

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                vueloTurista = new VueloTurista();
                JSONObject jsonobject = null;
                jsonobject=jsonArray.getJSONObject(i);
                vueloTurista.setCodigo(jsonobject.optInt("codigo"));
                vueloTurista.setFecha(jsonobject.optString("fecha"));
                vueloTurista.setHora(jsonobject.optString("hora"));
                vueloTurista.setDestino(jsonobject.optString("nombre"));
                vueloTurista.setPasajeros(jsonobject.optInt("pasajeros"));
                vuelosturista.add(vueloTurista);
            }

            AdapterVueloTurista adapterVueloTurista = new AdapterVueloTurista(getContext(),vuelosturista);
            listavueloturista.setAdapter(adapterVueloTurista);
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
        progressDialog.hide();
    }
}