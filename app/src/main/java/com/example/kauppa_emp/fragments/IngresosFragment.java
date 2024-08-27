package com.example.kauppa_emp.fragments;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterIngresos;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.ViewGroup;

import java.util.Collections;

public class IngresosFragment extends BaseFragment<Ingresos> {
    private FloatingActionButton addButton;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ingresos;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_Ingresos;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_Ingresos;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_Ingresos;
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
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
        return new CustomAdapterIngresos(getActivity(), getContext(), items);
    }

    @Override
    protected void bddToArraylist() {
        items = Ingresos.bddToArraylist(dbHelper.getAllIngresos());
        Collections.reverse(items);
    }

    protected void openAddDialog() {
        /*
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingreso, null);

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
        CustomAdapterProdsIngresoDialog adapter = new CustomAdapterProdsIngresoDialog(getContext(), arrayProdNombre);
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

                    float result = dbHelper.addIngreso(fecha, String.valueOf(campoTotal.getText()), detalle, 2, prodsEnIngresos);
                    if (result != -1) {
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
         */
    }
}
