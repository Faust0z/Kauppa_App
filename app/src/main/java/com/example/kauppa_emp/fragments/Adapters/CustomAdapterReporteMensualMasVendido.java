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

public class CustomAdapterReporteMensualMasVendido extends RecyclerView.Adapter<CustomAdapterReporteMensualMasVendido.ViewHolder> {

    private final List<ReporteMensualFragment.LineaProductoMasVendido> productos;

    public CustomAdapterReporteMensualMasVendido(List<ReporteMensualFragment.LineaProductoMasVendido> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_mas_vendido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReporteMensualFragment.LineaProductoMasVendido producto = productos.get(position);
        holder.btnPuesto.setText(String.valueOf(producto.getPuesto()));
        holder.textNombre.setText(producto.getNombre());
        holder.textUnidades.setText(String.format("%d Unidades", producto.getUnidades()));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btnPuesto;
        TextView textNombre, textUnidades;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPuesto = itemView.findViewById(R.id.btnPuestoLineaMasVendido);
            textNombre = itemView.findViewById(R.id.textProductoLineaMasVendido);
            textUnidades = itemView.findViewById(R.id.textUnidadesLineaMasVendido);
        }
    }
}