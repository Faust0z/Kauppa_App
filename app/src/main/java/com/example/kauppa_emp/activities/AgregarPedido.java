package com.example.kauppa_emp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kauppa_emp.database.dataObjects.Pedidos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgregarPedido extends MasInfoPedidos {
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actualizarButton.setText("Agregar");
        actualizarButton.setOnClickListener(v -> insertBDD());

        anularButton.setText("Cancelar");
        anularButton.setOnClickListener(v -> finish());

        spinnerTipo.setVisibility(View.GONE);
    }

    @Override
    protected void getIntentData(){
        pedidoActual = new Pedidos("0", fechaActual, "", "", "0", "0", "0", "", "", "1");
    }

    @Override
    protected void setData(){
        super.setData();
        movTitulo.setText("Ingrese los datos del nuevo Pedido");
    }

    private void insertBDD() {
        String fechaEntrega = movTextoFechaEntrega.getText().toString();
        String detalle = movTextoDetalle.getText().toString();
        String senia = movTextoSenia.getText().toString().trim();
        String total = movTextoTotal.getText().toString();
        String nomCliente = movTextoNomCliente.getText().toString();
        String celCliente = movTextoCelCliente.getText().toString();

        if (dbHelper.addPedido(fechaActual, fechaEntrega, detalle, senia, total, nomCliente, celCliente, 1) != -1){
            Toast.makeText(this, "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
        }

        Pedidos ultimoPedido = Pedidos.getUltimoPedido(dbHelper.getUltimoPedido());
        gestionarProdsAsociados(fechaActual, ultimoPedido.getId());

        if (!senia.isEmpty() && !senia.equals("0")){
            dbHelper.addIngreso(fechaActual, senia, "Seña del pedido N°: " + ultimoPedido.getId(), 2, nomCliente);
        }

        finish();
    }
}