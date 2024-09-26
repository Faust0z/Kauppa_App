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
import com.example.kauppa_emp.activities.MasInfoProds;
import com.example.kauppa_emp.database.dataObjects.Productos;

import java.util.ArrayList;

public class CustomAdapterProductos extends RecyclerView.Adapter<CustomAdapterProductos.MyViewHolder> {
    protected final Context context;
    protected final Activity activity;
    protected final ArrayList<Productos> productos;

    public CustomAdapterProductos(Activity activity, Context context, ArrayList<Productos> productos){
        this.activity = activity;
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filas_prods, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Productos prodActual = productos.get(position);
        holder.textView_Id_FilaProds.setText(prodActual.getId());
        holder.textView_Nombre_FilaProds.setText(prodActual.getNombre());
        holder.textView_CantStock_FilaProds.setText(prodActual.getStock());
        holder.textView_PrecioUni_FilaProds.setText(prodActual.getPrecioUnitario());

        holder.mainLayout.setOnClickListener(v -> clickEnFila(holder, prodActual));
    }

    protected void clickEnFila(@NonNull MyViewHolder holder, Productos prodActual) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            Intent intent = new Intent(context, MasInfoProds.class);
            intent.putExtra("prodId", prodActual.getId());
            intent.putExtra("prodNombre", prodActual.getNombre());
            intent.putExtra("prodStock", prodActual.getStock());
            intent.putExtra("prodFechaAct", prodActual.getFechaActualiz());
            intent.putExtra("prodPrecioUni", prodActual.getPrecioUnitario());
            activity.startActivityForResult(intent, 1);
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_Id_FilaProds, textView_Nombre_FilaProds, textView_CantStock_FilaProds, textView_PrecioUni_FilaProds;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Id_FilaProds = itemView.findViewById(R.id.textView_Id_FilaProds);
            textView_Nombre_FilaProds = itemView.findViewById(R.id.textView_Nombre_FilaProds);
            textView_CantStock_FilaProds = itemView.findViewById(R.id.textView_CantStock_FilaProds);
            textView_PrecioUni_FilaProds = itemView.findViewById(R.id.textView_PrecioUni_FilaProds);
            mainLayout = itemView.findViewById(R.id.mainLayoutFilasProds);
        }
    }
}
