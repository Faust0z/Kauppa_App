package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCajaDiaria;
import com.example.kauppa_emp.database.dataObjects.Movimientos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class FiltrarPorFechaBalGnral extends AppCompatActivity {
    protected RecyclerView recView_FiltrarPorFechaAct;
    protected Button button_Filtrar_FiltrarPorFechaAct, buttonVolver;

    protected String fecha;

    protected DatabaseHelper dbHelper;
    protected ArrayList<Movimientos> items;
    protected RecyclerView.Adapter customAdapter;

    protected int getRecyclerViewId() {return R.id.recView_FiltrarPorFechaActBalGnral;}

    protected int getFiltrarButtonId() {return R.id.button_Filtrar_FiltrarPorFechaActBalGnral;}

    protected RecyclerView.Adapter getAdapter() {return new CustomAdapterCajaDiaria(this, this, items);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_por_fecha_balgnral);
        dbHelper = new DatabaseHelper(FiltrarPorFechaBalGnral.this);
        items = new ArrayList<>();

        createButtonFiltrar();

        recView_FiltrarPorFechaAct = findViewById(getRecyclerViewId());
        recView_FiltrarPorFechaAct.setLayoutManager(new LinearLayoutManager(this));

        buttonVolver = findViewById(R.id.button_Volver_FiltrarPorFechaActBalGnral);
        buttonVolver.setOnClickListener(v -> finish());

        getIntentData();
        addElementsToRecyclerView();
    }

    protected void createButtonFiltrar() {
        button_Filtrar_FiltrarPorFechaAct = findViewById(getFiltrarButtonId());
        button_Filtrar_FiltrarPorFechaAct.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                fecha = dateFormat.format(calendar.getTime());
                addElementsToRecyclerView();
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void getIntentData(){
        if (getIntent().hasExtra("fecha")){
            fecha = getIntent().getStringExtra("fecha");
        }
    }

    protected void addElementsToRecyclerView() {
        bddToArraylist();
        filtrarElementosPorFecha();
        customAdapter = getAdapter();
        recView_FiltrarPorFechaAct.setAdapter(customAdapter);
    }

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

    private void filtrarElementosPorFecha(){
        items.removeIf(item -> !item.getFecha().equals(fecha));
    }
}