package com.example.kauppa_emp.database.dataObjects;

public class TiposMovimiento {
    public static final String VENTA_SIMPLE = "1";
    public static final String VENTA_DETALLADA = "2";
    public static final String COMPRA = "3";
    public static final String PAGO = "4";
    public static final String COBRO = "5";
    public static final String VARIOS = "6";

    public static String getTipoById(String id){
        switch (id){
            case "1":
                return "Venta Simple";
            case "2":
                return "Venta Detallada";
            case "3":
                return "Compra";
            case "4":
                return "Pago";
            case "5":
                return "Cobro";
            case "6":
                return "Varios";
            default:
                return "Tipo no definido";
        }
    }

    public static String getIngreOrEgreById(String id){
        if (id.equals("1") || id.equals("2")){
            return "Ingreso";
        } else {
            return "Egreso";
        }
    }
}
