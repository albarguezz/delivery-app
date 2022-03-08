package com.example.delivery_java.interfaces;

import com.example.delivery_java.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioAPI {
    @GET("api/usuarios/")
    Call<List<Usuario>> findUsuarios();

    @GET("api/usuarios/{id}")
    Call<Usuario> findUsuarioById(@Path("id") String id);

    @POST("api/usuarios/")
    Call<Usuario> addUsuario(@Body Usuario newUser);
}
