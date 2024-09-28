package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.AgregarPedido;
import com.example.kauppa_emp.activities.FiltrarPorFechaPedidos;
import com.example.kauppa_emp.database.dataObjects.Pedidos;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterPedidos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.Collections;

public class PedidosFragment extends BaseFragment<Pedidos> {
    private TextView textView_totalCant_Pdid,
            textView_PendientesCant_Pdid,
            textView_ListosCant_Pdid;

    private FloatingActionButton addButton;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pedidos;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_Pedidos;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_Pedidos;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_Pedidos;
    }

    @Override
    protected Activity getFiltrarActivity() {
        return new FiltrarPorFechaPedidos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        textView_totalCant_Pdid = view.findViewById(R.id.textView_totalCant_Pdid);
        textView_PendientesCant_Pdid = view.findViewById(R.id.textView_PendientesCant_Pdid);
        textView_ListosCant_Pdid = view.findViewById(R.id.textView_ListosCant_Pdid);

        addButton = view.findViewById(getAddButtonId());
        addButton.setOnClickListener(v -> openAddDialog());

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        calcularIngEgrTotal();
    }

    private void calcularIngEgrTotal() {
        int totalPedidos = 0, totalPendientes = 0, totalListos = 0;
        for (Pedidos item : items) {
            String idEstado = item.getIdEstado();
            if (idEstado.equals("1") || idEstado.equals("2")){
                totalPendientes++;
            }
            if (idEstado.equals("3") || idEstado.equals("4")){
                totalListos++;
            }
            totalPedidos++;

            textView_totalCant_Pdid.setText(String.valueOf(totalPedidos));
            textView_PendientesCant_Pdid.setText(String.valueOf(totalPendientes));
            textView_ListosCant_Pdid.setText(String.valueOf(totalListos));
        }
    }

    private BigDecimal calcularTextViewPedidos(){
        BigDecimal total = new BigDecimal(0);
        for (Pedidos item : items) {
            String itemEstado = item.getIdEstado();
            if (!itemEstado.equals("4")){
                total = total.add(new BigDecimal(item.getTotal()));
            }
        }
        return total;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterPedidos(getActivity(), getContext(), items);
    }

    @Override
    protected void bddToArraylist() {
        items = Pedidos.bddToArraylist(dbHelper.getAllPedidos());
        Collections.reverse(items);
    }

    protected void openAddDialog() {
        Intent intent = new Intent(getActivity(), AgregarPedido.class);
        startActivity(intent);
    }
}
