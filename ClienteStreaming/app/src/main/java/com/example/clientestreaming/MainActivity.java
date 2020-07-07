package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView contraseña;
    private Button botonRegistrar;
    private Button botonIngresar;
    private Window window;
    ResponseService user;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombreUsuario=(TextView)findViewById(R.id.nombreUsuarioInput);
        contraseña=(TextView)findViewById(R.id.ContraseñaInput);
        botonIngresar=(Button)findViewById(R.id.botonIngresar);
        botonRegistrar=(Button)findViewById(R.id.botonRegistrar);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));

    }

    public void ingresar(View view){
        //getLogin();
        reproducir();
        //String tipo="usuario";
        //menuPrincipal(tipo);
    }

    public void registrarse(View view){
        Intent siguiente = new Intent(this,RegistroUsuario.class);
        startActivity(siguiente);
    }

    public void reproducir(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        System.out.println("Desues de la direccion");
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<byte[]> call = postService.obtenerCancion();
        System.out.println("Despues de post");
        call.enqueue(new Callback<byte[]>() {
            @Override
            public void onResponse(Call<byte[]> call, Response<byte[]> response) {
                Log.e("Respuesta","hey1");
                try {
                    Log.e("Respuesta","hey1");
                    byte[] arreglo=response.body();
                    ByteArrayMediaDataSource prueba = new ByteArrayMediaDataSource(arreglo);
                    mp.setDataSource(prueba);
                    mp.prepare();
                    mp.start();

                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<byte[]> call, Throwable t) {
            }
        });


    }

    private void getLogin() {
        String name= String.valueOf(nombreUsuario.getText());
        String cont= String.valueOf(contraseña.getText());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<ResponseService> call = postService.GetLogin(name,cont);

        call.enqueue(new Callback<ResponseService>() {
            @Override
            public void onResponse(Call<ResponseService> call, Response<ResponseService> response) {
                try {
                   user = response.body();

                    menuPrincipal(user.getTipo());
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseService> call, Throwable t) {
            }
        });
    }

    public void menuPrincipal(String tipo){
        if(tipo.equalsIgnoreCase("creador")){
            Intent siguiente = new Intent(this,InicioCreadorContenido.class);
            startActivity(siguiente);
        }else{
            Intent siguiente = new Intent(this,MenuPrincipal.class);
            startActivity(siguiente);
        }
    }
}
