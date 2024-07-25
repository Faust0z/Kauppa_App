package com.example.kauppa_emp.fragments;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCompras;
import com.example.kauppa_emp.fragments.dataObjects.Egresos;

public class ComprasFragment extends BaseFragment<Egresos> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_compras;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerViewCompras;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.buttonFiltrarCompras;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterCompras(getActivity(), getContext(), items);
    }

    protected void openAddDialog() {
        return; //Todo: a implementar
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_CajaDiaria; //R.id.addButtonCompras; Todo: Falta implementar este botón
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
    }

    @Override
    protected void bddToArraylist() {
        if (!buttonFiltrar.getText().toString().isEmpty()) {
            items = Egresos.bddToArraylist(dbHelper.getComprasByFecha(buttonFiltrar.getText().toString()));
        } else {
            items = Egresos.bddToArraylist(dbHelper.getAllCompras());
        }
    }
}