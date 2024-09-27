package com.example.kauppa_emp.database.dataObjects;

import android.database.Cursor;

import java.util.ArrayList;

public class Pedidos {
    private String id;
    private String fecha;
    private String fechaEntrega;
    private String detalle;
    private String senia;
    private String resto;
    private String total;
    private String nomCliente;
    private String celCliente;
    private String idEstado;

    public Pedidos(String id, String fecha, String fechaEntrega, String detalle, String senia, String resto, String total, String nomCliente, String celCliente, String idEstado) {
        this.id = id;
        this.fecha = fecha;
        this.fechaEntrega = fechaEntrega;
        this.detalle = detalle;
        this.senia = senia;
        this.resto = resto;
        this.total = resto;
        this.nomCliente = nomCliente;
        this.celCliente = celCliente;
        this.idEstado = idEstado;
    }

    public static ArrayList<Pedidos> bddToArraylist(Cursor cursor){
        ArrayList<Pedidos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String fechaEntrega = cursor.getString(2);
                String detalle = cursor.getString(3);
                String senia = cursor.getString(4);
                String resto = cursor.getString(5);
                String total = cursor.getString(6);
                String nomCliente = cursor.getString(7);
                String celCliente = cursor.getString(8);
                String idEstado = cursor.getString(9);

                Pedidos pedido = new Pedidos(id, fecha, fechaEntrega, detalle, senia, resto, total, nomCliente, celCliente, idEstado);
                items.add(pedido);
            }
        }
        return items;
    }

    public static ArrayList<Movimientos> bddToArraylistMovimientos(Cursor cursor){
        ArrayList<Movimientos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fechaEntrega = cursor.getString(2);
                String detalle = cursor.getString(3);
                String total = cursor.getString(6);
                String nomCliente = cursor.getString(7);
                String idEstado = cursor.getString(9);

                if (idEstado.equals("4")){ //Si está entregado
                    Movimientos movimiento = new Movimientos(id, fechaEntrega, total, detalle, idEstado, nomCliente);
                    movimiento.setEsPedido(true);
                    items.add(movimiento);
                }
            }
        }
        return items;
    }

    public static Pedidos getUltimoPedido(Cursor cursor){
        Pedidos pedido = null;
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String fechaEntrega = cursor.getString(2);
                String detalle = cursor.getString(3);
                String senia = cursor.getString(4);
                String resto = cursor.getString(5);
                String total = cursor.getString(6);
                String nomCliente = cursor.getString(7);
                String celCliente = cursor.getString(8);
                String idEstado = cursor.getString(9);

                pedido = new Pedidos(id, fecha, fechaEntrega, detalle, senia, resto, total, nomCliente, celCliente, idEstado);
            }
        }
        return pedido;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getSenia() {
        return senia;
    }

    public String getResto() {
        return resto;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public String getCelCliente() {
        return celCliente;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
