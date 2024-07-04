package com.example.kauppa_emp.fragments.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.MasInfoCajaDiaria;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.dataObjects.Movimientos;

import java.util.ArrayList;

public class CustomAdapterCajaDiaria extends RecyclerView.Adapter<CustomAdapterCajaDiaria.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Movimientos> movimientos;

    public CustomAdapterCajaDiaria(Activity activity, Context context, ArrayList<Movimientos> movimientos) {
        this.activity = activity;
        this.context = context;
        this.movimientos = movimientos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_cajadiaria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimientos movimientoActual = movimientos.get(position);
        holder.cajadiaria_id_txt.setText(movimientoActual.getId());
        holder.cajadiaria_entradasalida_txt.setText(movimientoActual.getIdTipo());
        holder.cajadiaria_fecha_txt.setText(movimientoActual.getFecha());
        holder.cajadiaria_monto_txt.setText(movimientoActual.getMonto());

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoCajaDiaria.class);
                intent.putExtra("movId", movimientoActual.getId());
                intent.putExtra("movFecha", movimientoActual.getFecha());
                intent.putExtra("movMonto", movimientoActual.getMonto());
                intent.putExtra("movDetalle", movimientoActual.getDetalle());
                intent.putExtra("movIdTipos", movimientoActual.getIdTipo());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cajadiaria_id_txt, cajadiaria_entradasalida_txt, cajadiaria_fecha_txt, cajadiaria_monto_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cajadiaria_id_txt = itemView.findViewById(R.id.cajadiaria_id_txt);
            cajadiaria_entradasalida_txt = itemView.findViewById(R.id.cajadiaria_entradasalida_txt);
            cajadiaria_fecha_txt = itemView.findViewById(R.id.cajadiaria_fecha_txt);
            cajadiaria_monto_txt = itemView.findViewById(R.id.cajadiaria_monto_txt);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilaCajaDiaria);
        }
    }
}
