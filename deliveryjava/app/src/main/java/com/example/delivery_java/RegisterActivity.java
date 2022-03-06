package com.example.delivery_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.delivery_java.databinding.ActivityLoginBinding;
import com.example.delivery_java.databinding.ActivityRegisterBinding;
import com.example.delivery_java.interfaces.UsuarioAPI;
import com.example.delivery_java.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
    public void addUsuarios() {
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://127.0.0.1:8000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        UsuarioAPI usuarioApi2 = retrofit2.create(UsuarioAPI.class);
        Call<List<Usuario>> call2 = usuarioApi2.findUsuarios();
        call2.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                try {
                    if (response.isSuccessful()) {
                        List<Usuario> p = response.body();
                        assert p != null;
                    }
                } catch (Exception ex) {
                    Toast.makeText(RegisterActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
