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

public class ListaReproduccion extends AppCompatActivity {

    private Window window;
    private ImageButton botonVolver;
    private ListView listaCanciones;
    private String nombreCancion;
    ArrayAdapter<String> adapter;
    ArrayList<String> datos;
    String listaReproduccion;
    String tipo;
    int idU;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproduccion);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        botonVolver=(ImageButton) findViewById(R.id.botonV);
        listaCanciones=(ListView)findViewById(R.id.listaCanciones);
        datos = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        tipo = getIntent().getStringExtra("tipo");
        idU = getIntent().getExtras().getInt("id");
        obtenerListas();

        listaCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaReproduccion=datos.get(i);
                mostrarListas(listaReproduccion);
            }
        });
    }

    public void mostrarListas(String listaReproduccion){
        Intent siguiente = new Intent(this,contenidoLista.class);
        siguiente.putExtra("listaReproduccion",listaReproduccion);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

    public void volverMenu(View view){

        if(tipo.equalsIgnoreCase("usuario")){
            Intent siguiente = new Intent(this,MenuPrincipal.class);
            siguiente.putExtra("tipo",tipo);
            siguiente.putExtra("id",idU);
            startActivity(siguiente);
        }else{
            Intent siguiente = new Intent(this,InicioCreadorContenido.class);
            siguiente.putExtra("tipo",tipo);
            siguiente.putExtra("id",idU);
            startActivity(siguiente);
        }
    }

    private void obtenerListas() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<List<String>> call = postService.GetListasDeReproduccion(idU);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>>call, Response<List<String>> response) {
                try {
                    for(String song : response.body()) {
                        datos.add(song);
                    }

                    Log.e("Texto","");
                    listaCanciones.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        });
    }

}
