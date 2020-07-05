package com.example.clientestreaming;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface IServicioLogin {

    String API_ROUTE="api/cuenta/login";
    @GET(API_ROUTE)
    Call<ResponseService> GetLogin(
            @Query("nombreUsuario") String username,
            @Query("contraseña") String password
    );

}
