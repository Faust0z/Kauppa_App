package com.example.kauppa_emp.database.dataObjects;

import android.database.Cursor;

import java.util.ArrayList;

public class Productos {
    private String id;
    private String nombre;
    private String stock;
    private String fechaActualiz;
    private String precioUnitario;

    public Productos(String id, String nombre, String stock, String fechaActualiz, String precioUnitario) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.fechaActualiz = fechaActualiz;
        this.precioUnitario = precioUnitario;
    }

    public static ArrayList<Productos> bddToArraylist(Cursor cursor){
        ArrayList<Productos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String stock = cursor.getString(2);
                String fechaActualiz = cursor.getString(3);
                String precioUnitario = cursor.getString(4);

                Productos ingreso = new Productos(id, nombre, stock, fechaActualiz, precioUnitario);
                items.add(ingreso);
            }
        }
        return items;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getStock() {
        return stock;
    }

    public String getFechaActualiz() {
        return fechaActualiz;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

}
