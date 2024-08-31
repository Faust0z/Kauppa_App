package com.example.kauppa_emp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Egresos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoEgresos extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private EditText movTextoNomProv, movTextoFecha, movTextoMonto, movTextoDetalle;

    private Egresos egresoActual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_egresos);
        dbHelper = new DatabaseHelper(MasInfoEgresos.this);

        movTitulo = findViewById(R.id.textView_Titulo_EgresoInfo);
        movTextoFecha = findViewById(R.id.editText_Fecha_EgresoInfo);
        movTextoMonto = findViewById(R.id.editText_Monto_EgresoInfo);
        movTextoNomProv = findViewById(R.id.editText_NomProv_EgresoInfo);
        movTextoDetalle = findViewById(R.id.editText_Detalle_EgresoInfo);

        getIntentData();

        setData();

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        movTextoFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MasInfoEgresos.this, (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                movTextoFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        Button actualizarButton = findViewById(R.id.button_Update_EgresoInfo);
        actualizarButton.setOnClickListener(v -> createUpdateDialog());

        Button anularButton = findViewById(R.id.button_Eliminar_EgresoInfo);
        anularButton.setOnClickListener(v -> createDeleteDialog());
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("movId")){
            String id = getIntent().getStringExtra("movId");
            String NomProv = getIntent().getStringExtra("movNomProv");
            String idTipos = getIntent().getStringExtra("movIdTipos");
            String monto = getIntent().getStringExtra("movMonto");
            String fecha = getIntent().getStringExtra("movFecha");
            String detalle = getIntent().getStringExtra("movDetalle");

            egresoActual = new Egresos(id, fecha, monto, detalle, idTipos, NomProv);
        }
    }

    void setData(){
        movTitulo.setText("Información - Egreso #" + egresoActual.getId());
        movTextoNomProv.setText(egresoActual.getNomProv());
        movTextoFecha.setText(egresoActual.getFecha());
        movTextoMonto.setText(egresoActual.getMonto());
        movTextoDetalle.setText(egresoActual.getDetalle());
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

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Egreso");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String fecha = movTextoFecha.getText().toString().trim();
            String monto = movTextoMonto.getText().toString().trim();
            String detalle = movTextoDetalle.getText().toString().trim();
            String nomProv =  movTextoNomProv.getText().toString().trim();

            dbHelper.updtEgreso(egresoActual.getId(), fecha, monto, detalle, nomProv);
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar Egreso");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Eliminar", (dialogInterface, i) -> {
            dbHelper.delEgreso(egresoActual.getId());
            finish(); // Con finish se cierra el intent
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});

        builder.create().show();
    }
}