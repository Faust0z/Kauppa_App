package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Egresos;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterEgresos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class FiltrarPorFechaEgresos extends AppCompatActivity {
    protected RecyclerView recView_FiltrarPorFechaAct;
    protected Button button_Filtrar_FiltrarPorFechaAct, buttonVolver;

    protected String fecha;

    protected DatabaseHelper dbHelper;
    protected ArrayList<Egresos> items;
    protected RecyclerView.Adapter customAdapter;

    protected int getRecyclerViewId() {return R.id.recView_FiltrarPorFechaActEgresos;}

    protected int getFiltrarButtonId() {return R.id.button_Filtrar_FiltrarPorFechaActEgresos;}

    protected RecyclerView.Adapter getAdapter() {return new CustomAdapterEgresos(this, this, items);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_por_fecha_egresos);
        dbHelper = new DatabaseHelper(FiltrarPorFechaEgresos.this);
        items = new ArrayList<>();

        recView_FiltrarPorFechaAct = findViewById(getRecyclerViewId());
        recView_FiltrarPorFechaAct.setLayoutManager(new LinearLayoutManager(this));

        buttonVolver = findViewById(R.id.button_Volver_FiltrarPorFechaActEgresos);
        buttonVolver.setOnClickListener(v -> finish());

        getIntentData();
        createButtonFiltrar();
        addElementsToRecyclerView();
    }

    protected void createButtonFiltrar() {
        button_Filtrar_FiltrarPorFechaAct = findViewById(getFiltrarButtonId());
        button_Filtrar_FiltrarPorFechaAct.setText(fecha);
        button_Filtrar_FiltrarPorFechaAct.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                fecha = dateFormat.format(calendar.getTime());
                button_Filtrar_FiltrarPorFechaAct.setText(fecha);
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
        items = Egresos.bddToArraylist(dbHelper.getAllEgresos());
        Collections.reverse(items);
    }

    private void filtrarElementosPorFecha(){
        items.removeIf(item -> !item.getFecha().equals(fecha));
    }

}