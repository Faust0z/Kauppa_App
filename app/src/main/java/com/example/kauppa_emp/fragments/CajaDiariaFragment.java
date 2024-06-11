package com.example.kauppa_emp.fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCajaDiaria;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CajaDiariaFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private ArrayList<String> movimiento_id, movimiento_tipo, movimiento_fecha, movimiento_monto;
    CustomAdapterCajaDiaria customAdapter;
    private FloatingActionButton addButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        movimiento_id = new ArrayList<>();
        movimiento_tipo = new ArrayList<>();
        movimiento_fecha = new ArrayList<>();
        movimiento_monto = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caja_diaria, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_cajaDiaria);
        addButton = view.findViewById(R.id.addButton_caja_diaria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configura el LinearLayoutManager aquí
        addButton.setOnClickListener(v -> abrirDialogoBotonFlotante());

        bddToArraylist();
        customAdapter = new CustomAdapterCajaDiaria(getContext(), movimiento_id, movimiento_tipo,
                movimiento_fecha, movimiento_monto);
        recyclerView.setAdapter(customAdapter);

        return view;
    }

    void bddToArraylist(){
        Cursor cursor = dbHelper.getAllMovimientos();
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                movimiento_id.add(cursor.getString(0));
                movimiento_fecha.add(cursor.getString(1));
                movimiento_tipo.add(cursor.getString(5));
                movimiento_monto.add(cursor.getString(3));
            }
        }
    }

    private void abrirDialogoBotonFlotante() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cajadiaria, null);

        TextView textViewTitulo = dialogView.findViewById(R.id.textViewTitulo);
        EditText editTextFecha = dialogView.findViewById(R.id.editTextFecha);
        EditText editTextMonto = dialogView.findViewById(R.id.editTextMonto);
        EditText editTextDetalle = dialogView.findViewById(R.id.editTextDetalle);
        RadioGroup radioGroupTipo = dialogView.findViewById(R.id.radioGroupTipo);
        RadioButton radioButtonEntradas = dialogView.findViewById(R.id.radioButtonEntradas);
        CheckBox checkBoxAgregar = dialogView.findViewById(R.id.checkBoxAgregar);

        // Configurar la fecha actual por defecto
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        editTextFecha.setText(dateFormat.format(calendar.getTime()));

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        editTextFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                editTextFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        // Cambiar el título y la checkbox según sea una Compra/Venta
        radioButtonEntradas.setChecked(true);
        radioGroupTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEntradas) {
                checkBoxAgregar.setText("Agregar como Venta");
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radioButtonSalidas) {
                checkBoxAgregar.setText("Agregar como Compra");
                textViewTitulo.setText("Agregar Salida");
            }
        });

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    double monto = Double.parseDouble(editTextMonto.getText().toString());
                    boolean esVentaOCompra = checkBoxAgregar.isChecked();
                    boolean esEntrada = radioButtonEntradas.isChecked();

                    if (insertarBDD(fecha, detalle, monto, esEntrada, esVentaOCompra) != -1){
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private long insertarBDD(String fecha, String detalle, double monto, boolean esEntrada, boolean esVentaOCompra){
        int id_tipo = -1;
        long id_movimiento;

        if (esEntrada) {
            if (esVentaOCompra) {
                id_tipo = 2; // VENTA DETALLADA
                id_movimiento = dbHelper.addMovimiento(fecha, detalle, monto, null, id_tipo);
                dbHelper.addVenta((int) id_movimiento, fecha, monto);
            } else {
                id_tipo = 1; // VENTA SIMPLE
                id_movimiento = dbHelper.addMovimiento(fecha, detalle, monto, null, id_tipo);
            }
        } else {
            if (esVentaOCompra) {
                id_tipo = 6; // COMPRA
                id_movimiento = dbHelper.addMovimiento(fecha, detalle, monto, null, id_tipo);
                dbHelper.addCompra((int) id_movimiento, fecha, monto);
            } else {
                id_tipo = 3; // PAGO
                id_movimiento = dbHelper.addMovimiento(fecha, detalle, monto, null, id_tipo);
            }
        }
        return id_movimiento;
    }
}
