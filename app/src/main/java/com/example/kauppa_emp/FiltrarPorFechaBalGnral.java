package com.example.kauppa_emp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.database.DatabaseHelper;

import java.util.ArrayList;

public class FiltrarPorFechaBalGnral<T> extends AppCompatActivity {
    protected RecyclerView recView_FiltrarPorFechaAct;
    protected Button button_Filtrar_FiltrarPorFechaAct, buttonVolver;

    protected String fecha;

    protected DatabaseHelper dbHelper;
    protected ArrayList<T> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_por_fecha_balgnral);
        dbHelper = new DatabaseHelper(FiltrarPorFechaBalGnral.this);

        button_Filtrar_FiltrarPorFechaAct = findViewById(R.id.button_Filtrar_FiltrarPorFechaActBalGnral);
        recView_FiltrarPorFechaAct = findViewById(R.id.recView_FiltrarPorFechaActBalGnral);
        buttonVolver = findViewById(R.id.button_Volver_FiltrarPorFechaActBalGnral);

        recView_FiltrarPorFechaAct.setLayoutManager(new LinearLayoutManager(FiltrarPorFechaBalGnral.this));

        getIntentData();
//        filtrarElementosPorFecha();

    }

    private void getIntentData(){
        if (getIntent().hasExtra("fecha")){
            fecha = getIntent().getStringExtra("fecha");
        }
    }

//    private void filtrarElementosPorFecha(){
//        items.removeIf(item -> item.getFecha().equals(fecha));
//    }
}