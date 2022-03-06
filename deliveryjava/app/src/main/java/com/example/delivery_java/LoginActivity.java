package com.example.delivery_java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.delivery_java.databinding.ActivityDetailBinding;
import com.example.delivery_java.databinding.ActivityLoginBinding;
import com.example.delivery_java.interfaces.PedidoAPI;
import com.example.delivery_java.interfaces.UsuarioAPI;
import com.example.delivery_java.models.Pedido;
import com.example.delivery_java.models.Usuario;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogin.setOnClickListener(v -> {
            findUsuarios();
        });

        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void findUsuarios() {
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://192.168.1.38:8000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        UsuarioAPI usuarioApi2 = retrofit2.create(UsuarioAPI.class);
        Call<List<Usuario>> call2 = usuarioApi2.findUsuarios();
        call2.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                try {
                    if (response.isSuccessful()) {
                        List<Usuario> us = response.body();
                        assert us != null;
                        String email = binding.passEdit.getText().toString();
                        String pass = binding.passEdit.getText().toString();
                        for (Usuario user: us) {
                            if (user.getPassword().equals(pass) &&
                                    user.getEmail().equals(email) &&
                                    user.getRol().equals("repartidor")) {

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "No existe usuario con esos datos!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
