package com.example.delivery_java;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
            if(!TextUtils.isEmpty(binding.nameEdit.getText()) && !TextUtils.isEmpty(binding.usernameEdit.getText()) && !TextUtils.isEmpty(binding.passEdit.getText())) {
                Usuario userNew = new Usuario();
                userNew.setNombre(binding.nameEdit.getText().toString());
                userNew.setEmail(binding.usernameEdit.getText().toString());
                userNew.setPassword(binding.passEdit.getText().toString().trim());
                userNew.setRol("repartidor");
                addUsuarios(userNew);
            } else {
                Toast.makeText(RegisterActivity.this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Funcion que hace llamada de post para registrar un nuevo usuario
     */
    public void addUsuarios(Usuario new_user) {
        Retrofit retrofitUsers = new Retrofit.Builder().baseUrl("http://192.168.1.38:8000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        UsuarioAPI usuarioApi = retrofitUsers.create(UsuarioAPI.class);
        Call<Usuario> call = usuarioApi.addUsuario(new_user);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception ex) {
                    Toast.makeText(RegisterActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
