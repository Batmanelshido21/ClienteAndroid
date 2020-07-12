package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ListaReproduccion extends AppCompatActivity {

    private Window window;
    private ImageButton botonVolver;
    private ListView listaCanciones;
    private String nombreCancion;
    ArrayAdapter<String> adapter;
    ArrayList<String> datos;
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
        botonVolver=(ImageButton)findViewById(R.id.botonV);
        listaCanciones=(ListView)findViewById(R.id.listaCanciones);
        nombreCancion=getIntent().getStringExtra("nombreCancion");
        datos = new ArrayList<String>();

    }

    public void volver(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void obtenerListasReproduccion(){
        datos.add("Hola");
        datos.add("Perro");
        datos.add("Sucio");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datos);
        listaCanciones.setAdapter(adapter);
    }
}
