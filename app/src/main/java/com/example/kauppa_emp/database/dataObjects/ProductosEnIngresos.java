package com.example.kauppa_emp.database.dataObjects;

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