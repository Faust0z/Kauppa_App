package com.example.kauppa_emp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.Productos;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasInfoProds extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private TextView movTitulo;
    private TextInputEditText prodTextNombre, prodTextStock, prodTextPrecUnit, prodTextFechaAct;

    private Productos productoActual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_prods);
        dbHelper = new DatabaseHelper(MasInfoProds.this);

        movTitulo = findViewById(R.id.textView_Titulo_ProdInfo);
        prodTextNombre = findViewById(R.id.editText_Nombre_ProdInfo);
        prodTextStock = findViewById(R.id.editText_Stock_ProdInfo);
        prodTextFechaAct = findViewById(R.id.editText_FechaAct_ProdInfo);
        prodTextPrecUnit = findViewById(R.id.editText_PrecUnit_ProdInfo);

        Button actualizarButton = findViewById(R.id.button_Update_ProdInfo);
        actualizarButton.setOnClickListener(v -> createUpdateDialog());

        Button anularButton = findViewById(R.id.button_Eliminar_ProdInfo);
        anularButton.setOnClickListener(v -> createDeleteDialog());

        getIntentData();
        setData();
    }

    void getIntentData(){
        // Compruebo que el intent haya traído datos. Asumo que si está el id están todos
        if (getIntent().hasExtra("prodId")){
            String id = getIntent().getStringExtra("prodId");
            String nombre = getIntent().getStringExtra("prodNombre");
            String stock = getIntent().getStringExtra("prodStock");
            String fechaAct = getIntent().getStringExtra("prodFechaAct");
            String precioUni = getIntent().getStringExtra("prodPrecioUni");

            productoActual = new Productos(id, nombre, stock, fechaAct, precioUni, false);
        }
    }

    void setData(){
        movTitulo.setText("Información - Producto #" + productoActual.getId());
        prodTextNombre.setText(productoActual.getNombre());
        prodTextStock.setText(productoActual.getStock());
        prodTextFechaAct.setText(productoActual.getFechaActualiz());
        prodTextPrecUnit.setText(productoActual.getPrecioUnitario());
    }

    private void createUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Producto");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nombre = String.valueOf(prodTextNombre.getText()).trim();
            String stock = String.valueOf(prodTextStock.getText()).trim();
            String precioUni = String.valueOf(prodTextPrecUnit.getText()).trim();

            String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

            dbHelper.updtProducto(productoActual.getId(), nombre, stock, fechaActual, precioUni);
            finish(); // Con finish se cierra el intent
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});
        builder.create().show();
    }

    private void createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Eliminar Producto");
        builder.setMessage("Ten en cuenta que esta acción no se puede deshacer.");
        builder.setPositiveButton("Eliminar", (dialogInterface, i) -> {
            dbHelper.delProducto(productoActual.getId());
            finish(); // Con finish se cierra el intent
        });
        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});

        builder.create().show();
    }
}