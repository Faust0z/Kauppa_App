package com.example.kauppa_emp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CajaDiariaFragment extends Fragment {

    private RecyclerView recyclerViewCajaDiaria;
    private FloatingActionButton addButtonCajaDiaria;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caja_diaria, container, false);

        recyclerViewCajaDiaria = view.findViewById(R.id.recyclerView_cajaDiaria);
        addButtonCajaDiaria = view.findViewById(R.id.addButton_caja_diaria);

        addButtonCajaDiaria.setOnClickListener(v -> mostrarDialogoAgregarMovimiento());

        return view;
    }

    private void mostrarDialogoAgregarMovimiento() {
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
                    String descripcion = editTextDetalle.getText().toString();
                    String montoStr = editTextMonto.getText().toString();
                    double monto = Double.parseDouble(montoStr);
                    boolean esVentaOCompra = checkBoxAgregar.isChecked();

                    // Aquí agregar la lógica para guardar en la base de datos
                    Toast.makeText(getContext(), "Fecha: " + fecha + ", Descripción: " + descripcion + ", Monto: " + monto + ", Es Venta/Compra: " + esVentaOCompra, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
