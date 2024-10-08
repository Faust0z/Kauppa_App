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

import com.example.kauppa_emp.activities.MasInfoIngresos;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;

import java.util.ArrayList;

public class CustomAdapterIngresos extends RecyclerView.Adapter<CustomAdapterIngresos.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Ingresos> ingresos;

    public CustomAdapterIngresos(Activity activity, Context context, ArrayList<Ingresos> ingresos){
        this.activity = activity;
        this.context = context;
        this.ingresos = ingresos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filas_ingresos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Ingresos ingresoActual = ingresos.get(position);
        holder.textView_Id_FilaIngresos.setText(ingresoActual.getId());
        holder.textView_Tipo_FilaIngresos.setText(TiposMovimiento.getTipoById(ingresoActual.getIdTipo()));
        holder.textView_Fecha_FilaIngresos.setText(ingresoActual.getFecha());
        holder.textView_Monto_FilaIngresos.setText(ingresoActual.getMonto());

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoIngresos.class);
                intent.putExtra("movId", ingresoActual.getId());
                intent.putExtra("movNomCliente", ingresoActual.getNomCliente());
                intent.putExtra("movIdTipos", ingresoActual.getIdTipo());
                intent.putExtra("movFecha", ingresoActual.getFecha());
                intent.putExtra("movMonto", ingresoActual.getMonto());
                intent.putExtra("movDetalle", ingresoActual.getDetalle());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Id_FilaIngresos, textView_Tipo_FilaIngresos, textView_Fecha_FilaIngresos, textView_Monto_FilaIngresos;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Id_FilaIngresos = itemView.findViewById(R.id.textView_Id_FilaIngresos);
            textView_Tipo_FilaIngresos = itemView.findViewById(R.id.textView_Tipo_FilaIngresos);
            textView_Fecha_FilaIngresos = itemView.findViewById(R.id.textView_Fecha_FilaIngresos);
            textView_Monto_FilaIngresos = itemView.findViewById(R.id.textView_Monto_FilaIngresos);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilasIngresos);
        }
    }
}
