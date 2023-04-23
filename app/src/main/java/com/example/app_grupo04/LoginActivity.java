package com.example.app_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.modelo.Turista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    private EditText et_user , et_pass;
    private TextView tv_registrar;
    private Button btn_ingresar;

    private final String ip = "192.168.0.192";
    private final String carp = "bd_remota";
    private final String Json = "wsJSONConsultarUsuario.php";

    Turista miTurista = new Turista();

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        request = Volley.newRequestQueue(getBaseContext());

        et_user = findViewById(R.id.et_usuario);
        et_pass = findViewById(R.id.et_password);
        tv_registrar = findViewById(R.id.tv_registrar);
        btn_ingresar = findViewById(R.id.btn_ingresar);

        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ValidarService(et_user.getText().toString(),et_pass.getText().toString());
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Lo sentimos, este usuario no existe", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(this, jsonObject.optString("ImgTurista") + "", Toast.LENGTH_SHORT).show();
            //Guardar turista tmp;
            guardarLog(miTurista.getCodigo(),
                    miTurista.getCorreo(),
                    miTurista.getPassword(),
                    miTurista.getNombre(),
                    miTurista.getApellido(),
                    miTurista.getDireccion(),
                    miTurista.getTelefono(),
                    miTurista.getDato());

            Toast.makeText(this, "Se ha iniciado la sesión correctamente", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ValidarService(String correo, String pass){
        String url =  "http://" + ip + "/" + carp + "/" + Json + "?" + "usu=" + correo + "&pass=" + pass;
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

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        System.exit(0);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}