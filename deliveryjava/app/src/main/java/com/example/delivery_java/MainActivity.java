package com.example.delivery_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.delivery_java.controllers.CustomAdapter;
import com.example.delivery_java.databinding.ActivityMainBinding;
import com.example.delivery_java.interfaces.PedidoAPI;
import com.example.delivery_java.models.Pedido;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ArrayList<Pedido> listDatos;
    RecyclerView recycler;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        findPedidos();
    }

    public void findPedidos() {
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://192.168.1.38:8000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        PedidoAPI pedidoApi2 = retrofit2.create(PedidoAPI.class);
        Call<List<Pedido>> call2 = pedidoApi2.findPedidos();
        call2.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pedido>> call, @NonNull Response<List<Pedido>> response) {
                try {
                    if (response.isSuccessful()) {
                        List<Pedido> ps = response.body();
                        List<Pedido> pedidosPendientes = new ArrayList<>();
                        List<Pedido> pedidosRealizados = new ArrayList<>();
                        addRecyclerViewList(ps);

                        assert ps != null;
                        binding.totalPedidosNumber.setText(String.valueOf(ps.size()));
                        for (Pedido pedido: ps) {
                            String estado = pedido.getEstado();

                            if (estado.equals("pendiente")) {
                                pedidosPendientes.add(pedido);
                            } else {
                                pedidosRealizados.add(pedido);
                            }
                        }
                        binding.pedidosCompletadosNumber.setText(String.valueOf(pedidosRealizados.size()));
                        binding.pedidosPendientesNumber.setText(String.valueOf(pedidosPendientes.size()));
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pedido>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addRecyclerViewList(List<Pedido> pedidosResponse) {
        recycler = findViewById(R.id.recycleView);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        listDatos = listToArrayList(pedidosResponse);

        CustomAdapter adapter = new CustomAdapter(listDatos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("pedido", listDatos.get(recycler.getChildAdapterPosition(view)));
                startActivity(intent);
            }
        });
        recycler.setAdapter(adapter);
    }

    public static ArrayList<Pedido> listToArrayList(List<Pedido> myList) {
        ArrayList<Pedido> arl = new ArrayList<Pedido>();
        for (Pedido object : myList) {
            arl.add((Pedido) object);
        }
        return arl;

    }
}