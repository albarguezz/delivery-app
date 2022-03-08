package com.example.delivery_java.interfaces;

import com.example.delivery_java.models.Pedido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface PedidoAPI {

    @PATCH("api/pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") String id, @Body Pedido pedido);

    @GET("api/pedidos/")
    Call<List<Pedido>> findPedidos();

    @GET("api/pedidos/{id}")
    Call<Pedido> findPedidoById(@Path("id") String id);
}
