package com.example.kauppa_emp.fragments.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.ReporteMensualFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CustomAdapterReporteMensualMayorGanancia extends RecyclerView.Adapter<CustomAdapterReporteMensualMayorGanancia.ViewHolder> {

    private final List<ReporteMensualFragment.LineaProductoMayorGanancia> productos;

    public CustomAdapterReporteMensualMayorGanancia(List<ReporteMensualFragment.LineaProductoMayorGanancia> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mayor_ganancia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReporteMensualFragment.LineaProductoMayorGanancia producto = productos.get(position);
        holder.btnPuesto.setText(String.valueOf(producto.getPuesto()));
        holder.textNombre.setText(producto.getNombre());
        holder.textGanancia.setText(String.format("$%.0f", producto.getGanancia()));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btnPuesto;
        TextView textNombre, textGanancia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPuesto = itemView.findViewById(R.id.btnPuestoLineaMayorGanancia);
            textNombre = itemView.findViewById(R.id.textProductoLineaMayorGanancia);
            textGanancia = itemView.findViewById(R.id.textGananciaMayorGanancia);
        }
    }
}
