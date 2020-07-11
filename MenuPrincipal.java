package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.renderscript.ScriptGroup.*;


public class MenuPrincipal extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private ListView listaCanciones;
    private ArrayList<String> datos = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private TextView buscarTexto;
    private ImageButton botonBuscar;
    String nombreCancion;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal2);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton) findViewById(R.id.botonV);
        listaCanciones=(ListView)findViewById(R.id.listaCanciones);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        buscarTexto=(TextView)findViewById(R.id.buscadorTexto);
        botonBuscar=(ImageButton)findViewById(R.id.botonBuscar);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);

        listaCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nombreCancion = datos.get(i);
                cambioPantalla(nombreCancion);
            }
        });
    }

    public void cambioPantalla(String nombreCancion){
        Intent siguiente = new Intent(this,ReproduccionMP3.class);
        siguiente.putExtra("nombreCancion",nombreCancion);
        startActivity(siguiente);
    }

    public void volver(View view){
        Intent siguiente = new Intent(this,MainActivity.class);
        startActivity(siguiente);
    }

    public void obtenerCanciones(View view){

        String nombre = String.valueOf(buscarTexto.getText());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<List<Cancion>> call = postService.getCanciones(nombre);

        call.enqueue(new Callback<List<Cancion>>() {
            @Override
            public void onResponse(Call<List<Cancion>> call, Response<List<Cancion>> response) {
                try {
                    for(Cancion song : response.body()) {
                       datos.add(song.getNombre());
                    }
                    listaCanciones.setAdapter(adapter);

                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<Cancion>> call, Throwable t) {
            }
        });
    }
}
