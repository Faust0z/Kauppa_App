package com.example.kauppa_emp.fragments.dataObjects;

import android.database.Cursor;

import java.util.ArrayList;

public class Ingresos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;
    private String nomCliente;

    public Ingresos(String id, String fecha, String monto, String detalle, String idTipo, String nomCliente) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
        this.nomCliente = nomCliente;
    }

    public static ArrayList<Ingresos> bddToArraylist(Cursor cursor){
        ArrayList<Ingresos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomCliente = cursor.getString(5);

                Ingresos ingreso = new Ingresos(id, fecha, monto, detalle, idTipo, nomCliente);
                items.add(ingreso);
            }
        }
        return items;
    }

    public static ArrayList<Movimientos> bddToArraylistMovimientos(Cursor cursor){
        ArrayList<Movimientos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomCliente = cursor.getString(5);

                Movimientos movimiento = new Movimientos(id, fecha, monto, detalle, idTipo, nomCliente);
                items.add(movimiento);
            }
        }
        return items;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getMonto() {
        return monto;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public String getNomCliente() {
        return nomCliente;
    }
}
