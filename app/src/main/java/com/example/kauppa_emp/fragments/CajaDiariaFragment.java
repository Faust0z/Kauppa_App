package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.MainActivity;
import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCajaDiaria;
import com.example.kauppa_emp.fragments.dataObjects.Movimientos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CajaDiariaFragment extends BaseFragment<Movimientos> {
    private FloatingActionButton addButton;
    private Button button_irBalGnral_CajaDiaria;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_caja_diaria;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerView_cajaDiaria;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.buttonFiltrarCajaDiaria;
    }

    protected int getAddButtonId() {
        return R.id.addButton_caja_diaria;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addButton = view.findViewById(getAddButtonId());
        addButton.setOnClickListener(v -> openAddDialog());

        button_irBalGnral_CajaDiaria = view.findViewById(R.id.button_irBalGnral_CajaDiaria);
        button_irBalGnral_CajaDiaria.setOnClickListener(v -> changeFragment(new BalGnralFragment()));

        return view;
    }

    /*
      Este método lo copié del Main activity y no me termina de convencer la idea. Buscando encontré
      que se puede agregar una interfaz para permitir a cualquier fragmento llamar a ese método, pero
      no sé cuánto me convence hacer eso si ningún otro fragmento tiene necesidad de usar esto.
    */
    public void changeFragment(Fragment newFragment) { //Tuve que repetir
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
    }

    @Override
    protected void bddToArraylist() {
        items.clear();
        Cursor cursor = null;
        if (!buttonFiltrar.getText().toString().isEmpty()) {
            cursor = dbHelper.getMovimientosByFecha(buttonFiltrar.getText().toString());
        } else {
            cursor = dbHelper.getAllMovimientos();
        }

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
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterCajaDiaria(getActivity(), getContext(), items);
    }

    protected void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cajadiaria, null);

        TextView textViewTitulo = dialogView.findViewById(R.id.textViewTituloCajaDiaria);
        EditText editTextFecha = dialogView.findViewById(R.id.editTextFechaCajaDiaria);
        EditText editTextMonto = dialogView.findViewById(R.id.editTextMontoCajaDiaria);
        EditText editTextDetalle = dialogView.findViewById(R.id.editTextDetalleCajaDiaria);
        RadioGroup radioGroupTipo = dialogView.findViewById(R.id.radioGroupTipoCajaDiaria);
        RadioButton radioButtonEntradas = dialogView.findViewById(R.id.radioButtonEntradasCajaDiaria);

        createTextFechaYDialog(editTextFecha);
        createRadioGroup(radioGroupTipo, radioButtonEntradas, textViewTitulo);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    String monto = editTextMonto.getText().toString();
                    boolean esEntrada = radioButtonEntradas.isChecked();

                    if (insertBDD(fecha, detalle, monto, esEntrada) != -1) {
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void createTextFechaYDialog(EditText editTextFecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        editTextFecha.setText(dateFormat.format(calendar.getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        editTextFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                editTextFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void createRadioGroup(RadioGroup radioGroupTipo, RadioButton radioButtonEntradas, TextView textViewTitulo) {
        radioButtonEntradas.setChecked(true);
        radioGroupTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEntradasCajaDiaria) {
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radioButtonSalidasCajaDiaria) {
                textViewTitulo.setText("Agregar Salida");
            }
        });
    }

    private long insertBDD(String fecha, String detalle, String monto, boolean esEntrada) {
        String tipo;
        //Todo: placeholders para probar el Balance Gnral
        if (esEntrada) {
            tipo = TiposMovimiento.VENTA_SIMPLE;
        } else {
            tipo = TiposMovimiento.COMPRA;
        }
        return dbHelper.addMovimiento(fecha, detalle, monto, Integer.parseInt(tipo));
    }
}
