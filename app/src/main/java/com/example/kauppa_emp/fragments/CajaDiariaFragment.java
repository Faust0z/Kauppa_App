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
import com.example.kauppa_emp.fragments.dataObjects.Movimientos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Button;
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
    private FloatingActionButton addButton;
    private Button buttonFiltrar, buttonResetFiltro;

    private DatabaseHelper dbHelper;
    private ArrayList<Movimientos> movimientos;
    private CustomAdapterCajaDiaria customAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        movimientos = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caja_diaria, container, false);

        createButtonFiltrar(view);

        // Todo: eliminar este botón
        buttonResetFiltro = view.findViewById(R.id.buttonResetFiltro);
        buttonResetFiltro.setOnClickListener(v -> {
            // Limpiamos el campo de filtro y llamamos para refrescar los datos del recyclerview
            buttonFiltrar.setText("");
            addElementsToRecyclerView();
        });

        recyclerView = view.findViewById(R.id.recyclerView_cajaDiaria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addButton = view.findViewById(R.id.addButton_caja_diaria);
        addButton.setOnClickListener(v -> openAddDialog());

        addElementsToRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addElementsToRecyclerView();
    }

    private void createButtonFiltrar(View view){
        buttonFiltrar = view.findViewById(R.id.buttonFiltrarCajaDiaria);
        buttonFiltrar.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                buttonFiltrar.setText(dateFormat.format(calendar.getTime()));


            }, year, month, day);
            datePickerDialog.show();

            addElementsToRecyclerView();
        });
    }

    private void addElementsToRecyclerView() {
        bddToArraylist();
        customAdapter = new CustomAdapterCajaDiaria(getActivity(), getContext(), movimientos);
        recyclerView.setAdapter(customAdapter);
    }

    private void bddToArraylist() {
        movimientos.clear();

        // Todo: la funcionalidad de esto pasará a una activity, por lo que no es necesaria acá
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

                // Todo: puede ser que esta lógica se mueva a otro lado y en el movimiento simplemente se guarde el id en forma de string
                String tipo;
                String checkVentaCompra = cursor.getString(4);
                if (checkVentaCompra.equals("1") || checkVentaCompra.equals("2") || checkVentaCompra.equals("4")) {
                    tipo = "Entrada";
                } else {
                    tipo = "Salida";
                }

                Movimientos movimiento = new Movimientos(id, fecha, detalle, monto, tipo);
                movimientos.add(movimiento);
            }
        }
    }

    private void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cajadiaria, null);

        TextView textViewTitulo = dialogView.findViewById(R.id.textViewTituloCajaDiaria);
        EditText editTextFecha = dialogView.findViewById(R.id.editTextFechaCajaDiaria);
        EditText editTextMonto = dialogView.findViewById(R.id.editTextMontoCajaDiaria);
        EditText editTextDetalle = dialogView.findViewById(R.id.editTextDetalleCajaDiaria);
        RadioGroup radioGroupTipo = dialogView.findViewById(R.id.radioGroupTipoCajaDiaria);
        RadioButton radioButtonEntradas = dialogView.findViewById(R.id.radioButtonEntradasCajaDiaria);
        CheckBox checkBoxAgregar = dialogView.findViewById(R.id.checkBoxAgregarCajaDiaria);

        createTextFechaYDialog(editTextFecha);
        createRadioGroup(radioGroupTipo, radioButtonEntradas, checkBoxAgregar, textViewTitulo);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    String monto = editTextMonto.getText().toString();
                    boolean esVentaOCompra = checkBoxAgregar.isChecked();
                    boolean esEntrada = radioButtonEntradas.isChecked();

                    if (insertBDD(fecha, detalle, monto, esEntrada, esVentaOCompra) != -1) {
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

    private void createRadioGroup(RadioGroup radioGroupTipo, RadioButton radioButtonEntradas, CheckBox checkBoxAgregar, TextView textViewTitulo) {
        radioButtonEntradas.setChecked(true);
        radioGroupTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEntradasCajaDiaria) {
                checkBoxAgregar.setText("Agregar como Venta");
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radioButtonSalidasCajaDiaria) {
                checkBoxAgregar.setText("Agregar como Compra");
                textViewTitulo.setText("Agregar Salida");
            }
        });
    }

    private long insertBDD(String fecha, String monto, String detalle, boolean esEntrada, boolean esVentaOCompra) {
        int idTipo = 0;

        if (esEntrada) {
            if (esVentaOCompra) {
                idTipo = 2; // VENTA DETALLADA
            } else {
                idTipo = 1; // VENTA SIMPLE
            }
        } else {
            if (esVentaOCompra) {
                idTipo = 6; // COMPRA
            } else {
                idTipo = 3; // PAGO
            }
        }
        return dbHelper.addMovimiento(fecha, monto, detalle, idTipo);
    }
}