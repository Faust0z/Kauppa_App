package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoVentas extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private TextInputEditText movTextoFecha, movTextoMonto, movTextoDetalle, movTextoNomCliente;
    private Ingresos ingreso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_ingresos);
        dbHelper = new DatabaseHelper(MasInfoVentas.this);

        movTitulo = findViewById(R.id.textViewTituloVentaInfo);
        movTextoFecha = findViewById(R.id.editTextFechaVentaInfo);
        movTextoMonto = findViewById(R.id.editTextMontoVentaInfo);
        movTextoDetalle = findViewById(R.id.editTextDetalleVentaInfo);
        movTextoNomCliente = findViewById(R.id.editTextClienteVentaInfo);

        getIntentData();
        setIntentDataInTxt();

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoVentas.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);

            datePickerDialog.show();
        });

        Button actualizarButton = findViewById(R.id.buttonVentaUpdate);
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUpdateDialog();
            }
        });

        Button anularButton = findViewById(R.id.buttonVentaAnular);
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
            String monto = getIntent().getStringExtra("movMonto");
            String detalle = getIntent().getStringExtra("movDetalle");
            String idTipos = getIntent().getStringExtra("movIdTipos");
            String nomCliente = getIntent().getStringExtra("movNomCliente");
            if (nomCliente.equals("null")){nomCliente = "Cliente sin registrar";} //Todo: seguramente sea mejor pasar esta lógica a otro lado

            ingreso = new Ingresos(id, fecha, monto, detalle, idTipos, nomCliente);
        }
    }

    void setIntentDataInTxt(){
        movTitulo.setText("Información - Ingreso #" + ingreso.getId());
        movTextoFecha.setText(ingreso.getFecha());
        movTextoMonto.setText(ingreso.getMonto());
        movTextoDetalle.setText(ingreso.getDetalle());
        movTextoNomCliente.setText(ingreso.getNomCliente());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Actualizar Ingreso");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String fecha = String.valueOf(movTextoFecha.getText()).trim();
            String monto = String.valueOf(movTextoMonto.getText()).trim();
            String detalle = String.valueOf(movTextoDetalle.getText()).trim();
            String nomCliente =  String.valueOf(movTextoNomCliente.getText()).trim();

            dbHelper.updtIngreso(ingreso.getId(), fecha, monto, detalle, nomCliente);
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});

        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar Ingreso");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Eliminar", (dialogInterface, i) -> {
            dbHelper.delIngreso(ingreso.getId());
            finish(); // Con finish se cierra el intent
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});

        builder.create().show();
    }
}