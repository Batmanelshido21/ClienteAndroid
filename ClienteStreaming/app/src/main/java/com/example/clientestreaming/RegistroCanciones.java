package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.IOException;

public class RegistroCanciones extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private Button seleccionCancion;
    private MediaPlayer mediaPlayer= new MediaPlayer();
    private Button agregar;
    private Button finalizar;
    Cancion cancion;
    private TextView nombre;
    private TextView genero;
    private TextView duracion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_canciones);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton)findViewById(R.id.botonV);
        seleccionCancion=(Button)findViewById(R.id.seleccionCancion);
        agregar=(Button)findViewById(R.id.botonAgregarCancion);
        finalizar=(Button)findViewById(R.id.botonTerminarProceso);
        nombre=(TextView)findViewById(R.id.nombreCancionInput);
        genero=(TextView)findViewById(R.id.generoCancionInput);
        duracion=(TextView)findViewById(R.id.duracionInput);
    }

    public void volver(View view){
        Intent siguiente = new Intent(this,RegistrarAlbum.class);
        startActivity(siguiente);
    }

    public void cargarAudio(View view){
        Intent audio = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        audio.setType("audio/");
        startActivityForResult(audio.createChooser(audio,"Seleccione la aplicación"),10);
    }

    public void menuPrincipal(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void agregarNuevaCancion(View view){
        registrarCancion();
    }

    public void registrarCancion(){
        String name2= String.valueOf(nombre.getText());
        String genero1= String.valueOf(genero.getText());
        String durac= String.valueOf(duracion.getText());
        int id = (int) (Math.random() * 10000) + 1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<Cancion> call = postService.PostCancion(id,name2,genero1,durac);

        call.enqueue(new Callback<Cancion>() {
            @Override
            public void onResponse(Call<Cancion> call, Response<Cancion> response) {
                try {
                    cancion = response.body();
                    Log.e("Respuesta", cancion.getNombre());
                    nombre.setText("");
                    genero.setText("");
                    duracion.setText("");
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Cancion> call, Throwable t) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getApplicationContext(),path);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }
}
