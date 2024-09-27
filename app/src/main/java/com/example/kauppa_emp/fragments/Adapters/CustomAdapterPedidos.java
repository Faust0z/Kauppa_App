package com.example.kauppa_emp.fragments.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.MasInfoPedidos;
import com.example.kauppa_emp.database.dataObjects.Pedidos;

import java.util.ArrayList;

public class CustomAdapterPedidos extends RecyclerView.Adapter<CustomAdapterPedidos.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<Pedidos> pedidos;

    public CustomAdapterPedidos(Activity activity, Context context, ArrayList<Pedidos> pedidos){
        this.activity = activity;
        this.context = context;
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filas_pedidos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Pedidos pedidoActual = pedidos.get(position);
        holder.textView_Id_FilaPedidos.setText(pedidoActual.getId());
        holder.textView_NomClien_FilaPedidos.setText(pedidoActual.getNomCliente());
        holder.textView_FechaEntreg_FilaPedidos.setText(pedidoActual.getFechaEntrega());

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, MasInfoPedidos.class);
                intent.putExtra("movId", pedidoActual.getId());
                intent.putExtra("movFecha", pedidoActual.getFecha());
                intent.putExtra("movFechaEntrega", pedidoActual.getFechaEntrega());
                intent.putExtra("movDetalle", pedidoActual.getDetalle());
                intent.putExtra("movSenia", pedidoActual.getSenia());
                intent.putExtra("movResto", pedidoActual.getResto());
                intent.putExtra("movTotal", pedidoActual.getTotal());
                intent.putExtra("movNomCliente", pedidoActual.getNomCliente());
                intent.putExtra("movCelCliente", pedidoActual.getCelCliente());
                intent.putExtra("movIdTipos", pedidoActual.getIdEstado());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Id_FilaPedidos, textView_NomClien_FilaPedidos, textView_FechaEntreg_FilaPedidos;
        Button textView_Whatsapp_FilaPedidos;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Id_FilaPedidos = itemView.findViewById(R.id.textView_Id_FilaPedidos);
            textView_NomClien_FilaPedidos = itemView.findViewById(R.id.textView_NomClien_FilaPedidos);
            textView_FechaEntreg_FilaPedidos = itemView.findViewById(R.id.textView_FechaEntreg_FilaPedidos);
            textView_Whatsapp_FilaPedidos = itemView.findViewById(R.id.textView_Whatsapp_FilaPedidos);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilasPedidos);
        }
    }
}
