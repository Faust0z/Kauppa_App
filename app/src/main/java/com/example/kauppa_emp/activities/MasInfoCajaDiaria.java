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
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.database.dataObjects.Movimientos;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MasInfoCajaDiaria extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private TextInputEditText movTextoFecha, movTextoMonto, movTextoDetalle, movTextoIdPedidos, movTextoIdTipos;
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

        getIntentData();
        setIntentDataInTxt();
        comprobarDelUpdtButtons();
    }

    private void comprobarDelUpdtButtons() {
        MaterialButton actualizarButton = findViewById(R.id.buttonCajaDiariaUpdate);
        MaterialButton anularButton = findViewById(R.id.buttonCajaDiariaAnular);
        String intentIdTipos = getIntent().getStringExtra("movIdTipos");

        if (Objects.equals(intentIdTipos, TiposMovimiento.VARIOSEGR) || Objects.equals(intentIdTipos, TiposMovimiento.VARIOSING)){
            actualizarButton.setOnClickListener(v -> createUpdateDialog());
            anularButton.setOnClickListener(v -> createDeleteDialog());
        }
        else{
            actualizarButton.setEnabled(false);
            anularButton.setEnabled(false);
        }
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")){
            String id = getIntent().getStringExtra("movId");
            String fecha = getIntent().getStringExtra("movFecha");
            String monto = getIntent().getStringExtra("movMonto");
            String detalle = getIntent().getStringExtra("movDetalle");
            String idTipos = getIntent().getStringExtra("movIdTipos");
            boolean esPedido = getIntent().getBooleanExtra("esPedido", false);

            movimiento = new Movimientos(id, fecha, monto, detalle, idTipos);
            if (esPedido){movimiento.setEsPedido(true);}
        }
    }

    void setIntentDataInTxt(){
        if (movimiento.isPedido()){
            movTitulo.setText("Pedido N°" + movimiento.getId());
        }else{
            movTitulo.setText("Información - " + dbHelper.getTipoMovById(movimiento.getIdTipo()) + " #" + movimiento.getId());
        }
        movTextoFecha.setText(movimiento.getFecha());
        movTextoMonto.setText(movimiento.getMonto());
        movTextoDetalle.setText(movimiento.getDetalle());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Actualizar Movimiento");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String fecha = movTextoFecha.getText().toString().trim();
            String monto = movTextoMonto.getText().toString().trim();
            String detalle = movTextoDetalle.getText().toString().trim();

            dbHelper.updtMovimiento(movimiento.getId(), fecha, monto, detalle);
            finish(); // Hacemos finish para cerrar el intent
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar Movimiento");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Eliminar", (dialogInterface, i) -> {
            dbHelper.delMovimiento(movimiento.getId());
            finish(); // Hacemos finish para cerrar el intent
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});

        builder.create().show();
    }
}