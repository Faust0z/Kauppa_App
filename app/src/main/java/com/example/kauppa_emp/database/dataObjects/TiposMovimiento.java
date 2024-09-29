package com.example.kauppa_emp.database.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class TiposMovimiento {
    public static final String VENTA = "1";
    public static final String SENIA = "2";
    public static final String VARIOSING = "3";
    public static final String COMPRA = "4";
    public static final String PAGO = "5";
    public static final String VARIOSEGR = "6";
    public static final String PEDIDO = "7";

    public static String getTipoMov(String id){
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
                return "Indefinido";
        }
    }

    public static String getTipoById(String id){
        switch (id) {
            case "1":
                return "Venta";
            case "2":
                return "Seña";
            case "3":
            case "6":
                return "Varios";
            case "4":
                return "Compra";
            case "5":
                return "Pago";
            default:
                return "";
        }
    }

    public static String getEstadoById(String id){
        switch (id) {
            case "1":
                return "Sin Empezar";
            case "2":
                return "En Curso";
            case "3":
                return "Listo";
            case "4":
                return "Entregado";
            default:
                return "";
        }
    }

    public static List<String> relleSpinnerIngresos(){
        List<String> elementos = new ArrayList<>();
        elementos.add(getTipoById("1"));
        elementos.add(getTipoById("2"));
        elementos.add(getTipoById("3"));
        return elementos;
    }

    public static List<String> relleSpinnerEgresos(){
        List<String> elementos = new ArrayList<>();
        elementos.add(getTipoById("4"));
        elementos.add(getTipoById("5"));
        elementos.add(getTipoById("6"));
        return elementos;
    }

    public static List<String> relleSpinnerPedidos(){
        List<String> elementos = new ArrayList<>();
        elementos.add(getEstadoById("1"));
        elementos.add(getEstadoById("2"));
        elementos.add(getEstadoById("3"));
        elementos.add(getEstadoById("4"));
        return elementos;
    }

    public static int nombreToId(String nombre){
        switch (nombre) {
            case "Venta":
                return 1;
            case "Seña":
                return 2;
            case "Varios Ing":
                return 3;
            case "Compra":
                return 4;
            case "Pago":
                return 5;
            case "Varios":
                return 6;
            case "Pedido":
                return 7;
            default:
                return 0;
        }
    }

    public static int estadoToId(String nombre){
        switch (nombre) {
            case "Sin Empezar":
                return 1;
            case "En Curso":
                return 2;
            case "Listo":
                return 3;
            case "Entregado":
                return 4;
            default:
                return 0;
        }
    }
}
