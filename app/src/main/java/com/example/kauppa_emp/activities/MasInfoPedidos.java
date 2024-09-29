package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.kauppa_emp.database.dataObjects.Pedidos;
import com.example.kauppa_emp.database.dataObjects.Productos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterProdsAsociados;
import com.example.kauppa_emp.fragments.ProdsAsociadosFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoPedidos extends AppCompatActivity {
    protected DatabaseHelper dbHelper;

    protected TextView movTitulo;

    protected TextInputLayout spinner_Estado_PedidoInfo_box;
    protected TextInputEditText movTextoNomCliente, movTextoFechaEntrega, movTextoDetalle, movTextoTotal, movTextoCelCliente, movTextoSenia, movTextoResto;
    protected MaterialButton button_Volver, button_AddProdPerso_PedidosInfo, button_AddProdStock_PedidosInfo, actualizarButton, anularButton;
    protected AutoCompleteTextView spinnerTipo;
    protected RecyclerView recyclerView;
    protected CustomAdapterProdsAsociados customAdapterProdsAsociados;

    private LinearLayout linearLay_MasInfo;
    private FrameLayout frameLay_ProdsEnStock;

    protected Pedidos pedidoActual;
    private ArrayList<Productos> prodsAsociados;
    private ArrayList<Productos> prodsAgregados;
    protected boolean prodsModificados = false;
    private boolean layoutMasInfoVisible = true;
    private boolean isUserInput = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_pedidos);
        dbHelper = new DatabaseHelper(MasInfoPedidos.this);
        prodsAgregados = new ArrayList<>();

        vincularComponentes();
        getIntentData();
        setData();

        configurarSpinner();
        configurarRecView();
        configurarCampoFecha();
        configurarLayoutProds();


        button_Volver.setOnClickListener(v -> alternarLayouts());
        button_AddProdPerso_PedidosInfo.setOnClickListener(v -> openAddDialogProdPerso());
        button_AddProdStock_PedidosInfo.setOnClickListener(v -> alternarLayouts());
        actualizarButton.setOnClickListener(v -> createUpdateDialog());
        anularButton.setOnClickListener(v -> createDeleteDialog());

        // Es necesario desvincularle cualquier TextWatcher al campo de cantidad primero
        if (movTextoSenia.getTag() instanceof TextWatcher) {
            movTextoSenia.removeTextChangedListener((TextWatcher) movTextoSenia.getTag());
        }

        movTextoSenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isUserInput = after > 0;  // Esto es para asegurarnos de que es input del usuario y no del código que disparó la llamada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(movTextoSenia.getText()).isEmpty()){
                    movTextoResto.setText(customAdapterProdsAsociados.getPrecioTotal());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserInput) return;
                calcularTotalYResto();
            }
        });

        // Es necesario desvincularle cualquier TextWatcher al campo de cantidad primero
        if (movTextoTotal.getTag() instanceof TextWatcher) {
            movTextoTotal.removeTextChangedListener((TextWatcher) movTextoTotal.getTag());
        }

        movTextoTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isUserInput = after > 0;  // Esto es para asegurarnos de que es input del usuario y no del código que disparó la llamada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserInput) return;
                calcularTotalYResto();
            }
        });

        movTextoTotal.setText(customAdapterProdsAsociados.getPrecioTotal());
        calcularTotalYResto();
    }

    private void calcularTotalYResto() {
        String seniaStr = String.valueOf(movTextoSenia.getText());
        String totalStr = String.valueOf(movTextoTotal.getText());

        if (!totalStr.isEmpty()){
            BigDecimal resto = new BigDecimal(totalStr).subtract(new BigDecimal(seniaStr));
            movTextoResto.setText(resto.toString());
        } else{
            movTextoResto.setText(customAdapterProdsAsociados.getPrecioTotal());
        }

    }

    void vincularComponentes(){
        movTitulo = findViewById(R.id.textView_Titulo_PedidoInfo);
        movTextoNomCliente = findViewById(R.id.editText_NomClient_PedidoInfo);
        movTextoCelCliente = findViewById(R.id.editText_CelClient_PedidoInfo);
        movTextoFechaEntrega = findViewById(R.id.editText_FechaEnt_PedidoInfo);
        movTextoDetalle = findViewById(R.id.editText_Detalle_PedidoInfo);
        movTextoSenia = findViewById(R.id.editText_Senia_PedidoInfo);
        movTextoResto = findViewById(R.id.editText_Resto_PedidoInfo);
        movTextoTotal = findViewById(R.id.editText_Total_PedidoInfo);
        spinnerTipo = findViewById(R.id.spinner_Estado_PedidoInfo);
        spinner_Estado_PedidoInfo_box = findViewById(R.id.spinner_Estado_PedidoInfo_box);
        recyclerView = findViewById(R.id.recView_PedidoInfo);
        frameLay_ProdsEnStock = findViewById(R.id.frameLay_ProdsEnStock);
        linearLay_MasInfo = findViewById(R.id.linearLay_MasInfo);
        button_Volver = findViewById(R.id.button_Volver);
        button_AddProdPerso_PedidosInfo = findViewById(R.id.button_AddProdPerso_PedidoInfo);
        button_AddProdStock_PedidosInfo = findViewById(R.id.button_AddProdStock_PedidoInfo);
        actualizarButton = findViewById(R.id.button_Update_PedidoInfo);
        anularButton = findViewById(R.id.button_Eliminar_PedidoInfo);
    }

    protected void getIntentData() {
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")) {
            String id = getIntent().getStringExtra("movId");
            String fecha = getIntent().getStringExtra("movFecha");
            String fechaEntrega = getIntent().getStringExtra("movFechaEntrega");
            String detalle = getIntent().getStringExtra("movDetalle");
            String resto = getIntent().getStringExtra("movResto");
            String senia = getIntent().getStringExtra("movSenia");
            String total = getIntent().getStringExtra("movTotal");
            String nomCliente = getIntent().getStringExtra("movNomCliente");
            String celCliente = getIntent().getStringExtra("movCelCliente");
            String idTipos = getIntent().getStringExtra("movIdTipos");

            pedidoActual = new Pedidos(id, fecha, fechaEntrega, detalle, senia, total, resto, nomCliente, celCliente, idTipos);
        }
    }


    private void configurarSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TiposMovimiento.relleSpinnerPedidos());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(arrayAdapter);
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
        prodsAsociados = Productos.getProdsPorPedid(dbHelper, pedidoActual.getId());

        customAdapterProdsAsociados = new CustomAdapterProdsAsociados(this, prodsAsociados, actualizarButton);
        customAdapterProdsAsociados.setProdEditadoListener(() -> {
            movTextoTotal.setText(customAdapterProdsAsociados.getPrecioTotal());
            prodsModificados = true;
        });
        recyclerView.setAdapter(customAdapterProdsAsociados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configurarCampoFecha() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFechaEntrega.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoPedidos.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFechaEntrega.setText(dateFormat.format(calendar.getTime()));
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
        movTitulo.setText("Información - Pedido #" + pedidoActual.getId());
        spinnerTipo.setText(TiposMovimiento.getEstadoById(pedidoActual.getIdEstado()), false);
        // setSpinnerToValue(spinnerTipo, TiposMovimiento.getEstadoById(pedidoActual.getIdEstado()));
        movTextoNomCliente.setText(pedidoActual.getNomCliente().equals("null") ? "Cliente sin registrar" : pedidoActual.getNomCliente());
        movTextoCelCliente.setText(pedidoActual.getCelCliente().equals("null") ? "Celular sin registrar" : pedidoActual.getCelCliente());
        movTextoFechaEntrega.setText(pedidoActual.getFechaEntrega());
        movTextoDetalle.setText(pedidoActual.getDetalle());
        movTextoSenia.setText(pedidoActual.getSenia());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Actualizar el Pedido N° " + pedidoActual.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            String nomCliente = String.valueOf(movTextoNomCliente.getText()).trim();
            String celCliente = String.valueOf(movTextoCelCliente.getText()).trim();
            String fecha_entrega = String.valueOf(movTextoFechaEntrega.getText()).trim();
            String detalle = String.valueOf(movTextoDetalle.getText()).trim();
            String senia = String.valueOf(movTextoSenia.getText()).trim();
            String resto = String.valueOf(movTextoSenia.getText()).trim();
            String total = String.valueOf(movTextoTotal.getText()).trim();

            int estado = TiposMovimiento.estadoToId(spinnerTipo.getEditableText().toString());
            dbHelper.updtPedido(pedidoActual.getId(), fecha_entrega, detalle, senia, resto, total, nomCliente, celCliente, estado);

            String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

            if (prodsModificados){
                gestionarProdsAsociados(fechaActual, pedidoActual.getId());
            }
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }

    protected void gestionarProdsAsociados(String fechaActual, String idPedidoActual){
        for (Productos prod: prodsAgregados) {
            if (prod.esFantasma()) { // Si el prod es fantasma, entonces no existe y lo agregamos a la tabla prods primero
                dbHelper.addProducto(prod.getNombre(), prod.getStock(), fechaActual, prod.getPrecioUnitario(), prod.esFantasma());
                Productos ultimoProdInsertado = Productos.getUltimoProducto(dbHelper.getUltimoProducto());
                dbHelper.addProductosPdid(idPedidoActual, ultimoProdInsertado.getId(), prod.getCant());
            } else{ // Sino ya es un prod existente, por lo que modificamos su stock
                String nuevoStock = String.valueOf(Integer.parseInt(prod.getStock()) - prod.getCant());
                dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
                dbHelper.addProductosPdid(idPedidoActual, prod.getId(), prod.getCant());
            }
        }
        for (Productos prod: prodsAsociados){
            dbHelper.updtProductosPdid(idPedidoActual, prod.getId(), prod.getCant());

            String nuevoStock = String.valueOf(prod.getStockSinEditar() - prod.getCant());
            if (prod.esRecienAgregado()){
                nuevoStock = String.valueOf(Integer.parseInt(prod.getStock()) - prod.getCant());
            }
            dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
        }
        for (Productos prod: customAdapterProdsAsociados.getProdsEliminados()){
            dbHelper.delProductosPdid(idPedidoActual, prod.getId());
            String nuevoStock = String.valueOf(prod.getStockSinEditar());
            dbHelper.updtProducto(prod.getId(), prod.getNombre(), nuevoStock, fechaActual, prod.getPrecioUnitario());
        }
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar el Pedido " + pedidoActual.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            dbHelper.delPedido(pedidoActual.getId());
            for (Productos prod: prodsAsociados){
                dbHelper.delProductosPdid(pedidoActual.getId(), prod.getId());
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