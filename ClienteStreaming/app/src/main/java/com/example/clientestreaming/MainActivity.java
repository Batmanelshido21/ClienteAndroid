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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;


public class MainActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView contraseña;
    private Button botonRegistrar;
    private Button botonIngresar;
    private Window window;
    ResponseService user;
    MediaPlayer mp;
    Audio audio;

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
        mp = new MediaPlayer();
        //mp = MediaPlayer.create(this,R.raw.hail);
    }

    public void ingresar(View view){
        getLogin();
    }

    public void registrarse(View view){
        Intent siguiente = new Intent(this,RegistroUsuario.class);
        startActivity(siguiente);
    }

    private void getLogin() {

        String nombreDeUsuario = String.valueOf(nombreUsuario.getText());
        String contraseñaDeUsuario = String.valueOf(contraseña.getText());

        if(validarDatos(nombreDeUsuario, contraseñaDeUsuario)){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.15:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IServicioLogin postService = retrofit.create(IServicioLogin.class);

            Call<ResponseService> call = postService.GetLogin(nombreDeUsuario, contraseñaDeUsuario);

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
                   errorEjecucion();
                }
            });

        }else{
            Toast.makeText(this, "No se pueden dejar campos vacios ni llenar con espacios",Toast.LENGTH_LONG).show();
        }
    }

    public void errorEjecucion(){
        Toast.makeText(this, "Error de autenticacion",Toast.LENGTH_LONG).show();
    }
    public void menuPrincipal(String tipo){
        if(tipo.equalsIgnoreCase("creador")){
            Intent siguiente = new Intent(this,InicioCreadorContenido.class);
            siguiente.putExtra("tipo",user.getTipo());
            siguiente.putExtra("id",user.getId());
            startActivity(siguiente);
        }else{
            Intent siguiente = new Intent(this,MenuPrincipal.class);
            siguiente.putExtra("tipo",user.getTipo());
            siguiente.putExtra("id",user.getId());
            startActivity(siguiente);
        }
    }

    public boolean validarDatos(String nombreUsuario, String contraseña){

        String nombre = nombreUsuario.replaceAll("\\s", "");
        String contraseñaDeUsuario = contraseña.replaceAll("\\s", "");

        if(!nombre.isEmpty() && !contraseñaDeUsuario.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
