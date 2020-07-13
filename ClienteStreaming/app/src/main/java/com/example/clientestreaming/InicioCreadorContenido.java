package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class InicioCreadorContenido extends AppCompatActivity {

    private Window window;
    private Button artista;
    private ImageButton volver;
    private Button album;
    private ImageButton boton;
    private Button listaReproduccion;
    private ArrayList<String> datos = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView listaCanciones;
    String nombreCancion;
    int pos;
    private ImageButton bAudio;
    private TextView buscarTexto;
    String tipo;
    int idU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_creador_contenido);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        artista=(Button)findViewById(R.id.registroArtista);
        volver=(ImageButton) findViewById(R.id.botonV);
        album=(Button)findViewById(R.id.registroAlbum);
        listaReproduccion=(Button)findViewById(R.id.lista);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        listaCanciones=(ListView)findViewById(R.id.listaCanciones);
        bAudio=(ImageButton)findViewById(R.id.buscarAudio);
        buscarTexto=(TextView)findViewById(R.id.buscadorTexto);
        tipo = getIntent().getStringExtra("tipo");
        idU = getIntent().getExtras().getInt("id");

        listaCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nombreCancion = datos.get(i);
                pos = i;
                cambioPantalla(nombreCancion);
            }
        });

    }

    public void cambioPantalla(String nombreCancion){
        Intent siguiente = new Intent(this,ReproduccionMP3.class);
        siguiente.putStringArrayListExtra("miLista", datos);;
        siguiente.putExtra("nombreCancion",nombreCancion);
        siguiente.putExtra("pos", pos);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void registroArtista(View view){
        Intent siguiente = new Intent(this,RegistrarArtista.class);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void obtenerCanciones(View view){
        adapter.clear();
        String nombre = String.valueOf(buscarTexto.getText());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<List<CancionRespuesta>> call = postService.getCanciones(nombre);

        call.enqueue(new Callback<List<CancionRespuesta>>() {
            @Override
            public void onResponse(Call<List<CancionRespuesta>> call, Response<List<CancionRespuesta>> response) {
                try {
                    for(CancionRespuesta song : response.body()) {
                        datos.add(song.getNombre());
                    }

                    listaCanciones.setAdapter(adapter);


                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<CancionRespuesta>> call, Throwable t) {
            }
        });
    }
    public void registroAlbum(View view){
        Intent siguiente = new Intent(this, ArtistasRegistrados.class);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void listaReproduccion(View view){
        Intent siguiente = new Intent(this,ListaReproduccion.class);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void volverLogin(View view){
        Intent siguiente = new Intent(this,MainActivity.class);
        startActivity(siguiente);
    }
}
