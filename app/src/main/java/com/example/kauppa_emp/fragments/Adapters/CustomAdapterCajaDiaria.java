package com.example.kauppa_emp.fragments.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;

import java.util.ArrayList;

public class CustomAdapterCajaDiaria extends RecyclerView.Adapter<CustomAdapterCajaDiaria.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList movimiento_id, movimiento_tipo, movimiento_fecha, movimiento_monto;

    public CustomAdapterCajaDiaria(Context context, ArrayList movimiento_id, ArrayList
            movimiento_tipo, ArrayList movimiento_fecha, ArrayList movimiento_monto){
        this.context = context;
        this.movimiento_id = movimiento_id;
        this.movimiento_tipo = movimiento_tipo;
        this.movimiento_fecha = movimiento_fecha;
        this.movimiento_monto = movimiento_monto;
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
        holder.cajadiaria_entradasalida_txt.setText(String.valueOf(movimiento_tipo.get(position)));
        holder.cajadiaria_fecha_txt.setText(String.valueOf(movimiento_fecha.get(position)));
        holder.cajadiaria_monto_txt.setText(String.valueOf(movimiento_monto.get(position)));

        /*
        // Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(movimiento_id.get(currentPosition)));
                    intent.putExtra("tipo", String.valueOf(movimiento_tipo.get(currentPosition)));
                    intent.putExtra("fecha", String.valueOf(movimiento_fecha.get(currentPosition)));
                    intent.putExtra("monto", String.valueOf(movimiento_monto.get(currentPosition)));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });

         */
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
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
