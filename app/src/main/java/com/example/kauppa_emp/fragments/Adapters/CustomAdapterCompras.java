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

import com.example.kauppa_emp.MasInfoCompras;
import com.example.kauppa_emp.R;

import java.util.ArrayList;

public class CustomAdapterCompras extends RecyclerView.Adapter<CustomAdapterCompras.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> movimiento_id, movimiento_fecha, movimiento_detalle, movimiento_monto, movimiento_IdPedidos, movimiento_IdTipo, movimiento_NomCliente;

    public CustomAdapterCompras(Activity activity, Context context, ArrayList<String> movimiento_id, ArrayList<String> movimiento_fecha,
                                ArrayList<String> movimiento_detalle, ArrayList<String> movimiento_monto, ArrayList<String>
                                           movimiento_IDpedidos, ArrayList<String> movimiento_tipo,
                                ArrayList<String> movimiento_NomCliente){
        this.activity = activity;
        this.context = context;
        this.movimiento_id = movimiento_id;
        this.movimiento_fecha = movimiento_fecha;
        this.movimiento_detalle = movimiento_detalle;
        this.movimiento_monto = movimiento_monto;
        this.movimiento_IdPedidos = movimiento_IDpedidos;
        this.movimiento_IdTipo = movimiento_tipo;
        this.movimiento_NomCliente = movimiento_NomCliente;
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
        // En estos Holders tenemos que poner los datos que queremos mostrar al abrir los detalles de un elemento
        holder.filaCompras_id_txt.setText(String.valueOf(movimiento_id.get(position)));
        holder.filaCompras_fecha_txt.setText(String.valueOf(movimiento_fecha.get(position)));
        holder.filaCompras_monto_txt.setText(String.valueOf(movimiento_monto.get(position)));

        // onClickListener para cada fila
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    //Indicar qué datos le vamos a pasar al intent de "Ver más info"
                    Intent intent = new Intent(context, MasInfoCompras.class);
                    intent.putExtra("id", String.valueOf(movimiento_id.get(currentPosition)));
                    intent.putExtra("fecha", String.valueOf(movimiento_fecha.get(currentPosition)));
                    intent.putExtra("detalle", String.valueOf(movimiento_detalle.get(currentPosition)));
                    intent.putExtra("monto", String.valueOf(movimiento_monto.get(currentPosition)));
                    intent.putExtra("IdPedidos", String.valueOf(movimiento_IdPedidos.get(currentPosition)));
                    intent.putExtra("IdTipos", String.valueOf(movimiento_IdTipo.get(currentPosition)));
                    intent.putExtra("nombreCliente", String.valueOf(movimiento_NomCliente.get(currentPosition)));
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
