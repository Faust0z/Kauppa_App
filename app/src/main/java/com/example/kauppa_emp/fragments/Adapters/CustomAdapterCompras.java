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
import com.example.kauppa_emp.fragments.dataObjects.Egresos;

import java.util.ArrayList;

public class CustomAdapterCompras extends RecyclerView.Adapter<CustomAdapterCompras.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Egresos> egresos;

    public CustomAdapterCompras(Activity activity, Context context, ArrayList<Egresos> egresos){
        this.activity = activity;
        this.context = context;
        this.egresos = egresos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_compras, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Egresos egresoActual = egresos.get(position);
        holder.filaCompras_id_txt.setText(egresoActual.getId());
        holder.filaCompras_fecha_txt.setText(egresoActual.getFecha());
        holder.filaCompras_monto_txt.setText(egresoActual.getMonto());

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoCajaDiaria.class);
                intent.putExtra("movId", egresoActual.getId());
                intent.putExtra("movFecha", egresoActual.getFecha());
                intent.putExtra("movDetalle", egresoActual.getDetalle());
                intent.putExtra("movMonto", egresoActual.getMonto());
                intent.putExtra("movIdTipos", egresoActual.getIdTipo());
                intent.putExtra("movNomCliente", egresoActual.getNomCliente());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return egresos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filaCompras_id_txt, filaCompras_fecha_txt, filaCompras_monto_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filaCompras_id_txt = itemView.findViewById(R.id.filaCompras_id_txt);
            filaCompras_fecha_txt = itemView.findViewById(R.id.filaCompras_fecha_txt);
            filaCompras_monto_txt = itemView.findViewById(R.id.filaCompras_monto_txt);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilaCompras);
        }
    }
}
