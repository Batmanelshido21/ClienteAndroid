package com.example.clientestreaming;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface IServicioLogin {
    String API_ROUTE="api/cuenta";
    @GET(API_ROUTE)
    Call<List<ResponseService>> Get();

}
