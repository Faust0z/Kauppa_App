package com.example.kauppa_emp.database.dataObjects;

import android.database.Cursor;

import com.example.kauppa_emp.database.DatabaseHelper;

import java.util.ArrayList;

public class Movimientos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;
    private String datoExtra = "";

    public Movimientos(String id, String fecha, String monto, String detalle, String idTipo) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
    }

    public Movimientos(String id, String fecha, String monto, String detalle, String idTipo, String datoExtra) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
        this.datoExtra = datoExtra;
    }

    public static ArrayList<Movimientos> bddToArraylist(DatabaseHelper dbHelper, Cursor cursor){
        ArrayList<Movimientos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String tipo = cursor.getString(4);

                Movimientos movimiento = new Movimientos(id, fecha, monto, detalle, tipo);
                items.add(movimiento);
            }
        }

        ArrayList<Movimientos> itemsIngr = Ingresos.bddToArraylistMovimientos(dbHelper.getAllVentas());
        items.addAll(itemsIngr);

        ArrayList<Movimientos> itemsEgre = Egresos.bddToArraylistMovimientos(dbHelper.getAllCompras());
        items.addAll(itemsEgre);
        return items;
    }

    public static ArrayList<Movimientos> bddToArraylistByFecha(DatabaseHelper dbHelper, String fechaActual){
        ArrayList<Movimientos> items = new ArrayList<>();
        Cursor cursor = dbHelper.getMovimientosByFecha(fechaActual);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String tipo = cursor.getString(4);

                Movimientos movimiento = new Movimientos(id, fecha, monto, detalle, tipo);
                items.add(movimiento);
            }
        }

        ArrayList<Movimientos> itemsIngr = Ingresos.bddToArraylistMovimientos(dbHelper.getVentasByFecha(fechaActual));
        items.addAll(itemsIngr);

        ArrayList<Movimientos> itemsEgre = Egresos.bddToArraylistMovimientos(dbHelper.getComprasByFecha(fechaActual));
        items.addAll(itemsEgre);

        return items;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMonto() {
        return monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public String getDatoExtra() {
        return datoExtra;
    }

}
