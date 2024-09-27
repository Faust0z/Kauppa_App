package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.AgregarPedido;
import com.example.kauppa_emp.database.dataObjects.Pedidos;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterPedidos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

public class PedidosFragment extends BaseFragment<Pedidos> {
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
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addButton = view.findViewById(getAddButtonId());
        addButton.setOnClickListener(v -> openAddDialog());

        return view;
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
