package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarArtista extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private Button inicio;
    private ImageView imagenArtista;
    private Button cargarImagen;
    private TextView nombreA;
    private TextView descripcion;
    Artista user;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_artista);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton)findViewById(R.id.volverRegistro);
        inicio=(Button)findViewById(R.id.botonInicio);
        imagenArtista=(ImageView)findViewById(R.id.imagenArtista);
        cargarImagen=(Button)findViewById(R.id.cargarImagen);
        nombreA=(TextView)findViewById(R.id.nombreArtisticoInput);
        descripcion=(TextView)findViewById(R.id.descripcionInput);
    }

    public void volverInicio(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void cargarImagen(View view){
        Intent foto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        foto.setType("image/");
        startActivityForResult(foto.createChooser(foto,"Seleccione la aplicación"),10);
    }

    public void registrarArtista(View view){

        String nombreArtista = String.valueOf(nombreA.getText());
        String descripcionArtista = String.valueOf(descripcion.getText());

        if(validarDatos(nombreArtista, descripcionArtista)){

            int id = (int) (Math.random() * 10000) + 1;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.15:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IServicioLogin postService = retrofit.create(IServicioLogin.class);
            Call<Artista> call = postService.PostArtista(id, nombreArtista, descripcionArtista);

            call.enqueue(new Callback<Artista>() {

                @Override
                public void onResponse(Call<Artista> call, Response<Artista> response) {
                    try {
                        user = response.body();
                        Log.e("Se registró correctamente ",user.getNombreArtistico());
                        Log.e(" de correo electronico",user.getDescripcion());
                        nombreA.setText("");
                        descripcion.setText("");
                    } catch (Exception e) {
                        Log.e("Error",e.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<Artista> call, Throwable t) {
                }
            });

        }else{

            Toast.makeText(this, "No se pueden dejar campos vacios ni llenar con espacios",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagenArtista.setImageURI(path);
        }
    }

    public boolean validarDatos(String nombreArtistico, String descripcion){

        String nombre = nombreArtistico.replaceAll("\\s", "");
        String descripcionArtista = descripcion.replaceAll("\\s", "");

        if(!nombre.isEmpty() && !descripcionArtista.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
