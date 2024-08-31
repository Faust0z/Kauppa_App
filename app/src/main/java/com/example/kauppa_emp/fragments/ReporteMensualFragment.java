package com.example.kauppa_emp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterReporteMensualMasVendido;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterReporteMensualMayorGanancia;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ReporteMensualFragment extends Fragment {

    private MaterialButton btnVolverReporteMensual;
    private RecyclerView recyclerViewMasVendidos;
    private RecyclerView recyclerViewMayorGanancia;
    private CustomAdapterReporteMensualMasVendido customAdapterReporteMensualMasVendido;
    private CustomAdapterReporteMensualMayorGanancia customAdapterReporteMensualMayorGanancia;

    private ArrayList<LineaProductoMasVendido> listaProductosMasVendidos;
    private ArrayList<LineaProductoMayorGanancia> listaProductosMayorGanancia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reporte_mensual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Se define un manejador de evento para el botón "retroceder", permitiendo
        // al usuario volver a la pantalla de herramientas.
        btnVolverReporteMensual = view.findViewById(R.id.btnVolverReporteMensual);
        btnVolverReporteMensual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop fragment from stack
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        // Inicializar el RecyclerView y el adaptador
        recyclerViewMasVendidos = view.findViewById(R.id.recyclerViewMasVendidos);
        recyclerViewMasVendidos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear una lista de productos de ejemplo
        listaProductosMasVendidos = new ArrayList<>();
        listaProductosMasVendidos.add(new LineaProductoMasVendido("Anillo Finlandes", 40, 1));
        listaProductosMasVendidos.add(new LineaProductoMasVendido("Piedra Energetica", 30, 2));
        listaProductosMasVendidos.add(new LineaProductoMasVendido("Adorno Artesanal", 20, 3));
        listaProductosMasVendidos.add(new LineaProductoMasVendido("Bombilla Artesanal", 10, 4));

        // Inicializar el adaptador con la lista de productos y configurar el RecyclerView
        customAdapterReporteMensualMasVendido = new CustomAdapterReporteMensualMasVendido(listaProductosMasVendidos);
        recyclerViewMasVendidos.setAdapter(customAdapterReporteMensualMasVendido);


        // Inicializar el RecyclerView y el adaptador
        recyclerViewMayorGanancia = view.findViewById(R.id.recyclerViewMayorGanancia);
        recyclerViewMayorGanancia.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear una lista de productos de ejemplo
        listaProductosMayorGanancia = new ArrayList<>();
        listaProductosMayorGanancia.add(new LineaProductoMayorGanancia("Anillo Finlandes", 80000, 1));
        listaProductosMayorGanancia.add(new LineaProductoMayorGanancia("Piedra Energetica", 55000, 2));
        listaProductosMayorGanancia.add(new LineaProductoMayorGanancia("Adorno Artesanal", 27000, 3));
        listaProductosMayorGanancia.add(new LineaProductoMayorGanancia("Bombilla Artesanal", 14500.98, 4));

        // Inicializar el adaptador con la lista de productos y configurar el RecyclerView
        customAdapterReporteMensualMayorGanancia = new CustomAdapterReporteMensualMayorGanancia(listaProductosMayorGanancia);
        recyclerViewMayorGanancia.setAdapter(customAdapterReporteMensualMayorGanancia);

    }

    public static class LineaProductoMasVendido {

        private final String nombre;
        private final int unidades;
        private final int puesto;

        public LineaProductoMasVendido(String nombre, int unidades, int puesto) {
            this.nombre = nombre;
            this.unidades = unidades;
            this.puesto = puesto;
        }

        public String getNombre() {
            return nombre;
        }

        public int getUnidades() {
            return unidades;
        }

        public int getPuesto() {
            return puesto;
        }

    }

    public static class LineaProductoMayorGanancia {

        private final String nombre;
        private final double ganancia;
        private final int puesto;

        public LineaProductoMayorGanancia(String nombre, double ganancia, int puesto) {
            this.nombre = nombre;
            this.ganancia = ganancia;
            this.puesto = puesto;
        }

        public String getNombre() {
            return nombre;
        }

        public double getGanancia() {
            return ganancia;
        }

        public int getPuesto() {
            return puesto;
        }

    }

}
