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
    private ArrayList<String> arrayMovId, arrayMovFecha, arrayMovDetalle, arrayMovMonto, arrayMovIdPedidos, arrayMovIdTipo;
    CustomAdapterCajaDiaria customAdapter;

    boolean filtroFecha;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        // Ni el detalle ni el pedidos afectados se usa para el recyclerview, pero se necesita
        // almacenar para poder ver todos los datos del movimiento luego
        arrayMovId = new ArrayList<>();
        arrayMovFecha = new ArrayList<>();
        arrayMovMonto = new ArrayList<>();
        arrayMovDetalle = new ArrayList<>();
        arrayMovIdPedidos = new ArrayList<>();
        arrayMovIdTipo = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caja_diaria, container, false);

        buttonFiltrar = view.findViewById(R.id.buttonFiltrarCajaDiaria);
        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    buttonFiltrar.setText(dateFormat.format(calendar.getTime()));

                    addElementsToRecyclerView();

                }, year, month, day);
                datePickerDialog.show();
            }
        });

        buttonResetFiltro = view.findViewById(R.id.buttonResetFiltro);
        buttonResetFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpiamos el campo de filtro y llamamos para refrescar los datos del recyclerview
                buttonFiltrar.setText("");
                addElementsToRecyclerView();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView_cajaDiaria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addButton = view.findViewById(R.id.addButton_caja_diaria);
        addButton.setOnClickListener(v -> openAddDialog());

        addElementsToRecyclerView();

        return view;
    }

    @Override // Usamos este método para que, si borramos o actualizamos desde la actividad MasInfo, se actualice la lista
    public void onResume() {
        super.onResume();
        bddToArraylist();
        addElementsToRecyclerView();
    }

    private void addElementsToRecyclerView(){
        bddToArraylist();
        customAdapter = new CustomAdapterCajaDiaria(getActivity(), getContext(), arrayMovId, arrayMovFecha,
                arrayMovDetalle, arrayMovMonto, arrayMovIdPedidos, arrayMovIdTipo);
        recyclerView.setAdapter(customAdapter);
    }

    private void bddToArraylist(){
        // Vacío los arraylist antes de volverles a insertar toda la BDD para evitar duplicados
        arrayMovId.clear();
        arrayMovFecha.clear();
        arrayMovMonto.clear();
        arrayMovDetalle.clear();
        arrayMovIdPedidos.clear();
        arrayMovIdTipo.clear();


        Cursor cursor = null;
        if (!buttonFiltrar.getText().toString().isEmpty()){
            cursor = dbHelper.getMovimientosByFecha(buttonFiltrar.getText().toString());
        }else{
            cursor = dbHelper.getAllMovimientos();
        }

        // Si obtuvimos datos, volcarlos en los arraylists
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                arrayMovId.add(cursor.getString(0));
                arrayMovFecha.add(cursor.getString(1));
                arrayMovDetalle.add(cursor.getString(2));
                arrayMovMonto.add(cursor.getString(3));
                arrayMovIdPedidos.add(cursor.getString(4));

                // TODO: puede ser que mueva esta lógica a otro lado para guardar los ID tal cual como en la BDD acá
                String checkVentaCompra = cursor.getString(5);
                // Si el id_tipo es 1, 2 o 4. Significa que es una venta simple, detallada o un cobro
                if (checkVentaCompra.equals("1") || checkVentaCompra.equals("2") || checkVentaCompra.equals("4") ){
                    arrayMovIdTipo.add("Entrada");
                } else{
                    arrayMovIdTipo.add("Salida");
                }
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
                    String monto = editTextMonto.getText().toString();
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

    private long insertBDD(String fecha, String detalle, String monto, boolean esEntrada, boolean esVentaOCompra){
        int id_tipo = -1;
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
