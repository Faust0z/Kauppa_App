package com.example.kauppa_emp.fragments;

import android.app.Activity;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class BalGnralFragment extends BaseFragment<Movimientos> {
    private TextView textView_IngresosCant_Balgnral,
                     textView_EgresosCant_Balgnral,
                     textView_TotalCant_BalGnral,
                     textView_PdidoPndienCant_Balgnral,
                     textView_ProdBajoStockCant_Balgnral;

    private BigDecimal bigDeciTotalIngres, bigDeciTotalEgres;

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

        calcularIngEgrTotal();

        textView_PdidoPndienCant_Balgnral.setText("");
        textView_ProdBajoStockCant_Balgnral.setText("");

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        calcularIngEgrTotal();
    }

    private void calcularIngEgrTotal(){
        bigDeciTotalIngres = calcularTextViewIngresos();
        bigDeciTotalEgres = calcularTextViewEgresos();
        textView_IngresosCant_Balgnral.setText("$" + bigDeciTotalIngres);
        textView_EgresosCant_Balgnral.setText("$-" + bigDeciTotalEgres);
        textView_TotalCant_BalGnral.setText("$" + bigDeciTotalIngres.subtract(bigDeciTotalEgres));
    }

    private BigDecimal calcularTextViewIngresos(){
        BigDecimal total = new BigDecimal(0);
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.VENTA_SIMPLE) || itemTipo.equals(TiposMovimiento.VENTA_DETALLADA)){
                total = total.add(new BigDecimal(item.getMonto()));
            }
        }
        return total;
    }

    private BigDecimal calcularTextViewEgresos(){
        BigDecimal total = new BigDecimal(0);
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.COMPRA) || itemTipo.equals(TiposMovimiento.COBRO) || itemTipo.equals(TiposMovimiento.VARIOS)){
                total = total.add(new BigDecimal(item.getMonto()));
            }
        }
        return total;
    }

    @Override
    protected void bddToArraylist() {
        items = Movimientos.bddToArraylist(dbHelper, dbHelper.getAllMovimientos());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        items.sort((mov1, mov2) -> {
            try {
                Date fecha1 = dateFormat.parse(mov1.getFecha());
                Date fecha2 = dateFormat.parse(mov2.getFecha());
                return fecha1.compareTo(fecha2);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        });

        Collections.reverse(items);
    }

    @Override
    protected Activity getFiltrarActivity() {
        return new FiltrarPorFechaBalGnral();
    }
}
