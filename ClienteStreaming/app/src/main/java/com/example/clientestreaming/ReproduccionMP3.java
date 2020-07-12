package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
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
import java.util.ArrayList;
import java.util.List;

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
    private List<String> nombresDeCanciones = new ArrayList<>();
    int pos;
     private ImageView image;
     String tipo;
     int idU;


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
        nombresDeCanciones = (ArrayList<String>) getIntent().getSerializableExtra("miLista");
        pos = getIntent().getExtras().getInt("pos");
        listaReproduccion= (Button)findViewById(R.id.botonListaR);
        image =(ImageView)findViewById(R.id.imagenAlbum);
        tipo = getIntent().getStringExtra("tipo");
        idU = getIntent().getExtras().getInt("id");

        reproducir(nombreCancion);
    }

    private void obtenerImagen() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);

        Call<Imagen> call = postService.getImagenAlbum(nombreCancion);

        call.enqueue(new Callback<Imagen>() {
            @Override
            public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                try {
                   Imagen imagen = response.body();
                    byte[] byteArray = Base64.decode(imagen.getImagen(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    image.setImageBitmap(bitmap);
                   Log.e("Exito",imagen.getImagen());

                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Imagen> call, Throwable t) {
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
        if(pos < (nombresDeCanciones.size() -1)){
            pos++;
            Log.e("Nombre Cancion: ", nombresDeCanciones.get(pos));
            reproducir(nombresDeCanciones.get(pos));
        }
    }

    public void AgregarALista(View view){
        Intent siguiente = new Intent(this,ListaReproducir.class);
        siguiente.putExtra("nombreCancion",nombresDeCanciones.get(pos));
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void volver(View view){
        mp.pause();
        Intent siguiente = new Intent(this,MenuPrincipal.class);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void atrasarAudio(View view){
        if(pos >= 1){
            pos--;
            Log.e("Nombre Cancion: ", nombresDeCanciones.get(pos));
            reproducir(nombresDeCanciones.get(pos));
        }
    }

    public void reproducir(String nombreCancion){
        obtenerImagen();
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
                    byteArrray = Base64.decode(audio.getCancion(), Base64.DEFAULT);//Base64.getDecoder().decode(audio.getCancion().getBytes());
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

