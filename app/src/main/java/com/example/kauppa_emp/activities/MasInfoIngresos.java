package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.example.kauppa_emp.database.dataObjects.Productos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProdsAsociados;
import com.example.kauppa_emp.fragments.ProdsAsociadosFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoIngresos extends AppCompatActivity {
    protected DatabaseHelper dbHelper;

    protected TextView movTitulo;
    protected EditText movTextoNomCliente, movTextoFecha, movTextoDetalle, movTextoTotal;
    protected Button button_Volver, button_AddProdPerso_IngresosInfo, button_AddProdStock_IngresosInfo, actualizarButton, anularButton;
    protected Spinner spinnerTipo;
    protected RecyclerView recyclerView;
    protected CustomAdapterProdsAsociados customAdapterProdsAsociados;

    private LinearLayout linearLay_MasInfo;
    private FrameLayout frameLay_ProdsEnStock;

    protected Ingresos ingresoActual;
    private ArrayList<Productos> prodsAsociados;
    private ArrayList<Productos> prodsAgregados;
    protected boolean prodsModificados = false;
    private boolean layoutMasInfoVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_ingresos);
        dbHelper = new DatabaseHelper(MasInfoIngresos.this);
        prodsAgregados = new ArrayList<>();

        vincularComponentes();
        getIntentData();

        configurarSpinner();
        configurarRecView();
        configurarCampoFecha();
        configurarLayoutProds();

        setData();

        button_Volver.setOnClickListener(v -> alternarLayouts());
        button_AddProdPerso_IngresosInfo.setOnClickListener(v -> openAddDialogProdPerso());
        button_AddProdStock_IngresosInfo.setOnClickListener(v -> alternarLayouts());
        actualizarButton.setOnClickListener(v -> createUpdateDialog());
        anularButton.setOnClickListener(v -> createDeleteDialog());
    }

    void vincularComponentes(){
        movTitulo = findViewById(R.id.textView_Titulo_IngresoInfo);
        movTextoNomCliente = findViewById(R.id.editText_NomClient_IngresoInfo);
        movTextoFecha = findViewById(R.id.editText_Fecha_IngresoInfo);
        movTextoDetalle = findViewById(R.id.editText_Detalle_IngresoInfo);
        movTextoTotal = findViewById(R.id.editText_Total_IngresoInfo);
        spinnerTipo = findViewById(R.id.spinner_Tipo_IngresoInfo);
        recyclerView = findViewById(R.id.recView_IngresosInfo);
        frameLay_ProdsEnStock = findViewById(R.id.frameLay_ProdsEnStock);
        linearLay_MasInfo = findViewById(R.id.linearLay_MasInfo);
        button_Volver = findViewById(R.id.button_Volver);
        button_AddProdPerso_IngresosInfo = findViewById(R.id.button_AddProdPerso_IngresosInfo);
        button_AddProdStock_IngresosInfo = findViewById(R.id.button_AddProdStock_IngresosInfo);
        actualizarButton = findViewById(R.id.button_Update_IngresoInfo);
        anularButton = findViewById(R.id.button_Eliminar_IngresoInfo);
    }

    protected void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")){
            String id = getIntent().getStringExtra("movId");
            String nomCliente = getIntent().getStringExtra("movNomCliente");
            String idTipos = getIntent().getStringExtra("movIdTipos");
            String fecha = getIntent().getStringExtra("movFecha");
            String monto = getIntent().getStringExtra("movMonto");
            String detalle = getIntent().getStringExtra("movDetalle");

            ingresoActual = new Ingresos(id, fecha, monto, detalle, idTipos, nomCliente);
        }
    }

    private void configurarSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TiposMovimiento.relleSpinnerIngresos());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(arrayAdapter);

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habDesProdsAsociados();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) spinner.getAdapter();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void configurarRecView() {
        prodsAsociados = Productos.getProdsPorIngr(dbHelper, ingresoActual.getId());

        customAdapterProdsAsociados = new CustomAdapterProdsAsociados(this, prodsAsociados, actualizarButton);
        customAdapterProdsAsociados.setProdEditadoListener(() -> {
            movTextoTotal.setText(customAdapterProdsAsociados.getPrecioTotal());
            prodsModificados = true;

        });
        recyclerView.setAdapter(customAdapterProdsAsociados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        habDesProdsAsociados();
    }

    private void habDesProdsAsociados() {
        if (spinnerTipo.getSelectedItem().toString().equals("Venta")) { // Si el ingreso es una venta, mostramos sus prods asociados
            recyclerView.setVisibility(View.VISIBLE);
            button_AddProdPerso_IngresosInfo.setVisibility(View.VISIBLE);
            button_AddProdStock_IngresosInfo.setVisibility(View.VISIBLE);
            movTextoTotal.setFocusable(true);
        }
        else { // Si el prod no es una venta, no mostramos lo relacionado a productos
                recyclerView.setVisibility(View.GONE);
                button_AddProdPerso_IngresosInfo.setVisibility(View.GONE);
                button_AddProdStock_IngresosInfo.setVisibility(View.GONE);
                movTextoTotal.setFocusable(false);
        }
    }

    private void configurarCampoFecha() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoIngresos.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void configurarLayoutProds() {
        // Esto es un fragmentLayout que usa el mismo fragmento de la pantalla Productos, pero que le
        // cambia un par de detalles para lo que necesitamos acá
        Fragment ProdsAsociadosFragment = new ProdsAsociadosFragment(prodsAsociados, prodsAgregados, customAdapterProdsAsociados);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLay_ProdsEnStock, ProdsAsociadosFragment);
        fragmentTransaction.commit();
    }

    private void alternarLayouts() {
        if (layoutMasInfoVisible) {
            frameLay_ProdsEnStock.setVisibility(View.VISIBLE);
            linearLay_MasInfo.setVisibility(View.INVISIBLE);
        } else {
            frameLay_ProdsEnStock.setVisibility(View.INVISIBLE);
            linearLay_MasInfo.setVisibility(View.VISIBLE);
        }
        layoutMasInfoVisible = !layoutMasInfoVisible;
        prodsModificados = true;
        customAdapterProdsAsociados.notifyDataSetChanged();
    }

    protected void setData(){
        movTitulo.setText("Ingreso N° " + ingresoActual.getId());
        movTextoNomCliente.setText(ingresoActual.getNomCliente().equals("null") ? "Cliente sin registrar" : ingresoActual.getNomCliente());
        setSpinnerToValue(spinnerTipo, TiposMovimiento.getTipoById(ingresoActual.getIdTipo()));
        movTextoFecha.setText(ingresoActual.getFecha());
        movTextoTotal.setText(ingresoActual.getMonto());
        movTextoDetalle.setText(ingresoActual.getDetalle());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Actualizar el Ingreso N° " + ingresoActual.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            String fecha = movTextoFecha.getText().toString().trim();
            String total = movTextoTotal.getText().toString().trim();
            String detalle = movTextoDetalle.getText().toString().trim();
            String nomCliente =  movTextoNomCliente.getText().toString().trim();

            int tipo = TiposMovimiento.nombreToId(spinnerTipo.getSelectedItem().toString());
            dbHelper.updtIngreso(ingresoActual.getId(), fecha, total, detalle, tipo, nomCliente);

            String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

            if (prodsModificados){
                gestionarProdsAsociados(fechaActual, ingresoActual.getId());
            }
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }

    protected void gestionarProdsAsociados(String fechaActual, String idIngresoActual){
        for (Productos prod: prodsAgregados) {
            if (prod.esFantasma()) { // Si el prod es fantasma, entonces no existe y lo agregamos a la tabla prods primero
                dbHelper.addProducto(prod.getNombre(), prod.getStock(), fechaActual, prod.getPrecioUnitario(), prod.esFantasma());
                Productos ultimoProdInsertado = Productos.getUltimoProducto(dbHelper.getUltimoProducto());
                dbHelper.addProductosIngr(idIngresoActual, ultimoProdInsertado.getId(), prod.getCant());
            } else{ // Sino ya es un prod existente, por lo que modificamos su stock
                String nuevoStock = String.valueOf(Integer.parseInt(prod.getStock()) - prod.getCant());
                dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
                dbHelper.addProductosIngr(idIngresoActual, prod.getId(), prod.getCant());
            }
        }
        for (Productos prod: prodsAsociados){
            dbHelper.updtProductosIngr(idIngresoActual, prod.getId(), prod.getCant());

            String nuevoStock = String.valueOf(prod.getStockSinEditar() - prod.getCant());
            if (prod.esRecienAgregado()){
                nuevoStock = String.valueOf(Integer.parseInt(prod.getStock()) - prod.getCant());
            }
            dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
        }
        for (Productos prod: customAdapterProdsAsociados.getProdsEliminados()){
            dbHelper.delProductosIngr(idIngresoActual, prod.getId());
            String nuevoStock = String.valueOf(prod.getStockSinEditar());
            dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
        }
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar el Ingreso " + ingresoActual.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            dbHelper.delIngreso(ingresoActual.getId());
            for (Productos prod: prodsAsociados){
                dbHelper.delProductosIngr(ingresoActual.getId(), prod.getId());
            }
            prodsModificados = true;
            finish(); // Con finish se cierra el intent
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }

    protected void openAddDialogProdPerso() {
        // Dialog para agregar prods fantasma, sólo tiene nombre y precio unit y se guardará en
        // la bdd como fantasma
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_prod, null);

        EditText editText_NombreProd_Prod = dialogView.findViewById(R.id.editText_NombreProd_Prod);
        EditText editText_PrecUnit_Prod =  dialogView.findViewById(R.id.editText_PrecUnit_Prod);
        dialogView.findViewById(R.id.editText_CantInic_Prod).setVisibility(View.GONE);

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    prodsModificados = true;
                    String nomProd = editText_NombreProd_Prod.getText().toString();
                    String precioUniProd = editText_PrecUnit_Prod.getText().toString();

                    Productos newProd = new Productos("0", nomProd, "0", "0", precioUniProd, true);
                    newProd.setRecienAgregado(true);
                    prodsAgregados.add(newProd);
                    prodsAsociados.add(newProd);
                    customAdapterProdsAsociados.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}