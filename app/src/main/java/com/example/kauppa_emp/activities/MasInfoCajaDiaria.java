package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Movimientos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoCajaDiaria extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private EditText movTextoFecha, movTextoMonto, movTextoDetalle, movTextoIdPedidos, movTextoIdTipos;
    private Movimientos movimiento;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_caja_diaria);
        dbHelper = new DatabaseHelper(MasInfoCajaDiaria.this);

        movTitulo = findViewById(R.id.textViewTituloCajaDiariaInfo);
        movTextoFecha = findViewById(R.id.editTextFechaCajaDiariaInfo);
        movTextoMonto = findViewById(R.id.editTextMontoCajaDiariaInfo);
        movTextoDetalle = findViewById(R.id.editTextDetalleCajaDiariaInfo);
        movTextoIdPedidos = findViewById(R.id.editTextIdPedidosCajaDiariaInfo);

        getIntentData();
        setIntentDataInTxt();

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoCajaDiaria.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);

            datePickerDialog.show();
        });

        Button actualizarButton = findViewById(R.id.buttonCajaDiariaUpdate);
        actualizarButton.setOnClickListener(v -> createUpdateDialog());

        Button anularButton = findViewById(R.id.buttonCajaDiariaAnular);
        anularButton.setOnClickListener(v -> createDeleteDialog());
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")){
            String id = getIntent().getStringExtra("movId");
            String fecha = getIntent().getStringExtra("movFecha");
            String monto = getIntent().getStringExtra("movMonto");
            String detalle = getIntent().getStringExtra("movDetalle");
            String idTipos = getIntent().getStringExtra("movIdTipos");

            movimiento = new Movimientos(id, fecha, monto, detalle, idTipos);
        }
    }

    void setIntentDataInTxt(){
        movTitulo.setText(movimiento.getIdTipo() + " " + movimiento.getId());
        movTextoFecha.setText(movimiento.getFecha());
        movTextoMonto.setText(movimiento.getMonto());
        movTextoDetalle.setText(movimiento.getDetalle());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Actualizar " + movimiento.getIdTipo() + " N° " + movimiento.getId() +  "?");
        builder.setMessage("Desea actualizar la " + movimiento.getIdTipo() + " " + movimiento.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            String fecha = movTextoFecha.getText().toString().trim();
            String monto = movTextoMonto.getText().toString().trim();
            String detalle = movTextoDetalle.getText().toString().trim();

            dbHelper.updtMovimiento(movimiento.getId(), fecha, monto, detalle);
            finish(); // Hacemos finish para cerrar el intent
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar " + movimiento.getIdTipo() + " " + movimiento.getId() + "?");
        builder.setMessage("Desea eliminar la " + movimiento.getIdTipo() + " " + movimiento.getId() + "?");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            dbHelper.delMovimiento(movimiento.getId());
            finish(); // Hacemos finish para cerrar el intent
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }
}