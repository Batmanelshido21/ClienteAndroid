package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class ArtistasRegistrados extends AppCompatActivity {

    ListView listViewArtistas;
    ArrayList<String> nombreArtistas;
    ArrayAdapter adapter;
    ArrayList<Integer> idArtistas = new ArrayList<>();
    int idArtista;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistas_registrados);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        listViewArtistas = (ListView)findViewById(R.id.listViewArtistas);
        nombreArtistas =  new  ArrayList<>();
        idArtistas =  new  ArrayList<>();
        Toast.makeText(this, "Seleccione a un artista para continuar", Toast.LENGTH_LONG).show();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombreArtistas);
        obtenerArtistas();

        listViewArtistas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idArtista = idArtistas.get(i);
                Log.e("Id Artista AR", String.valueOf(idArtista));
                desplegarRegistroDeAlbum();
            }
        });
    }

    public void obtenerArtistas(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServicioLogin getService = retrofit.create(IServicioLogin.class);

        Call<List<Artista>> call = getService.obtenerArtistas();

        call.enqueue(new Callback<List<Artista>>() {
            @Override
            public void onResponse(Call<List<Artista>> call, Response<List<Artista>> response) {
                for(Artista artista : response.body()){
                    Log.e("Nombre Artista", artista.getNombreArtistico());
                    nombreArtistas.add(artista.getNombreArtistico());
                    idArtistas.add(artista.getId());
                }

                listViewArtistas.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Artista>> call, Throwable throwable) {

            }
        });
    }

    public  void desplegarRegistroDeAlbum(){
        Intent pantallaRegistrarAlbum = new Intent(this, RegistrarAlbum.class);
        pantallaRegistrarAlbum.putExtra("idArtista", idArtista);
        startActivity(pantallaRegistrarAlbum);
    };
}
