package com.example.kauppa_emp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.fragments.dataObjects.Egresos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoCompras extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private EditText movTextoFecha, movTextoDetalle, movTextoMonto, movTextoIdPedidos, movTextoIdTipos, movTextoNomCliente;
    private Egresos egreso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_compras);
        dbHelper = new DatabaseHelper(MasInfoCompras.this);

        movTitulo = findViewById(R.id.textViewTituloCompraInfo);
        movTextoFecha = findViewById(R.id.editTextFechaCompraInfo);
        movTextoMonto = findViewById(R.id.editTextMontoCompraInfo);
        movTextoNomCliente = findViewById(R.id.editTextClienteCompraInfo);
        movTextoDetalle = findViewById(R.id.editTextDetalleCompraInfo);

        getIntentData();
        setIntentDataInTxt();

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoCompras.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        Button actualizarButton = findViewById(R.id.buttonCompraUpdate);
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUpdateDialog();
            }
        });

        Button anularButton = findViewById(R.id.buttonCompraAnular);
        anularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeleteDialog();
            }
        });
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")){
            String id = getIntent().getStringExtra("movId");
            String fecha = getIntent().getStringExtra("movFecha");
            String detalle = getIntent().getStringExtra("movDetalle");
            String monto = getIntent().getStringExtra("movMonto");
            String idTipos = getIntent().getStringExtra("movIdTipos");
            String movNomCliente = getIntent().getStringExtra("movNomCliente");

            egreso = new Egresos(id, fecha, monto, detalle, idTipos, movNomCliente);
        }
    }

    void setIntentDataInTxt(){
        movTitulo.setText("Compra N° " + egreso.getId());
        movTextoFecha.setText(egreso.getFecha());
        movTextoMonto.setText(egreso.getMonto());
        movTextoDetalle.setText(egreso.getDetalle());
        movTextoNomCliente.setText(egreso.getNomCliente());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar la compra N° " + egreso.getId() + "?");
        builder.setMessage("Desea actualizar la compra " + egreso.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            String fecha = movTextoFecha.getText().toString().trim();
            String monto = movTextoMonto.getText().toString().trim();
            String detalle = movTextoDetalle.getText().toString().trim();
            String nomCliente =  movTextoNomCliente.getText().toString().trim();

            dbHelper.updtEgreso(egreso.getId(), fecha, monto, detalle, nomCliente);
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar la venta " + egreso.getId() + "?");
        builder.setMessage("Desea eliminar la venta " + egreso.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            dbHelper.delEgreso(egreso.getId());
            finish(); // Con finish se cierra el intent
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }
}