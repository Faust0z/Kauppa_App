package com.example.kauppa_emp.fragments.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.Productos;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;

public class CustomAdapterProdsAsociadosEnStock extends CustomAdapterProductos {
    protected ArrayList<Productos> prodsAsociados;
    protected ArrayList<Productos> prodsAgregados;

    private CustomAdapterProdsAsociados customAdapter;

    public CustomAdapterProdsAsociadosEnStock(Activity activity, Context context, ArrayList<Productos> productos,
                                              ArrayList<Productos> prodsAsociados, ArrayList<Productos> prodsAgregados,
                                              CustomAdapterProdsAsociados customAdapter) {
        super(activity, context, productos);
        this.prodsAsociados = prodsAsociados;
        this.customAdapter = customAdapter;
        this.prodsAgregados = prodsAgregados;
    }

    @Override
    protected void clickEnFila(@NonNull MyViewHolder holder, Productos prodActual) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.dialog_add_prod, null);

        dialogView.findViewById(R.id.editText_NombreProd_Prod).setVisibility(View.GONE);
        dialogView.findViewById(R.id.editText_PrecUnit_Prod).setVisibility(View.GONE);
        EditText editText_CantInic_Prod = dialogView.findViewById(R.id.editText_CantInic_Prod);

        new MaterialAlertDialogBuilder(activity)
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    int cantidad = Integer.parseInt(editText_CantInic_Prod.getText().toString());
                    if (!prodActual.esFantasma() && cantidad > Integer.parseInt((prodActual.getStock()))){
                        Toast.makeText(activity, "La cantidad del producto debe ser menor al stock existente", Toast.LENGTH_SHORT).show();
                    }else{
                        prodActual.setCant(cantidad);
                        prodActual.setRecienAgregado(true);

                        prodsAsociados.add(prodActual);
                        prodsAgregados.add(prodActual);
                        productos.remove(prodActual);

                        activity.findViewById(R.id.frameLay_ProdsEnStock).setVisibility(View.INVISIBLE);
                        activity.findViewById(R.id.linearLay_MasInfo).setVisibility(View.VISIBLE);
                        notifyDataSetChanged();
                        customAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
