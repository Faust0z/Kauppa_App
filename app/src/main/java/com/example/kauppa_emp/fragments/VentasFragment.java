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
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProductosVentasDialog;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterVentas;
import com.example.kauppa_emp.fragments.dataObjects.Ingresos;
import com.example.kauppa_emp.fragments.dataObjects.ProductosEnIngresos;
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

public class VentasFragment extends BaseFragment<Ingresos> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ventas;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerViewVentas;
    }

    protected int getAddButtonId() {
        return R.id.addButtonVentas;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.buttonFiltrarVentas;
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
            cursor = dbHelper.getVentasByFecha(buttonFiltrar.getText().toString());
        } else {
            cursor = dbHelper.getAllVentas();
        }

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomCliente = cursor.getString(5);

                Ingresos ingreso = new Ingresos(id, fecha, monto, detalle, idTipo, nomCliente);
                items.add(ingreso);
            }
            cursor.close();
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterVentas(getActivity(), getContext(), items);
    }

    protected void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ventas, null);

        TextView textViewTitulo = dialogView.findViewById(R.id.textViewTituloVenta);
        EditText editTextFecha = dialogView.findViewById(R.id.editTextFechaVenta);
        EditText editTextDetalle = dialogView.findViewById(R.id.editTextDetalleVenta);
        TextView campoTotal = dialogView.findViewById(R.id.textViewTotalVenta);

        // Todo: todo esto se tiene que ir
        Cursor cursor = dbHelper.getAllProductos();
        ArrayList<String> arrayProdId = new ArrayList<>();
        ArrayList<String> arrayProdNombre = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                arrayProdId.add(cursor.getString(0));
                arrayProdNombre.add(cursor.getString(1));
            }
        }

        // Inicializar el RecyclerView
        RecyclerView recyclerViewProductos = dialogView.findViewById(R.id.recyclerViewProductosVenta);
        CustomAdapterProductosVentasDialog adapter = new CustomAdapterProductosVentasDialog(getContext(), arrayProdNombre);
        recyclerViewProductos.setAdapter(adapter);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el botón para agregar productos
        Button agregarProductoButton = dialogView.findViewById(R.id.buttonAgregarProductoVentasDialog);
        agregarProductoButton.setOnClickListener(v -> {
            adapter.addItem("");
        });

        // Para calcular automáticamente el monto, llamamos al método del adapter que nos da ese valor
        campoTotal.setText(String.valueOf(adapter.getSelectedPriceSum(dbHelper)));

        // Configurar la fecha actual por defecto
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        editTextFecha.setText(dateFormat.format(calendar.getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        editTextFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                editTextFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        // Cuando se aprete el botón "Agregar", se toman todos los inputs y se los procesa.
        ArrayList<ProductosEnIngresos> prodsEnIngresos = new ArrayList<>();
        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    ArrayList<String> IdProductos = null;

                    if (insertBDD(fecha, detalle, String.valueOf(campoTotal.getText()), prodsEnIngresos) != -1) {
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private long insertBDD(String fecha, String detalle, String monto, ArrayList<ProductosEnIngresos> prodsEnIngresos){
        int idTipo = 2;
        return dbHelper.addIngreso(fecha, monto, detalle, idTipo, prodsEnIngresos);
    }
}
