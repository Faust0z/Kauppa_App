package com.example.kauppa_emp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kauppa_emp.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoVentas extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private EditText movTextoFecha, movTextoDetalle, movTextoMonto, movTextoIdPedidos, movTextoIdTipos, movTextoNomCliente;
    private Button actualizarButton, anularButton;

    private String movId, movFecha, movDetalle, movMonto, movIdPedidos, movIdTipos, movNomCliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_ventas);
        dbHelper = new DatabaseHelper(MasInfoVentas.this);

        // Por las dudas, no existe movTextoId. El ID lo agrego al título
        movTitulo = findViewById(R.id.textViewTituloVentaInfo);
        movTextoFecha = findViewById(R.id.editTextFechaVentaInfo);
        movTextoMonto = findViewById(R.id.editTextMontoVentaInfo);
        movTextoNomCliente = findViewById(R.id.editTextClienteVentaInfo);
        movTextoDetalle = findViewById(R.id.editTextDetalleVentaInfo);

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

        actualizarButton = findViewById(R.id.buttonVentaUpdate);
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUpdateDialog();
            }
        });

        anularButton = findViewById(R.id.buttonVentaAnular);
        anularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeleteDialog();
            }
        });
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("id")){
            movId = getIntent().getStringExtra("id");
            movFecha = getIntent().getStringExtra("fecha");
            movDetalle = getIntent().getStringExtra("detalle");
            movMonto = getIntent().getStringExtra("monto");
            movIdPedidos = getIntent().getStringExtra("IdPedidos");
            movIdTipos = getIntent().getStringExtra("IdTipos");
            movNomCliente = getIntent().getStringExtra("nombreCliente");
            if (movNomCliente.equals("null")){movNomCliente = "Cliente sin registrar";}
        }
    }

    void setIntentDataInTxt(){
        movTitulo.setText("Venta N° " + movId);
        movTextoFecha.setText(movFecha);
        movTextoMonto.setText(movMonto);
        movTextoDetalle.setText(movDetalle);
        // movTextoIdPedidos.setText(movIdPedidos);
        movTextoNomCliente.setText(movNomCliente);
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar " + movIdTipos + " " + movId + "?");
        builder.setMessage("Desea actualizar la " + movIdTipos + " " + movId + "?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                movFecha = movTextoFecha.getText().toString().trim();
                movMonto = movTextoMonto.getText().toString().trim();
                movNomCliente = movTextoNomCliente.getText().toString().trim();
                movDetalle = movTextoDetalle.getText().toString().trim();
                dbHelper.updtMovimiento(movId, movFecha, movMonto, movNomCliente, movDetalle);
                finish(); // Con finish se cierra el intent
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar " + movIdTipos + " " + movId + "?");
        builder.setMessage("Desea eliminar la " + movIdTipos + " " + movId + "?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.delMovimiento(movId);
                finish(); // Con finish se cierra el intent
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}