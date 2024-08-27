package com.example.kauppa_emp.database.dataObjects;

import android.database.Cursor;

import com.example.kauppa_emp.database.DatabaseHelper;

import java.util.ArrayList;

public class Productos {
    private String id;
    private String nombre;
    private String stock;
    private String fechaActualiz;
    private String precioUnitario;
    private boolean esFantasma;

    private int cantidad; //Valor sólo usado para los prods asociados a ingresos o pedidos
    private int stockEditable; //Valor sólo usado para controlar el stock antes de editarlo

    public Productos(String id, String nombre, String stock, String fechaActualiz, String precioUnitario, boolean esFantasma) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.fechaActualiz = fechaActualiz;
        this.precioUnitario = precioUnitario;
        this.esFantasma = esFantasma;
    }

    public static ArrayList<Productos> bddToArraylist(Cursor cursor){
        ArrayList<Productos> items = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String stock = cursor.getString(2);
                String fechaActualiz = cursor.getString(3);
                boolean esFantasma = cursor.getInt(4) == 1;
                String precioUnitario = cursor.getString(5);

                Productos prod = new Productos(id, nombre, stock, fechaActualiz, precioUnitario, esFantasma);
                prod.setCant(Integer.parseInt(cursor.getString(2)));
                items.add(prod);
            }
        }
        return items;
    }

    public static ArrayList<Productos> getProdsPorIngr(DatabaseHelper dbHelper, String ingrId){
        ArrayList<Productos> items = new ArrayList<>();
        Cursor cursorProdPorIngr = dbHelper.getProductosIngrById(ingrId);
        if (cursorProdPorIngr.getCount() != 0) {
            while (cursorProdPorIngr.moveToNext()) {
                Cursor cursorProd = dbHelper.getProductoById(cursorProdPorIngr.getString(1));

                if (cursorProd.moveToNext()) {
                    String id = cursorProd.getString(0);
                    String nombre = cursorProd.getString(1);
                    String stock = cursorProd.getString(2);
                    String fechaActualiz = cursorProd.getString(3);
                    boolean esFantasma = cursorProd.getInt(4) == 1;
                    String precioUnitario = cursorProd.getString(5);

                    Productos prod = new Productos(id, nombre, stock, fechaActualiz, precioUnitario, esFantasma);
                    prod.setCant(Integer.parseInt(cursorProdPorIngr.getString(2)));
                    items.add(prod);
                }
            }
        }
        return items;
    }

    public static Productos getUltimoProducto(Cursor cursor){
        Productos prod = null;
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String stock = cursor.getString(2);
                String fechaActualiz = cursor.getString(3);
                boolean esFantasma = cursor.getInt(4) == 1;
                String precioUnitario = cursor.getString(5);

                prod = new Productos(id, nombre, stock, fechaActualiz, precioUnitario, esFantasma);
            }
        }
        return prod;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setStock(String stock) { this.stock = stock; }

    public String getStock() { return stock; }

    public String getFechaActualiz() {
        return fechaActualiz;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCant(){ return cantidad; }

    public void setCant(int cant){ this.cantidad = cant; }

    public void setEsFantasma(boolean esFantasma) { this.esFantasma = esFantasma; }

    public boolean esFantasma() { return esFantasma; }

    public int getStockEditable() {
        return stockEditable;
    }

    public void setStockEditable(int stockEditable) {
        this.stockEditable = stockEditable;
    }
}
