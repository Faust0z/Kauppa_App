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

import com.example.kauppa_emp.activities.MasInfoCajaDiaria;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.database.dataObjects.Movimientos;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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
        View view = inflater.inflate(R.layout.filas_cajadiaria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimientos movimientoActual = movimientos.get(position);

        NumberFormat format = NumberFormat.getInstance(new Locale("es", "ES"));
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);

        holder.textView_Id_FilaCajaDiaria.setText(String.valueOf(movimientos.size() - position));
        holder.textView_Fecha_FilaCajaDiaria.setText(movimientoActual.getFecha());
        holder.textView_Monto_FilaCajaDiaria.setText(format.format(Double.parseDouble(movimientoActual.getMonto())));
        holder.textView_DatoExtra_FilaCajaDiaria.setText(movimientoActual.getDatoExtra());
        if (movimientoActual.isPedido()){
            holder.textView_Tipo_FilaCajaDiaria.setText("Pedido");
        }else{
            holder.textView_Tipo_FilaCajaDiaria.setText(TiposMovimiento.getTipoMov(movimientoActual.getIdTipo()));
        }

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoCajaDiaria.class);
                intent.putExtra("movId", movimientoActual.getId());
                intent.putExtra("movFecha", movimientoActual.getFecha());
                intent.putExtra("movMonto", movimientoActual.getMonto());
                intent.putExtra("movDetalle", movimientoActual.getDetalle());
                intent.putExtra("movIdTipos", movimientoActual.getIdTipo());
                intent.putExtra("esPedido", movimientoActual.isPedido());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Id_FilaCajaDiaria, textView_Fecha_FilaCajaDiaria, textView_Monto_FilaCajaDiaria, textView_Tipo_FilaCajaDiaria, textView_DatoExtra_FilaCajaDiaria;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Id_FilaCajaDiaria = itemView.findViewById(R.id.textView_Id_FilaCajaDiaria);
            textView_Fecha_FilaCajaDiaria = itemView.findViewById(R.id.textView_Fecha_FilaCajaDiaria);
            textView_Monto_FilaCajaDiaria = itemView.findViewById(R.id.textView_Monto_FilaCajaDiaria);
            textView_Tipo_FilaCajaDiaria = itemView.findViewById(R.id.textView_Tipo_FilaCajaDiaria);
            textView_DatoExtra_FilaCajaDiaria = itemView.findViewById(R.id.textView_DatoExtra_FilaCajaDiaria);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilaCajaDiaria);
        }
    }
}
