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
import com.example.streaming.AudioFormat;
import com.example.streaming.AudioSample;
import com.example.streaming.AudioStreamGrpc;
import com.example.streaming.Cancion;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileOutputStream;

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

    private ManagedChannel canal;
    private AudioStreamGrpc.AudioStreamStub stub;

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
        play=(ImageButton) findViewById(R.id.busqueda);
        mp=MediaPlayer.create(this,R.raw.hail);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(100);
        adelantar=(ImageButton)findViewById(R.id.adelantarAudio);
        volver=(ImageButton)findViewById(R.id.botonV);
        atrasar=(ImageButton)findViewById(R.id.atrasarAudio);
        tiempoAudio=(TextView)findViewById(R.id.tiempoAudio);
        pausar=(ImageButton)findViewById(R.id.pausar);
        ObtenerAudioFormatDeCancion();
    }

    public void ObtenerAudioFormatDeCancion(){
        canal = ManagedChannelBuilder.forAddress("192.168.0.15", 5001).usePlaintext().build();
        stub = AudioStreamGrpc.newStub(canal);

        final Cancion cancion = Cancion.newBuilder().setNombre("FluorescentAdolescent.wav").build();

        stub.elegirCancion(cancion, new StreamObserver<AudioFormat>() {
            @Override
            public void onNext(AudioFormat value) {
                AudioFormat formato = value;

            }

            @Override
            public void onError(Throwable t) {
                Log.e("ERROR", t.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        });

        stub.obtenerStreamDeCancion(cancion, new StreamObserver<AudioSample>() {
            @Override
            public void onNext(AudioSample value) {
                value.getData().toByteArray();
                ByteArrayMediaDataSource cancion = new ByteArrayMediaDataSource(value.getData().toByteArray());
                mp.setDataSource(cancion);
                mp.start();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("ERROR", t.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        });

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

    public void volver(View view){
        Intent siguiente = new Intent(this,MenuPrincipal.class);
        startActivity(siguiente);
    }

    public void atrasarAudio(View view){
        mp.pause();
        mp=MediaPlayer.create(this,R.raw.dale);
        mp.start();
    }

}
