package com.example.app_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.modelo.Turista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    private EditText et_nombres, et_apellidos, et_usuario, et_password;
    private Button btn_aceptar;
    private TextView tv_cancelar;

    private final String ip = "192.168.0.192";
    private final String carp = "bd_remota";
    private final String Json = "wsJSONRegistro.php";

    Turista miTurista = new Turista();
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        getSupportActionBar().hide();
        request = Volley.newRequestQueue(getBaseContext());

        btn_aceptar = findViewById(R.id.btn_registrar);
        tv_cancelar = findViewById(R.id.tv_cancelar);
        et_nombres = findViewById(R.id.et_nombres);
        et_apellidos = findViewById(R.id.et_apellidos);
        et_usuario = findViewById(R.id.et_usuario);
        et_password = findViewById(R.id.et_password);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_nombres.getText().length() == 0 || et_apellidos.getText().length() == 0  || et_usuario.getText().length() == 0 || et_password.getText().length() == 0 ){
                    Toast.makeText(RegistroActivity.this, "Datos Incompletos", Toast.LENGTH_SHORT).show();
                }else{
                    RegistroService(et_nombres.getText().toString(),et_apellidos.getText().toString(),et_usuario.getText().toString(),et_password.getText().toString());
                }
            }
        });

        tv_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegistroActivity.this,LoginActivity.class));
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Este correo ya ha sido registrado, pruebe con otro", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray json = response.optJSONArray("turista");
        JSONObject jsonObject = null;

        try {
            jsonObject = json.getJSONObject(0);

            miTurista.setCodigo(Integer.parseInt(jsonObject.optString("CodigoTuristas")));
            miTurista.setNombre(jsonObject.optString("NombreTurista"));
            miTurista.setApellido(jsonObject.optString("ApellidosTurista"));
            miTurista.setDireccion(jsonObject.optString("DireccionTurista"));
            miTurista.setTelefono(jsonObject.optString("TelefonoTurista"));
            miTurista.setCorreo(jsonObject.optString("UsuarioTurista"));
            miTurista.setPassword(jsonObject.optString("PassTurista"));
            miTurista.setDato(jsonObject.optString("ImgTurista"));


            guardarLog(miTurista.getCodigo(),
                    miTurista.getCorreo(),
                    miTurista.getPassword(),
                    miTurista.getNombre(),
                    miTurista.getApellido(),
                    miTurista.getDireccion(),
                    miTurista.getTelefono(),
                    miTurista.getDato());

            Toast.makeText(this, "Se ha iniciado la sesión correctamente", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegistroActivity.this, MainActivity.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void RegistroService(String nomb, String ape, String correo, String pass){
        String url =  "http://" + ip + "/" + carp + "/" + Json + "?" + "usu="+ correo +
                "&pass="+ pass +
                "&nomb="+ nomb +
                "&ape="+ ape;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }


    private void guardarLog(int cod, String correo, String pass, String nomb, String ape, String direc, String telf, String dato){
        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("codigo",cod);
        editor.putString("correo",correo);
        editor.putString("pass",pass);
        editor.putString("nomb",nomb);
        editor.putString("ape",ape);
        editor.putString("direc",direc);
        editor.putString("telf",telf);
        editor.putString("img",dato);
        editor.commit();
    }

}