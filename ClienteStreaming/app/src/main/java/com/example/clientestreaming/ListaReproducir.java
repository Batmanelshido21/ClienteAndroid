package com.example.clientestreaming;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ListaReproducir extends AppCompatActivity {

    private Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproducir);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
    }
}
