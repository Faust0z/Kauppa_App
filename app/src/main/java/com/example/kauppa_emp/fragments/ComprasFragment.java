package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCompras;
import com.example.kauppa_emp.fragments.dataObjects.Egresos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
        return R.id.addButton_caja_diaria; //R.id.addButtonCompras; Todo: Falta implementar este botón
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
    }

    @Override
    protected void bddToArraylist() {
        items.clear();
        Cursor cursor;
        if (!buttonFiltrar.getText().toString().isEmpty()) {
            cursor = dbHelper.getComprasByFecha(buttonFiltrar.getText().toString());
        } else {
            cursor = dbHelper.getAllCompras();
        }

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomCliente = cursor.getString(5);

                Egresos egreso = new Egresos(id, fecha, monto, detalle, idTipo, nomCliente);
                items.add(egreso);
            }
            cursor.close();
        }
    }
}