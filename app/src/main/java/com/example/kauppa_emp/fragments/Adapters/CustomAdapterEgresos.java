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

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.MasInfoEgresos;
import com.example.kauppa_emp.database.dataObjects.Egresos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;

import java.util.ArrayList;

public class CustomAdapterEgresos extends RecyclerView.Adapter<CustomAdapterEgresos.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Egresos> egresos;

    public CustomAdapterEgresos(Activity activity, Context context, ArrayList<Egresos> egresos){
        this.activity = activity;
        this.context = context;
        this.egresos = egresos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filas_egresos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Egresos egresoActual = egresos.get(position);
        holder.textView_Id_FilaEgresos.setText(egresoActual.getId());
        holder.textView_Fecha_FilaEgresos.setText(egresoActual.getFecha());
        holder.textView_Monto_FilaEgresos.setText(egresoActual.getMonto());
        holder.textView_Tipo_FilaEgresos.setText(TiposMovimiento.getTipoEgre(egresoActual.getIdTipo()));

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            // Todo: modificar esto
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoEgresos.class);
                intent.putExtra("movId", egresoActual.getId());
                intent.putExtra("movNomProv", egresoActual.getNomProv());
                intent.putExtra("movIdTipos", egresoActual.getIdTipo());
                intent.putExtra("movMonto", egresoActual.getMonto());
                intent.putExtra("movFecha", egresoActual.getFecha());
                intent.putExtra("movDetalle", egresoActual.getDetalle());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return egresos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Id_FilaEgresos, textView_Fecha_FilaEgresos, textView_Monto_FilaEgresos, textView_Tipo_FilaEgresos;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Id_FilaEgresos = itemView.findViewById(R.id.textView_Id_FilaEgresos);
            textView_Fecha_FilaEgresos = itemView.findViewById(R.id.textView_Fecha_FilaEgresos);
            textView_Monto_FilaEgresos = itemView.findViewById(R.id.textView_Monto_FilaEgresos);
            textView_Tipo_FilaEgresos = itemView.findViewById(R.id.textView_Tipo_FilaEgresos);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilaCompras);
        }
    }
}
