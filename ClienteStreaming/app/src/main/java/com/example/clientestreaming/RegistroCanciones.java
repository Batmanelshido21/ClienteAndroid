package com.example.clientestreaming;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.common.io.ByteStreams;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


import static android.provider.MediaStore.Audio.*;

public class RegistroCanciones extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private Button seleccionCancion;
    private MediaPlayer mediaPlayer= new MediaPlayer();
    private Button agregar;
    private Button finalizar;
    Cancion cancion;
    private TextView nombre;
    private TextView genero;
    private TextView duracion;
    String base64File;
    String s;
    int idAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_canciones);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton)findViewById(R.id.botonV);
        seleccionCancion=(Button)findViewById(R.id.seleccionCancion);
        agregar=(Button)findViewById(R.id.botonAgregarCancion);
        finalizar=(Button)findViewById(R.id.botonTerminarProceso);
        nombre=(TextView)findViewById(R.id.nombreCancionInput);
        genero=(TextView)findViewById(R.id.generoCancionInput);
        duracion=(TextView)findViewById(R.id.duracionInput);

        idAlbum = getIntent().getIntExtra("idAlbum", 0);
    }

    public void volver(View view){
        Intent siguiente = new Intent(this,RegistrarAlbum.class);
        startActivity(siguiente);
    }

    public void cargarAudio(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path = data.getData();
            Log.e("Path",path.toString());
            Log.e("Data",data.toString());
            s =obtenerRutaDeImagen(path);
            Log.e("Ruta",s);
            ConvertirImagen(s);
        }
    }

    public String obtenerRutaDeImagen(Uri uri) {
        String[] projection = { MediaStore.Audio.Media.DATA};
        @SuppressWarnings("deprecation") Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA); cursor.moveToFirst(); return cursor.getString(column_index);
    }

    public void ConvertirImagen(String ruta){
        try {
            File file = new File(ruta);
            Log.e("File",file.getAbsolutePath());
            byte[] bytesArray = new byte[(int) file.length()];
            Log.e("Bytes",bytesArray.toString());
            FileInputStream archivo = new FileInputStream(file);
            archivo.read(bytesArray);
            base64File = Base64.encodeToString(bytesArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void menuPrincipal(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void agregarNuevaCancion(View view){
        registrarCancion();
    }

    public void registrarCancion(){

        String name2= String.valueOf(nombre.getText());
        String genero1= String.valueOf(genero.getText());
        String durac= String.valueOf(duracion.getText());

        int id = (int) (Math.random() * 10000) + 1;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.66:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);

        Cancion audio = new Cancion(id,name2,genero1,durac,base64File,0,idAlbum);

        Call<Cancion> call = postService.PostCancion(audio);

        call.enqueue(new Callback<Cancion>() {
            @Override
            public void onResponse(Call<Cancion> call, Response<Cancion> response) {
                try {
                    cancion = response.body();
                    nombre.setText("");
                    genero.setText("");
                    duracion.setText("");
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<Cancion> call, Throwable t) {
            }
        });

        /*if(cancion.getNombre().isEmpty()){
            Toast.makeText(this, "No se pudo registrar la canción", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Canción registrada, puede seguir agregando canciones o regresar a la pantalla principal", Toast.LENGTH_LONG).show();
        }*/
    }
}


/*import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RegistroCanciones extends AppCompatActivity {

    private Window window;
    private ImageButton volver;
    private Button seleccionCancion;
    private MediaPlayer mediaPlayer= new MediaPlayer();
    private Button agregar;
    private Button finalizar;
    Cancion cancion;
    private TextView nombre;
    private TextView genero;
    private TextView duracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_canciones);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        volver=(ImageButton)findViewById(R.id.botonV);
        seleccionCancion=(Button)findViewById(R.id.seleccionCancion);
        agregar=(Button)findViewById(R.id.botonAgregarCancion);
        finalizar=(Button)findViewById(R.id.botonTerminarProceso);
        nombre=(TextView)findViewById(R.id.nombreCancionInput);
        genero=(TextView)findViewById(R.id.generoCancionInput);
        duracion=(TextView)findViewById(R.id.duracionInput);
    }

    public void volver(View view){
        Intent siguiente = new Intent(this,RegistrarAlbum.class);
        startActivity(siguiente);
    }

    public void cargarAudio(View view){
        Intent audio = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        audio.setType("audio/");
        startActivityForResult(audio.createChooser(audio,"Seleccione la aplicación"),10);
    }

    public void menuPrincipal(View view){
        Intent siguiente = new Intent(this,InicioCreadorContenido.class);
        startActivity(siguiente);
    }

    public void agregarNuevaCancion(View view){
        registrarCancion();
    }

    public void registrarCancion(){

        String nombreDeCancion = String.valueOf(nombre.getText());
        String generoMusical = String.valueOf(genero.getText());
        String duracionDeCancion = String.valueOf(duracion.getText());

        if(validarDatos(nombreDeCancion, generoMusical, duracionDeCancion)){

            int id = (int) (Math.random() * 10000) + 1;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.15:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IServicioLogin postService = retrofit.create(IServicioLogin.class);

            Call<Cancion> call = postService.PostCancion(id, nombreDeCancion, generoMusical, duracionDeCancion);

            call.enqueue(new Callback<Cancion>() {
                @Override
                public void onResponse(Call<Cancion> call, Response<Cancion> response) {
                    try {
                        cancion = response.body();
                        Log.e("Respuesta", cancion.getNombre());
                        nombre.setText("");
                        genero.setText("");
                        duracion.setText("");
                    } catch (Exception e) {
                        Log.e("Error",e.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<Cancion> call, Throwable t) {
                }
            });
        }else{

            Toast.makeText(this, "No se pueden dejar campos vacios, llenar con espacios y la duración debe ser un número decimal",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getApplicationContext(),path);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }

    public boolean validarDatos(String nombreCancion, String genero, String duracion){

        String nombre = nombreCancion.replaceAll("\\s", "");
        String generoMusical = genero.replaceAll("\\s", "");
        String duracionDeCancion = genero.replaceAll("\\s", "");

        if((!nombre.isEmpty() && !generoMusical.isEmpty() && !duracionDeCancion.isEmpty()) && (verificarSiEsNumero(duracion))){
            return true;
        }else{
            return false;
        }
    }

    public boolean verificarSiEsNumero(String cadena) {

        return cadena.matches("-?\\d+(\\.\\d+)?");
    }
}*/
