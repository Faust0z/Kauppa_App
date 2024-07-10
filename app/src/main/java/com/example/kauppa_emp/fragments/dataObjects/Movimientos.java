package com.example.kauppa_emp.fragments.dataObjects;

public class Movimientos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;

    // Constructor
    public Movimientos(String id, String fecha, String monto, String detalle, String idTipo) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
    }

    // Getters
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
}

