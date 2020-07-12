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

public class ListaReproducir extends AppCompatActivity {

    private Window window;
    private String nombreCancion;
    private ListView listas;
    private ArrayList<String> datos = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int pos;
    private TextView nombreLista;
    private String lista;
    String tipo;
    int idU;
    private ImageButton volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproducir);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        nombreCancion=getIntent().getStringExtra("nombreCancion");
        listas=(ListView)findViewById(R.id.listaListas);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        nombreLista=(TextView)findViewById(R.id.nombreLista);
        tipo = getIntent().getStringExtra("tipo");
        idU = getIntent().getExtras().getInt("id");
        volver = (ImageButton)findViewById(R.id.botonVolver);
        obtenerListas();

        listas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lista=datos.get(pos);
                registrarCancionALista(lista,nombreCancion);
            }
        });
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

    public void registrarCancionALista(String lista,String nombreCancion){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<Boolean> call = postService.PostLigarLiastaConCancion(lista, nombreCancion);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                   Log.e("Exito"," si se pudo");
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    public void registrarLista(View view){
        registroLista();
    }

    private void registroLista() {
        String nombreDeLista = String.valueOf(nombreLista.getText());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        ListaDeReproduccion lista = new ListaDeReproduccion(nombreDeLista,idU);
        Call<ListaDeReproduccion> call = postService.PostListaDeReproduccion(lista);

        call.enqueue(new Callback<ListaDeReproduccion>() {
            @Override
            public void onResponse(Call<ListaDeReproduccion> call, Response<ListaDeReproduccion> response) {
                try {
                    Log.e("Se registró la lista",response.message());
                    nombreLista.setText("");
                    obtenerListas();
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ListaDeReproduccion> call, Throwable t) {
            }
        });
    }

    public void obtenerListas(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<List<String>> call = postService.GetListasDeReproduccion(4654);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>>call, Response<List<String>> response) {
                try {
                    for(String nombre : response.body()) {
                        datos.add(nombre);
                    }
                    listas.setAdapter(adapter);
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
