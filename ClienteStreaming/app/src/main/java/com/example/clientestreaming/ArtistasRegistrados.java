package com.example.clientestreaming;

import android.content.Intent;
import android.util.Log;
import android.view.View;
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
    ArrayList<Integer> idArtistas = new ArrayList<>();
    int idArtista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistas_registrados);

        listViewArtistas = (ListView)findViewById(R.id.listViewArtistas);

        nombreArtistas =  new  ArrayList<>();
        idArtistas =  new  ArrayList<>();

        obtenerArtistas();

        Toast.makeText(this, "Seleccione a un artista para continuar", Toast.LENGTH_LONG).show();

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombreArtistas);

        listViewArtistas.setAdapter(adapter);


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
                .baseUrl("http://192.168.1.66:5001/")
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
