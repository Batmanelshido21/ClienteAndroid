package com.example.clientestreaming;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;

public class RegistroUsuario extends AppCompatActivity {

    private Window window;
    ImageButton cancelar;
    ImageView imagen;
    ResponseService user;
    TextView correo;
    TextView nombreU;
    TextView contra;
    CheckBox tipo;
    TextView contra2;
    String imagenBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        this.window=getWindow();
        window.setStatusBarColor(Color.parseColor("#0B0B0B"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43a074")));
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#494949")));
        window.setNavigationBarColor(Color.parseColor("#43a074"));
        cancelar=(ImageButton)findViewById(R.id.botonV);
        imagen=(ImageView)findViewById(R.id.fotoPerfil);
        correo=(TextView)findViewById(R.id.correoElectronicoInput);
        nombreU=(TextView)findViewById(R.id.nombreUsuarioInput);
        contra=(TextView)findViewById(R.id.contrasenaInput);
        tipo=(CheckBox) findViewById(R.id.tipo);
        contra2=(TextView)findViewById(R.id.contraseñaConfirmacion);
    }

    public void cancelar(View view){
        Intent siguiente = new Intent(this,MainActivity.class);
        startActivity(siguiente);
    }

    public void Continuar(View view){

        String contraseña =String.valueOf(contra.getText());
        String cadenaDeConfirmacionParaContraseña =String.valueOf(contra2.getText());
        String nombreDeUsuario = String.valueOf(nombreU.getText());
        String email =String.valueOf(correo.getText());
        String tipoDeUsuario;

        if(validarDatos(email, nombreDeUsuario, contraseña, cadenaDeConfirmacionParaContraseña)){
            if(tipo.isChecked()){
                tipoDeUsuario="creador";
            }else{
                tipoDeUsuario="usuario";
            }
            registroCuenta(email, nombreDeUsuario, contraseña, tipoDeUsuario);
        }else{

            final Toast tag = Toast.makeText(this, "No se pueden dejar campos vacios, las contraseñas deben coincidir, no se pueden llenar campos con espacios y ningún campo debe tener la letra 'ñ'",Toast.LENGTH_LONG);
            tag.show();
            new CountDownTimer(5000, 1000) { public void onTick(long millisUntilFinished) {tag.show();} public void onFinish() {tag.show();} }.start();
        }
    }

    public void cargarFoto(View View){
        Intent foto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        foto.setType("image/");
        startActivityForResult(foto.createChooser(foto,"Seleccione la aplicación"),10);
    }

    public void registroCuenta(String correo, String nombreUsuario, String contraseña, String tipoDeUsuario){

        int id = (int) (Math.random() * 10000) + 1;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);

        Log.e("Contraseña", contraseña);

        ResponseService cuentaUsuario = new ResponseService(id, nombreUsuario, correo, contraseña, tipoDeUsuario, imagenBase64);

        Call<ResponseService> call = postService.Post(cuentaUsuario);

        call.enqueue(new Callback<ResponseService>() {
            @Override
            public void onResponse(Call<ResponseService> call, Response<ResponseService> response) {
                try {
                    user = response.body();
                    Log.e("Se registró correctamente ",user.getNombreUsuario());
                    Log.e(" de correo electronico",user.getCorreoElectronico());
                    menuInicio(user.getTipo());
                } catch (Exception e) {
                    Log.e("Error",e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseService> call, Throwable t) {
            }
        });
    }

    public void menuInicio(String tipo){
        if(tipo.equalsIgnoreCase("creador")){
            Intent siguiente = new Intent(this,InicioCreadorContenido.class);
            startActivity(siguiente);
        }else{
            Intent siguiente = new Intent(this,MenuPrincipal.class);
            startActivity(siguiente);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path = data.getData();
            imagen.setImageURI(path);
            String s = obtenerRutaDeImagen(path);
            ConvertirImagen(s);
        }
    }

    public String obtenerRutaDeImagen(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation") Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor .getColumnIndexOrThrow(MediaStore.Images.Media.DATA); cursor.moveToFirst(); return cursor.getString(column_index);
    }

    public void ConvertirImagen(String ruta){
        try {

            File file = new File(ruta);
            byte[] bytesArray = new byte[(int) file.length()];
            FileInputStream archivo = new FileInputStream(file);
            archivo.read(bytesArray);

            imagenBase64 = Base64.encodeToString(bytesArray, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validarDatos(String correo, String nombreUsuario, String contraseña, String cadenaDeConfirmacionParaContraseña){

        String email = correo.replaceAll("\\s", "");
        String userName = nombreUsuario.replaceAll("\\s", "");
        String password = contraseña.replaceAll("\\s", "");
        String passwordConfirm = cadenaDeConfirmacionParaContraseña.replaceAll("\\s", "");

        if((!userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty())
                && (contraseña.equals(cadenaDeConfirmacionParaContraseña))
                && (!contraseña.contains(" "))
                && (!contraseña.contains("ñ") && !nombreUsuario.contains("ñ") && !correo.contains("ñ"))){
            return true;
        }else {
            return false;
        }
    }
}
