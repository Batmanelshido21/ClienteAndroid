package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarAlbum extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private Button canciones;
    private ImageView imagenAlbum;
    private Button botonCargar;
    Album album;
    private TextView nombre;
    private TextView fecha;
    private TextView descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_album);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton) findViewById(R.id.botonV);
        canciones=(Button)findViewById(R.id.botonCanciones);
        imagenAlbum=(ImageView)findViewById(R.id.imagenAlbum);
        botonCargar=(Button)findViewById(R.id.cargarImagen);
        nombre=(TextView)findViewById(R.id.nombreAlbumInput);
        fecha=(TextView)findViewById(R.id.fechaAlbumInput);
        descripcion=(TextView)findViewById(R.id.descripcionAlbumInput);
    }

    public void volver(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void canciones(View view){
        registroAlbum();

    }

    public void cargarImagen(View view){
        Intent foto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        foto.setType("image/");
        startActivityForResult(foto.createChooser(foto,"Seleccione la aplicación"),10);
    }

    public void registroAlbum(){
        String name= String.valueOf(nombre.getText());
        String date= String.valueOf(fecha.getText());
        String descrip= String.valueOf(descripcion.getText());
        int id = (int) (Math.random() * 10000) + 1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<Album> call = postService.PostAlbum(id,name,date,descrip);


        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                try {
                    album = response.body();
                    Log.e("Respuesta", album.getNombre());
                    registroCanciones();
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Album> call, Throwable t) {
            }
        });
    }

    public void registroCanciones(){
        Intent siguiente = new Intent(this,RegistroCanciones.class);
        startActivity(siguiente);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagenAlbum.setImageURI(path);
        }
    }
}
