package com.example.delivery_java;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.delivery_java.databinding.ActivityDetailBinding;
import com.example.delivery_java.interfaces.PedidoAPI;
import com.example.delivery_java.models.Pedido;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private PedidoAPI pedidoAPI;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        Pedido  pedidoSelecionado = (Pedido) extras.getSerializable("pedido");

        binding.textTitle.setText("Pedido Nº " + pedidoSelecionado.getId().toString());
        binding.name.setText(pedidoSelecionado.getUsuario().getNombre());
        binding.price.setText((pedidoSelecionado.getPrecioTotal()) + " €.");
        binding.direction.setText(pedidoSelecionado.getUsuario().getDireccion());
        binding.estado.setText(pedidoSelecionado.getEstado());

        binding.btnRute.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("direccion", pedidoSelecionado.getUsuario().getDireccion());
            startActivity(intent);
        });

        binding.btnComplete.setOnClickListener(v -> {
            pedidoSelecionado.setEstado("completado");
            updatePedido(pedidoSelecionado.getId(), pedidoSelecionado);
        });

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Funcion que llama a la api y reliza un post para actualizar el estado del pedido
     * @param id @LatLng id del pedido a actualizar
     */
    private void updatePedido(Long id, Pedido pedido) {
        Retrofit retrofitPedido = new Retrofit.Builder()
                .baseUrl("http://192.168.64.1:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PedidoAPI pedidoAPI = retrofitPedido.create(PedidoAPI.class);
        Call<Pedido> call = pedidoAPI.updatePedido(String.valueOf(id), pedido);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(@NonNull Call<Pedido> call, @NonNull Response<Pedido> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(DetailActivity.this,"Pedido completado!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DetailActivity.this, "Lo sentimos algo ha fallado", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Pedido> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
