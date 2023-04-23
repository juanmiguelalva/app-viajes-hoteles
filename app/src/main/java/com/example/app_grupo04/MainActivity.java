package com.example.app_grupo04;

import static androidx.navigation.Navigation.findNavController;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_grupo04.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private final String ip = "192.168.0.192";
    private final String carp = "bd_remota";
    private final String Json = "wsJSONConsultarUsuario.php";

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    Bundle bundle=new Bundle();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        super.onCreate(savedInstanceState);
        request = Volley.newRequestQueue(getBaseContext());

        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        Integer codigo = preferences.getInt("codigo",0);
        String correo = preferences.getString("correo", "");
        String pass = preferences.getString("pass","");
        ValidarService(correo,pass);
    }

    public void view_login(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    public void nav_bar(){
            progressDialog.hide();
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.drawerLayout);
            setSupportActionBar(binding.appBarMain.toolbar);

            View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

            TextView et_nombre, et_correo;
            ImageView img_user;

            et_nombre = header.findViewById(R.id.nombre_prin);
            et_correo = header.findViewById(R.id.correo);
            img_user = header.findViewById(R.id.img_turista);

            SharedPreferences preferences = this.getSharedPreferences("Log", Context.MODE_PRIVATE);

            et_correo.setText(preferences.getString("correo", ""));
            et_nombre.setText(preferences.getString("nomb", "") + " " + preferences.getString("ape", ""));

            String img = preferences.getString("img", "");
            byte[] byteCode = Base64.decode(img, Base64.DEFAULT);


            if (img.equals("") || img.equals("null")) {
                Bitmap Imagen = BitmapFactory.decodeResource(getResources(), R.drawable.defaultuser);
                img_user.setImageBitmap(Imagen);
            }else{
                Bitmap Imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
                img_user.setImageBitmap(Imagen);
            }


            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                  R.id.nav_editarTurista,R.id.nav_vuelos,R.id.nav_hoteles,R.id.nav_vuelosturista,R.id.nav_inicio)
                    .setOpenableLayout(drawer)
                    .build();
            NavController navController = findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            bundle=getIntent().getExtras();
            if(bundle!=null){
                int id = bundle.getInt("id");
                switch (id){
                    case 1:
                        findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_inicio_to_nav_hoteles);
                        break;
                    case 2:
                        findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_inicio_to_nav_vuelos);
                        break;
                }
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.Cerrar_sesion:
                finish();
                CerrarSesion();
                view_login();
                Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void CerrarSesion(){
        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("codigo").commit();
        editor.remove("correo").commit();
        editor.remove("pass").commit();
        editor.remove("nomb").commit();
        editor.remove("ape").commit();
        editor.remove("direc").commit();
        editor.remove("telf").commit();
        editor.remove("img").commit();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        view_login();
    }

    @Override
    public void onResponse(JSONObject response) {
        nav_bar();
    }

    private void ValidarService(String correo, String pass){
        String url =  "http://" + ip + "/" + carp + "/" + Json + "?" + "usu=" + correo + "&pass=" + pass;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }
}