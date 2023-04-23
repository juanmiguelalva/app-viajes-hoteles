package com.example.app_grupo04;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarTuristaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarTuristaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Mis var

    private ImageView imgPerfil;

    private final String ip = "192.168.0.192";
    private final String carp = "bd_remota";
    private final String Json = "wsJSONEditar.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Integer cod;
    TextView tv_nombre, tv_apellido, tv_direccion, tv_telf;
    Button btn_registrar;
    String nomb, ape, direc, telf, dato;
    Bitmap bitmap;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    public EditarTuristaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarTuristaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarTuristaFragment newInstance(String param1, String param2) {
        EditarTuristaFragment fragment = new EditarTuristaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request = Volley.newRequestQueue(getContext());
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_editar_turista, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Log", Context.MODE_PRIVATE);
        nomb = preferences.getString("nomb", "");
        ape = preferences.getString("ape", "");
        direc = preferences.getString("direc","");
        telf = preferences.getString("telf", "");
        dato = preferences.getString("img", "");
        cod = preferences.getInt("codigo", 0);

        // CARGAR DATOS

            tv_nombre = view.findViewById(R.id.tv_nomb_turista);
            tv_apellido = view.findViewById(R.id.et_apell_turista);
            tv_direccion = view.findViewById(R.id.et_direccion_turista);
            tv_telf = view.findViewById(R.id.et_telf_turista);
            imgPerfil = view.findViewById(R.id.img_perfil);
            btn_registrar = view.findViewById(R.id.btn_registrar);

            tv_nombre.setText(nomb);
            tv_apellido.setText(ape);

                if (telf.equals("null")){
                    tv_telf.setText("");
                }else{
                    tv_telf.setText(telf);
                }

                if (direc.equals("null")){
                    tv_direccion.setText("");
                }else{
                    tv_direccion.setText(direc);
                }

            if (dato.equals("") || dato.equals("null")){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultuser);
                imgPerfil.setImageResource(R.drawable.defaultuser);
            }else{
                byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
                Bitmap Imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
                bitmap = Imagen;
                imgPerfil.setImageBitmap(Imagen);
            }

            //Onclick

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });

        return view;
    }

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    TomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),2);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void TomarFotografia(){
        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }

    private void cargarWebService(){
        String url = "http://" + ip + "/" + carp + "/" + Json + "?" ;
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")){

                    //Actualizar nav_header_main
                    SharedPreferences preferences = getContext().getSharedPreferences("Log", Context.MODE_PRIVATE);
                    View header = ((NavigationView) getActivity().findViewById(R.id.nav_view)).getHeaderView(0);

                    TextView et_nombre;
                    ImageView img_user;

                    et_nombre = header.findViewById(R.id.nombre_prin);
                    et_nombre.setText(preferences.getString("nomb", "")  + " " + preferences.getString("ape", ""));

                    img_user = header.findViewById(R.id.img_turista);
                    String img = preferences.getString("img", "");

                    byte[] byteCode = Base64.decode(img, Base64.DEFAULT);
                    Bitmap Imagen = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
                    img_user.setImageBitmap(Imagen);

                    dialog_confirm_edit();

                }else{
                    Toast.makeText(getContext(), "No se ha podido guardar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombre = tv_nombre.getText().toString();
                String apellido = tv_apellido.getText().toString();
                String direccion = tv_direccion.getText().toString();
                String telefono = tv_telf.getText().toString();

                String imagen = convertirImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("apellido", apellido);
                parametros.put("direccion", direccion);
                parametros.put("telefono", telefono);
                parametros.put("imagen",imagen);
                parametros.put("codigo", cod+"");

                guardarLog(nombre,apellido,direccion,telefono,imagen);


                return parametros;
            }
        };

        request.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imageByte = array.toByteArray();
        String imageString = Base64.encodeToString(imageByte, Base64.DEFAULT);

        return imageString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  getActivity().RESULT_OK){
            switch (requestCode){
                case 1:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgPerfil.setImageBitmap(bitmap);

                    break;
                case 2:
                    Uri miPath=data.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                        imgPerfil.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

    private void guardarLog(String nomb, String ape, String direc, String telf, String dato){
        SharedPreferences preferences =this.getActivity().getSharedPreferences("Log", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nomb",nomb);
        editor.putString("ape",ape);
        editor.putString("direc",direc);
        editor.putString("telf",telf);
        editor.putString("img",dato);
        editor.commit();
    }

    private void dialog_confirm_edit(){
        Dialog dialog_confirm = new Dialog(getContext());
        dialog_confirm.setContentView(R.layout.dialog_confirm_edit);
        dialog_confirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_confirm.setCancelable(false);
        dialog_confirm.show();

        TextView tv_aceptar;
        tv_aceptar = dialog_confirm.findViewById(R.id.tv_aceptar);

        tv_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });
    }

}