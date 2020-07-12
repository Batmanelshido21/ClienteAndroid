package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class contenidoLista extends AppCompatActivity {

    private Window window;
    private String listaReproduccion;
    private TextView nombreLista;
    private ImageButton volverLista;
    private ListView listaCanciones;
    private ArrayList<String> datos = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String nombreCancion;
    private TextView nombreL;
    int pos;
    String tipo;
    int idU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_lista);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        listaReproduccion=getIntent().getStringExtra("listaReproduccion");
        nombreLista=(TextView)findViewById(R.id.nombreLista);
        volverLista=(ImageButton)findViewById(R.id.volverMenu);
        listaCanciones=(ListView)findViewById(R.id.listaCanciones);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        nombreL=(TextView)findViewById(R.id.nombreLista);
        nombreLista.setText(listaReproduccion);
        tipo = getIntent().getStringExtra("tipo");
        idU = getIntent().getExtras().getInt("id");
        obtenerCanciones();

        listaCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nombreCancion = datos.get(i);
                pos = i;
                cambioPantalla(nombreCancion);
            }
        });
    }

    public void volver(View view){
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

    private void obtenerCanciones() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<List<String>> call = postService.GetcancionesDeLista(listaReproduccion);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>>call, Response<List<String>> response) {
                try {
                    for(String nombre : response.body()) {
                        datos.add(nombre);
                    }
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

    public void cambioPantalla(String nombreCancion){
        Intent siguiente = new Intent(this,ReproduccionMP3.class);
        siguiente.putStringArrayListExtra("miLista", datos);;
        siguiente.putExtra("nombreCancion",nombreCancion);
        siguiente.putExtra("pos", pos);
        siguiente.putExtra("tipo",tipo);
        siguiente.putExtra("id",idU);
        startActivity(siguiente);
    }

}
