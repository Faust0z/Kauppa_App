package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.activities.FiltrarPorFechaBalGnral;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCajaDiaria;
import com.example.kauppa_emp.database.dataObjects.Movimientos;

import android.widget.TextView;

import java.util.Collections;

public class BalGnralFragment extends BaseFragment<Movimientos> {
    private TextView textView_IngresosCant_Balgnral,
                     textView_EgresosCant_Balgnral,
                     textView_TotalCant_BalGnral,
                     textView_PdidoPndienCant_Balgnral,
                     textView_ProdBajoStockCant_Balgnral;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_balance_general;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_BalGnral;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_BalGnral;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterCajaDiaria(getActivity(), getContext(), items);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        textView_IngresosCant_Balgnral = view.findViewById(R.id.textView_IngresosCant_Balgnral);
        textView_EgresosCant_Balgnral = view.findViewById(R.id.textView_EgresosCant_Balgnral);
        textView_TotalCant_BalGnral = view.findViewById(R.id.textView_TotalCant_BalGnral);
        textView_PdidoPndienCant_Balgnral = view.findViewById(R.id.textView_PdidoPndienCant_Balgnral);
        textView_ProdBajoStockCant_Balgnral = view.findViewById(R.id.textView_ProdBajoStockCant_Balgnral);

        textView_IngresosCant_Balgnral.setText("$" + calcularTextViewIngresos());
        textView_EgresosCant_Balgnral.setText("$-" + calcularTextViewEgresos());
        textView_TotalCant_BalGnral.setText("$" + calcularTextViewTotal());
        textView_PdidoPndienCant_Balgnral.setText("");
        textView_ProdBajoStockCant_Balgnral.setText("");

        return view;
    }

    private double calcularTextViewIngresos(){
        double total = 0;
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.VENTA_SIMPLE) || itemTipo.equals(TiposMovimiento.VENTA_DETALLADA)){
                total += Double.parseDouble(item.getMonto());
            }
        }
        return total;
    }

    private double calcularTextViewEgresos(){
        double total = 0;
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.COMPRA) || itemTipo.equals(TiposMovimiento.COBRO)){
                total += Double.parseDouble(item.getMonto());
            }
        }
        return total;
    }

    private double calcularTextViewTotal(){
        return calcularTextViewIngresos() - calcularTextViewEgresos();
    }

    @Override
    protected void bddToArraylist() {
        items.clear();

        Cursor cursor = dbHelper.getAllMovimientos();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String tipo = cursor.getString(4);

                Movimientos movimiento = new Movimientos(id, fecha, detalle, monto, tipo);
                items.add(movimiento);
            }
        }
        Collections.reverse(items);
    }

    @Override
    protected Activity getFiltrarActivity() {
        return new FiltrarPorFechaBalGnral();
    }
}
