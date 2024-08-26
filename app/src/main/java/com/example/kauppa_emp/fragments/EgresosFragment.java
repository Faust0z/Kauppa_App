package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.activities.FiltrarPorFechaEgresos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterEgresos;
import com.example.kauppa_emp.database.dataObjects.Egresos;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EgresosFragment extends BaseFragment<Egresos> {
    private FloatingActionButton addButton;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_egresos;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_Egresos;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_Egresos;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_Egresos;
    }

    @Override
    protected Activity getFiltrarActivity() {
        return new FiltrarPorFechaEgresos();
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
        return new CustomAdapterEgresos(getActivity(), getContext(), items);
    }

    @Override
    protected void bddToArraylist() {
        items = Egresos.bddToArraylist(dbHelper.getAllEgresos());
        Collections.reverse(items);
    }

    protected void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_egreso, null);

        TextInputEditText editText_NomProv_Egreso = dialogView.findViewById(R.id.editText_NomProv_Egreso);
        TextInputEditText editText_Fecha_Egreso = dialogView.findViewById(R.id.editText_Fecha_Egreso);
        TextInputEditText editText_Monto_Egreso = dialogView.findViewById(R.id.editText_Monto_Egreso);
        TextInputEditText editText_Detalle_Egreso = dialogView.findViewById(R.id.editText_Detalle_Egreso);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, TiposMovimiento.relleSpinnerEgresos());
        AutoCompleteTextView tipoEgresoDropdown = (AutoCompleteTextView) dialogView.findViewById(R.id.spinner_Tipo_Egreso);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEgresoDropdown.setAdapter(arrayAdapter);

        createTextFechaYDialog(editText_Fecha_Egreso);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String nomProv = String.valueOf(editText_NomProv_Egreso.getText());
                    String fecha = String.valueOf(editText_Fecha_Egreso.getText());
                    String monto = String.valueOf(editText_Monto_Egreso.getText());
                    String detalle = String.valueOf(editText_Detalle_Egreso.getText());
                    String tipo = tipoEgresoDropdown.getEditableText().toString();

                    int id = TiposMovimiento.nombreToId(tipo);
                    float result = dbHelper.addEgreso(fecha, monto, detalle, id, nomProv);
                    if (result != -1) {
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void createTextFechaYDialog(EditText editText_Fecha_Egreso) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        editText_Fecha_Egreso.setText(dateFormat.format(calendar.getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        editText_Fecha_Egreso.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                editText_Fecha_Egreso.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });
    }
}