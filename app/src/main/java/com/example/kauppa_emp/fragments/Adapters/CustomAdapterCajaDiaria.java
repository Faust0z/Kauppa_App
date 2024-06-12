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
import com.example.kauppa_emp.fragments.CajaDiariaFragment;

import java.util.ArrayList;

public class CustomAdapterCajaDiaria extends RecyclerView.Adapter<CustomAdapterCajaDiaria.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> movimiento_id, movimiento_fecha, movimiento_detalle, movimiento_monto, movimiento_IdPedidos, movimiento_IdTipo;

    public CustomAdapterCajaDiaria(Activity activity, Context context, ArrayList<String> movimiento_id, ArrayList<String> movimiento_fecha,
                                   ArrayList<String> movimiento_detalle, ArrayList<String> movimiento_monto, ArrayList<String>
                                           movimiento_IDpedidos, ArrayList<String> movimiento_tipo){
        this.activity = activity;
        this.context = context;
        this.movimiento_id = movimiento_id;
        this.movimiento_fecha = movimiento_fecha;
        this.movimiento_detalle = movimiento_detalle;
        this.movimiento_monto = movimiento_monto;
        this.movimiento_IdPedidos = movimiento_IDpedidos;
        this.movimiento_IdTipo = movimiento_tipo;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_cajadiaria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.cajadiaria_id_txt.setText(String.valueOf(movimiento_id.get(position)));
        holder.cajadiaria_entradasalida_txt.setText(String.valueOf(movimiento_IdTipo.get(position)));
        holder.cajadiaria_fecha_txt.setText(String.valueOf(movimiento_fecha.get(position)));
        holder.cajadiaria_monto_txt.setText(String.valueOf(movimiento_monto.get(position)));

        // Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, MasInfoCajaDiaria.class);
                    intent.putExtra("id", String.valueOf(movimiento_id.get(currentPosition)));
                    intent.putExtra("fecha", String.valueOf(movimiento_fecha.get(currentPosition)));
                    intent.putExtra("detalle", String.valueOf(movimiento_detalle.get(currentPosition)));
                    intent.putExtra("monto", String.valueOf(movimiento_monto.get(currentPosition)));
                    intent.putExtra("IdPedidos", String.valueOf(movimiento_IdPedidos.get(currentPosition)));
                    intent.putExtra("IdTipos", String.valueOf(movimiento_IdTipo.get(currentPosition)));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimiento_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

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
