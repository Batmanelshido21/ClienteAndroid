package com.example.clientestreaming;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.AbstractMap;
import java.util.List;

public interface IServicioLogin {

    String API_ROUTE="api/cuenta/login";
    @GET(API_ROUTE)
    Call  <ResponseService> GetLogin(
            @Query("nombreUsuario") String username,
            @Query("contraseña") String password
    );

    @POST("api/cuenta/registroCuenta")
    Call<ResponseService> Post(
            @Body ResponseService cuentaUsuario
    );

    @POST("api/cuenta/registroArtista")
    Call<Artista> PostArtista(
            @Body Artista artista
    );

    @GET("api/cuenta/reproducirAudio")
    Call<Audio> obtenerCancion(
            @Query("nombreCancion")String nombre);

    @POST("api/cuenta/registroAlbum")
    Call<Album> PostAlbum(
            @Body Album album
    );

    @POST("api/cuenta/registroCancion")
    Call<Cancion> PostCancion(
            @Body Cancion cancion
    );
}
