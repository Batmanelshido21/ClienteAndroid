package com.example.clientestreaming;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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
        String password=String.valueOf(contra.getText());
        String password2=String.valueOf(contra2.getText());
        if(password.equals(password2)){
            registroCuenta();
        }
    }

    public void cargarFoto(View View){
        Intent foto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        foto.setType("image/");
        startActivityForResult(foto.createChooser(foto,"Seleccione la aplicación"),10);
    }

    public void registroCuenta(){
        String name= String.valueOf(nombreU.getText());
        String password=String.valueOf(contra.getText());
        String email=String.valueOf(correo.getText());
        String tipo2;
        if(tipo.isChecked()){
            tipo2="creador";
        }else{
            tipo2="usuario";
        }
        int id = (int) (Math.random() * 10000) + 1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.15:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioLogin postService = retrofit.create(IServicioLogin.class);
        Call<ResponseService> call = postService.Post(id,name,email,password,tipo2);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path=data.getData();
            imagen.setImageURI(path);
        }
    }

}
