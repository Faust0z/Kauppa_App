package com.example.kauppa_emp.database.dataObjects;

public class TiposMovimiento {
    public static final String VENTA = "1";
    public static final String SENIA = "2";
    public static final String VARIOSING = "3";
    public static final String COMPRA = "4";
    public static final String PAGO = "5";
    public static final String VARIOSEGR = "6";
    public static final String PEDIDO = "7";

    public static String getIngreOrEgreById(String id){
        switch (id) {
            case "1":
            case "2":
            case "3":
                return "Ingreso";
            case "4":
            case "5":
            case "6":
                return "Egreso";
            case "7":
                return "Pedido";
            default:
                return "Mov indefinido";
        }
    }
}
