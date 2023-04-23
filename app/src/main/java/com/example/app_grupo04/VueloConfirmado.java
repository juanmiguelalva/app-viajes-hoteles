package com.example.app_grupo04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_grupo04.modelo.VueloTurista;

public class VueloConfirmado extends AppCompatActivity implements View.OnClickListener {

    Bundle bundle;
    VueloTurista vtnew;
    TextView tv_monto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vuelo_confirmado);
        bundle = new Bundle();
        vtnew= new VueloTurista();
        bundle=getIntent().getExtras();
        vtnew= (VueloTurista) bundle.getSerializable("vtnew");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Confirmación de Vuelo");

        tv_monto=findViewById(R.id.tv_monto);
        tv_monto.setText("Total a Pagar: PEN "+vtnew.getMonto());

        findViewById(R.id.btn_hoteles).setOnClickListener(this);
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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("codigoVueloTurista",0);
        editor.commit();
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("codigoVueloTurista",vtnew.getCodigo());
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hoteles:
                setResult(RESULT_OK);
                guardarPreferencias();
                finish();
                break;
        }
    }
}