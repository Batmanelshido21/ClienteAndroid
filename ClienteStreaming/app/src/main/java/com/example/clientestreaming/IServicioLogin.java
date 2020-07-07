package com.example.clientestreaming;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.AbstractMap;
import java.util.List;

public interface IServicioLogin {

    String API_ROUTE="api/cuenta/login";
    @GET(API_ROUTE)
    Call<ResponseService> GetLogin(
            @Query("nombreUsuario") String username,
            @Query("contraseña") String password
    );

    @POST("api/cuenta/registroCuenta")
    Call<ResponseService> Post(
            @Query("id") int id,
            @Query("nombreUsuario") String nombreUsuario,
            @Query("correoElectronico") String correoElectronico,
            @Query("contraseña") String contraseña,
            @Query("tipo") String tipo
    );

    @POST("api/cuenta/registroArtista")
    Call<Artista> PostArtista(
            @Query("id") int id,
            @Query("nombreArtistico") String nombreArtistico,
            @Query("descripcion") String descripcion
    );

    @GET("api/cuenta/reproducirAudio")
    Call<Audio> obtenerCancion();
}
