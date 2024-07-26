package com.example.kauppa_emp.database.dataObjects;

import android.os.Parcel;
import android.os.Parcelable;

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
