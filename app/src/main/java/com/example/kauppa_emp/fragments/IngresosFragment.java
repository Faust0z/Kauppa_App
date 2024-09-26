package com.example.kauppa_emp.fragments;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.AgregarIngreso;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterIngresos;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.ViewGroup;

import java.util.Collections;

public class IngresosFragment extends BaseFragment<Ingresos> {
    private FloatingActionButton addButton;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ingresos;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_Ingresos;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_Ingresos;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_Ingresos;
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
        return new CustomAdapterIngresos(getActivity(), getContext(), items);
    }

    @Override
    protected void bddToArraylist() {
        items = Ingresos.bddToArraylist(dbHelper.getAllIngresos());
        Collections.reverse(items);
    }

    protected void openAddDialog() {
        Intent intent = new Intent(getActivity(), AgregarIngreso.class);
        startActivity(intent);
    }
}
