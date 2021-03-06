package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.util.Base64;

public class ReproduccionMP3 extends AppCompatActivity {

    private Window window;
    private ImageButton play;
    private ProgressBar progressBar;
    private ImageButton adelantar;
    private ImageButton atrasar;
    private ImageButton volver;
    private TextView tiempoAudio;
    private ImageButton pausar;
    private ImageButton guardarCancion;
    private Button listaReproduccion;
    MediaPlayer mp;
    private String nombreCancion;
    byte[] byteArrray;
    private Button descargar;

    private ManagedChannel canal;
    Audio audio;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduccion_m_p3);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        mp=MediaPlayer.create(this,R.raw.hail);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        adelantar=(ImageButton)findViewById(R.id.adelantarAudio);
        volver=(ImageButton)findViewById(R.id.botonV);
        atrasar=(ImageButton)findViewById(R.id.atrasarAudio);
        tiempoAudio=(TextView)findViewById(R.id.tiempoAudio);
        pausar=(ImageButton)findViewById(R.id.pausar);
        nombreCancion=getIntent().getStringExtra("nombreCancion");
        play=(ImageButton)findViewById(R.id.reproductor);
        descargar=(Button)findViewById(R.id.botonDescargar);
        reproducir(nombreCancion);
    }

    public void reproducir(View view){
        mp.start();
    }

    public void pausar(View view){
        mp.pause();
    }

    public void cambiar(View view){
        mp.pause();
        mp=MediaPlayer.create(this,R.raw.gta);
        mp.start();
    }

    public void guardarAudio(View view){

    }

    public void volver(View view){
        mp.pause();
        Intent siguiente = new Intent(this,MenuPrincipal.class);
        startActivity(siguiente);
    }

    public void atrasarAudio(View view){
        mp.pause();
        mp=MediaPlayer.create(this,R.raw.dale);
        mp.start();
    }
    public void reproducir(String nombreCancion){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<Audio> call = postService.obtenerCancion(nombreCancion);
        System.out.println("antes del error");
        call.enqueue(new Callback<Audio>() {
            @Override
            public void onResponse(Call<Audio> call, Response<Audio> response) {
                try {
                    audio= response.body();
                    byteArrray = Base64.getDecoder().decode(audio.getCancion().getBytes());
                    File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
                    tempMp3.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(tempMp3);
                    fos.write(byteArrray);
                    fos.close();

                    // resetting mediaplayer instance to evade problems
                    mp.reset();

                    // In case you run into issues with threading consider new instance like:
                    // MediaPlayer mediaPlayer = new MediaPlayer();

                    // Tried passing path directly, but kept getting
                    // "Prepare failed.: status=0x1"
                    // so using file descriptor instead
                    FileInputStream fis = new FileInputStream(tempMp3);
                    mp.setDataSource(fis.getFD());

                    mp.prepare();
                    mp.start();

                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Audio> call, Throwable t) {
                Log.e("Resuesta", "Falló");
            }
        });
    }
}
