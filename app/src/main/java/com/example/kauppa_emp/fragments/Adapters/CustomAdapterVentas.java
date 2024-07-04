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

import com.example.kauppa_emp.MasInfoVentas;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.dataObjects.Ingresos;

import java.util.ArrayList;

public class CustomAdapterVentas extends RecyclerView.Adapter<CustomAdapterVentas.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Ingresos> ingresos;

    public CustomAdapterVentas(Activity activity, Context context, ArrayList<Ingresos> ingresos){
        this.activity = activity;
        this.context = context;
        this.ingresos = ingresos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_ventas, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Ingresos ingresoActual = ingresos.get(position);
        holder.filaVentas_id_txt.setText(ingresoActual.getId());
        holder.filaVentas_fecha_txt.setText(ingresoActual.getFecha());
        holder.filaVentas_monto_txt.setText(ingresoActual.getMonto());

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoVentas.class);
                intent.putExtra("movId", ingresoActual.getId());
                intent.putExtra("movFecha", ingresoActual.getFecha());
                intent.putExtra("movMonto", ingresoActual.getMonto());
                intent.putExtra("movDetalle", ingresoActual.getDetalle());
                intent.putExtra("movIdTipos", ingresoActual.getIdTipo());
                intent.putExtra("movNomCliente", ingresoActual.getIdTipo());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filaVentas_id_txt, filaVentas_fecha_txt, filaVentas_monto_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filaVentas_id_txt = itemView.findViewById(R.id.filaVentas_id_txt);
            filaVentas_fecha_txt = itemView.findViewById(R.id.filaVentas_fecha_txt);
            filaVentas_monto_txt = itemView.findViewById(R.id.filaVentas_monto_txt);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilaVentas);
        }
    }
}
