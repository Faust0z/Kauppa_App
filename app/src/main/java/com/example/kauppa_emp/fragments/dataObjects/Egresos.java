package com.example.kauppa_emp.fragments.dataObjects;

public class Egresos {
    private String id;
    private String fecha;
    private String monto;
    private String detalle;
    private String idTipo;
    private String nomCliente;

    public Egresos(String id, String fecha, String monto, String detalle, String idTipo, String nomCliente) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.detalle = detalle;
        this.idTipo = idTipo;
        this.nomCliente = nomCliente;
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
