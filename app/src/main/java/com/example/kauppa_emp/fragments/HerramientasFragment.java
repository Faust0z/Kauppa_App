package com.example.kauppa_emp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.google.android.material.card.MaterialCardView;

public class HerramientasFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_herramientas, container, false);

        MaterialCardView btnCalcularPrecio = view.findViewById(R.id.btnCalcularPrecio);
        btnCalcularPrecio.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new CalcularPrecioFragment())
                    .addToBackStack(null)
                    .commit();
        });

        MaterialCardView btnReporteMensual = view.findViewById(R.id.btnReporteMensual);
        btnReporteMensual.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ReporteMensualFragment())
                .addToBackStack(null)
                .commit());

        MaterialCardView button_AccesoProds_Herrs = view.findViewById(R.id.button_AccesoProds_Herrs);
        button_AccesoProds_Herrs.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ProductosFragment())
                .addToBackStack(null)
                .commit());

        return view;
    }
}