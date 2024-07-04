package com.example.kauppa_emp.fragments.dataObjects;

public class ProductosEnIngresos {
    private String idProducto;
    private String cantidad;

    public ProductosEnIngresos(String idProducto, String quantity) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getCantidad() {
        return cantidad;
    }
}