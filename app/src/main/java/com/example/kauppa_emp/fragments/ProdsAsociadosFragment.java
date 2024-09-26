package com.example.kauppa_emp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.database.dataObjects.Productos;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProdsAsociados;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProdsAsociadosEnStock;

import java.util.ArrayList;
import java.util.Collections;


public class ProdsAsociadosFragment extends ProductosFragment {
    // Una extensión del fragmento de productos para cuando es necesario mostrar productos
    // en la pantalla de ingresos o pedidos.
    ArrayList<Productos> prodsAsociados;
    ArrayList<Productos> prodsEliminados;
    CustomAdapterProdsAsociados customAdapterProdsAsociados;

    public ProdsAsociadosFragment(ArrayList<Productos> prodsAsociados, ArrayList<Productos> prodsEliminados, CustomAdapterProdsAsociados customAdapterProdsAsociados) {
        super();
        this.prodsAsociados = prodsEliminados;
        this.prodsEliminados = prodsAsociados;
        this.customAdapterProdsAsociados = customAdapterProdsAsociados;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        // Estos for son para sacar todos los productos que ya estén agregados
        ArrayList<Productos> temp = new ArrayList<>();
        for (Productos prod : prodsAsociados) {
            for (Productos item : items) {
                if (item.getId().equals(prod.getId())) {
                    temp.add(item);
                    break;
                }
            }
        }
        ArrayList<Productos> itemsNoYaAsociados = new ArrayList<>();
        for (Productos item : items) {
            if (!temp.contains(item)) {
                itemsNoYaAsociados.add(item);
            }
        }
        return new CustomAdapterProdsAsociadosEnStock(getActivity(), getContext(), itemsNoYaAsociados,
                prodsAsociados, prodsEliminados, customAdapterProdsAsociados);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        textView_Titulo_Productos.setText("Agregar producto en stock");

        textView_totalCant_Prods.setVisibility(View.GONE);
        textView_StkBajoCant_Prods.setVisibility(View.GONE);
        textView_SinStkCant_Prods.setVisibility(View.GONE);

        return view;
    }
}