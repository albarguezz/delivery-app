package com.example.delivery_java.interfaces;

import com.example.delivery_java.models.Producto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductoAPI {
    @GET("api/productos/")
    Call<List<Producto>> findProductos();

    @GET("api/productos/{id}")
    Call<Producto> findProductoById(@Path("id") String id);
}
