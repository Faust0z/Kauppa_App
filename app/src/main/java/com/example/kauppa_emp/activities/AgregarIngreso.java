package com.example.kauppa_emp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import com.example.kauppa_emp.database.dataObjects.Ingresos;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgregarIngreso extends MasInfoIngresos {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actualizarButton.setText("Agregar");
        actualizarButton.setOnClickListener(v -> insertBDD());

        anularButton.setText("Cancelar");
        anularButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void getIntentData(){
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        ingresoActual = new Ingresos("0", fechaActual, "0", "", "1", "");
    }

    @Override
    protected void setData(){
        super.setData();
        movTitulo.setText("Agregar Ingreso");
    }

    private void insertBDD() {
        String fecha = String.valueOf(movTextoFecha.getText());
        String monto = String.valueOf(movTextoTotal.getText());
        String detalle = String.valueOf(movTextoDetalle.getText());
        int idTipo = TiposMovimiento.nombreToId(spinnerTipo.getEditableText().toString());
        String nomCliente = String.valueOf(movTextoNomCliente.getText());

        dbHelper.addIngreso(fecha, monto, detalle, idTipo, nomCliente);

        Ingresos ultimoIngreso = Ingresos.getUltimoIngreso(dbHelper.getUltimoIngreso());
        gestionarProdsAsociados(fecha, ultimoIngreso.getId());

        finish();
    }
}