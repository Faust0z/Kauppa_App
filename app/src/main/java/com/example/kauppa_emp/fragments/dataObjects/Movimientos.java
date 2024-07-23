package com.example.kauppa_emp.fragments.dataObjects;

import android.database.Cursor;
import java.util.ArrayList;

public class Movimientos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;

    public Movimientos(String id, String fecha, String monto, String detalle, String idTipo) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
    }

    public static ArrayList<Movimientos> bddToArraylist(Cursor cursor){
        ArrayList <Movimientos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(3); // Monto debería ser la segunda columna, pero es la 3 por algún motivo
                String detalle = cursor.getString(2);
                String tipo = cursor.getString(4);

                Movimientos movimiento = new Movimientos(id, fecha, monto, detalle, tipo);
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

    public String getMonto() {
        return monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getIdTipo() {
        return idTipo;
    }
}
