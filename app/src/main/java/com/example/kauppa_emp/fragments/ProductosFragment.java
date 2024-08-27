package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.Movimientos;
import com.example.kauppa_emp.database.dataObjects.Productos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProductos;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class ProductosFragment extends BaseFragment<Productos> {
    protected TextView textView_Titulo_Productos,
            textView_totalCant_Prods,
            textView_StkBajoCant_Prods,
            textView_SinStkCant_Prods;

    private FloatingActionButton addButton;

    private int stockMinimo = 3; //Valor según el cuál se calcula los prods bajos en stock

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_productos;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_Prods;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_Prods;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_Prods;
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        textView_Titulo_Productos = view.findViewById(R.id.textView_Titulo_Productos);
        textView_totalCant_Prods = view.findViewById(R.id.textView_totalCant_Prods);
        textView_StkBajoCant_Prods = view.findViewById(R.id.textView_StkBajoCant_Prods);
        textView_SinStkCant_Prods = view.findViewById(R.id.textView_SinStkCant_Prods);

        addButton = view.findViewById(getAddButtonId());
        addButton.setOnClickListener(v -> openAddDialog());

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        calcularCantsProds();
    }

    private void calcularCantsProds(){
        int sumaTotalProds = 0;
        int sumaProdsBajStock = 0;
        int sumaProdsSinStock = 0;
        for (Productos prod : items) {
            int stock = Integer.parseInt(prod.getStock());
            if (stock <= stockMinimo){
                if (stock <= 0){
                    sumaProdsSinStock++;
                    break;
                }
                sumaProdsBajStock++;
            }
            sumaTotalProds += stock;
        }

        textView_totalCant_Prods.setText(String.valueOf(sumaTotalProds));
        textView_StkBajoCant_Prods.setText(String.valueOf(sumaProdsBajStock));
        textView_SinStkCant_Prods.setText(String.valueOf(sumaProdsSinStock));
    }

    @Override
    protected void bddToArraylist() {
        items = Productos.bddToArraylist(dbHelper.getAllProductos());
        Collections.reverse(items);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterProductos(getActivity(), getContext(), items);
    }

    protected void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_prod, null);

        EditText editText_NombreProd_Prod = dialogView.findViewById(R.id.editText_NombreProd_Prod);
        EditText editText_CantInic_Prod = dialogView.findViewById(R.id.editText_CantInic_Prod);
        EditText editText_PrecUnit_Prod = dialogView.findViewById(R.id.editText_PrecUnit_Prod);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String nomProd = editText_NombreProd_Prod.getText().toString();
                    String cantInic = editText_CantInic_Prod.getText().toString();
                    String precUnit = editText_PrecUnit_Prod.getText().toString();

                    String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

                    float result = dbHelper.addProducto(nomProd, cantInic, fechaActual, precUnit, false);
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

}