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
    private ArrayList<String> movimiento_id, movimiento_fecha, movimiento_detalle, movimiento_monto, movimiento_pedidosAfectados, movimiento_tipo;
    CustomAdapterCajaDiaria customAdapter;
    private FloatingActionButton addButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        // Ni el detalle ni el pedidos afectados se usa para el recyclerview, pero se necesita
        // almacenar para poder ver todos los datos del movimiento luego
        movimiento_id = new ArrayList<>();
        movimiento_fecha = new ArrayList<>();
        movimiento_detalle = new ArrayList<>();
        movimiento_tipo = new ArrayList<>();
        movimiento_monto = new ArrayList<>();
        movimiento_pedidosAfectados = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caja_diaria, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_cajaDiaria);
        addButton = view.findViewById(R.id.addButton_caja_diaria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addButton.setOnClickListener(v -> openAddDialog());

        addElementsToRecyclerView();

        return view;
    }

    private void bddToArraylist(){
        // Vacío los arraylist antes de volverles a insertar toda la BDD para evitar duplicados
        movimiento_id.clear();
        movimiento_fecha.clear();
        movimiento_detalle.clear();
        movimiento_tipo.clear();
        movimiento_monto.clear();
        movimiento_pedidosAfectados.clear();

        Cursor cursor = dbHelper.getAllMovimientos();
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                movimiento_id.add(cursor.getString(0));
                movimiento_fecha.add(cursor.getString(1));
                movimiento_detalle.add(cursor.getString(2));
                movimiento_monto.add(cursor.getString(3));
                movimiento_pedidosAfectados.add(cursor.getString(4));

                String checkVentaCompra = cursor.getString(5);
                // Si el id_tipo es 1, 2 o 4. Significa que es una venta simple, detallada o un cobro
                if (checkVentaCompra.equals("1") || checkVentaCompra.equals("2") || checkVentaCompra.equals("4") ){
                    movimiento_tipo.add("Entrada");
                } else{
                    movimiento_tipo.add("Salida");
                }
            }
        }
    }

    private void addElementsToRecyclerView(){
        bddToArraylist();
        customAdapter = new CustomAdapterCajaDiaria(getContext(), movimiento_id, movimiento_tipo,
                movimiento_fecha, movimiento_monto);
        recyclerView.setAdapter(customAdapter);
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
            if (checkedId == R.id.radioButtonEntradasCajaDiaria) {
                checkBoxAgregar.setText("Agregar como Venta");
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radioButtonSalidasCajaDiaria) {
                checkBoxAgregar.setText("Agregar como Compra");
                textViewTitulo.setText("Agregar Salida");
            }
        });

        // Cuando se aprete el botón "Agregar", se toman todos los inputs y se los procesa.
        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    double monto;
                    try {
                        monto = Double.parseDouble(editTextMonto.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error, no se ingresó un monto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean esVentaOCompra = checkBoxAgregar.isChecked();
                    boolean esEntrada = radioButtonEntradas.isChecked();

                    if (insertBDD(fecha, detalle, monto, esEntrada, esVentaOCompra) != -1){
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    }else{
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private long insertBDD(String fecha, String detalle, double monto, boolean esEntrada, boolean esVentaOCompra){
        int id_tipo = -1;
        // Los métodos de insert en la BDD devuelven un código para indicar cómo fue la operación.
        // En caso de que sea -1 vamos a saber que falló el insert.
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
