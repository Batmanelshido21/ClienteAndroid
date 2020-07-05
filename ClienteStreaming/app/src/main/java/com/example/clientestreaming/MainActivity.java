package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView nombreUsuario;
    private TextView contraseña;
    private Button botonRegistrar;
    private Button botonIngresar;
    private Window window;
    private static IServicioLogin API_SERVICE;

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
        new Login().execute();
        //cuentas();
        //Intent siguiente = new Intent(this,MenuPrincipal.class);
        //startActivity(siguiente);
    }

    public void registrarse(View view){
        Intent siguiente = new Intent(this,RegistroUsuario.class);
        startActivity(siguiente);
    }

    public void cuentas(){
        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        String baseUrl = "http://192.168.0.15:5001/";

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- usamos el log level
                    .build();
            API_SERVICE = retrofit.create(IServicioLogin.class);

            Call<List<ResponseService>> response = API_SERVICE.Get();

            try {
                for(ResponseService user : response.execute().body()) {
                    Log.e("Respuesta",user.getNombreUsuario());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static class Login extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            final String url="http://192.168.0.15:5001/";
            //127.0.0.1
            //192.168.0.15
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            IServicioLogin servicioLogin = retrofit.create(IServicioLogin.class);
            Call<List<ResponseService>> response = servicioLogin.Get();

            try {
                for(ResponseService user : response.execute().body()) {
                    Log.e("Respuesta",user.getNombreUsuario());
                }
            } catch (IOException e) {
               Log.e("Error",e.getMessage());
            }

            return null;
        }
    }
}
