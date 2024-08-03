package com.example.kauppa_emp.database.dataObjects;

import android.database.Cursor;
import java.util.ArrayList;

public class Egresos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;
    private String nomProv;

    public Egresos(String id, String fecha, String monto, String detalle, String idTipo, String nomProv) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
        this.nomProv = nomProv;
    }

    public static ArrayList<Egresos> bddToArraylist(Cursor cursor){
        ArrayList<Egresos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomProv = cursor.getString(5);

                Egresos egreso = new Egresos(id, fecha, monto, detalle, idTipo, nomProv);
                items.add(egreso);
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
                String nomProv = cursor.getString(5);

                Movimientos movimiento = new Movimientos(id, fecha, monto, detalle, idTipo, nomProv);
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

    public String getNomProv() {
        return nomProv;
    }
}
