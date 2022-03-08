package com.example.delivery_java.controllers;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.delivery_java.R;
import com.example.delivery_java.models.Pedido;

import java.util.ArrayList;
import android.widget.TextView;

public class CustomAdapter
        extends RecyclerView.Adapter<CustomAdapter.ViewHolderDatos>
        implements View.OnClickListener {

    ArrayList<Pedido> listDatos;
    private View.OnClickListener listener;

    public CustomAdapter(ArrayList<Pedido> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarPedidos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public static class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView title;
        TextView estado;
        TextView direccion;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitleCard);
            estado = itemView.findViewById(R.id.textEstado);
            direccion = itemView.findViewById(R.id.textLocation);
        }

        @SuppressLint("SetTextI18n")
        public void asignarPedidos(Pedido pedido) {

            title.setText("Pedido Nº " + pedido.getId().toString());
            estado.setText(pedido.getEstado());
            direccion.setText(pedido.getUsuario().getDireccion());
        }

    }
}