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

        spinner_Estado_PedidoInfo_box.setVisibility(View.GONE);
    }

    @Override
    protected void getIntentData(){
        pedidoActual = new Pedidos("0", fechaActual, "", "", "0", "0", "0", "", "", "1");
    }

    @Override
    protected void setData(){
        super.setData();
        movTitulo.setText("Agregar Pedido");
    }

    private void insertBDD() {
        String fechaEntrega = String.valueOf(movTextoFechaEntrega.getText());
        String detalle = String.valueOf(movTextoDetalle.getText());
        String senia = String.valueOf(movTextoSenia.getText()).trim();
        String total = String.valueOf(movTextoTotal.getText());
        String nomCliente = String.valueOf(movTextoNomCliente.getText());
        String celCliente = String.valueOf(movTextoCelCliente.getText());

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