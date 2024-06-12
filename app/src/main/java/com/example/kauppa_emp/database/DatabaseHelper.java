package com.example.kauppa_emp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VER = 1;
    private Context context;

    private static final String TABLE_PRODUCTOS = "PRODUCTOS";
    private static final String TABLE_PEDIDOS = "PEDIDOS";
    private static final String TABLE_MOVIMIENTOS = "MOVIMIENTOS";
    private static final String TABLE_TIPOS_MOVIMIENTO = "TIPOS_MOVIMIENTO";
    private static final String TABLE_ESTADOS_PEDIDO = "ESTADOS_PEDIDO";
    private static final String TABLE_PRODUCTOS_POR_VENTA = "PRODUCTOS_POR_VENTA";
    private static final String TABLE_PRODUCTOS_POR_PEDIDO = "PRODUCTOS_POR_PEDIDO";

    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS = "CREATE TABLE " + TABLE_PRODUCTOS + " ("
            + "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nombre TEXT NOT NULL, "
            + "stock INTEGER, "
            + "stock_minimo INTEGER, "
            + "activo BOOLEAN NOT NULL, "
            + "a_pedido BOOLEAN NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_PEDIDOS = "CREATE TABLE " + TABLE_PEDIDOS + " ("
            + "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "nombre_cliente TEXT NOT NULL, "
            + "monto REAL NOT NULL, "
            + "total_pagado REAL NOT NULL, "
            + "pagado BOOLEAN NOT NULL, "
            + "id_estado INTEGER NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_MOVIMIENTOS = "CREATE TABLE " + TABLE_MOVIMIENTOS + " ("
            + "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "detalle TEXT, "
            + "monto REAL NOT NULL, "
            + "id_pedido_afectado INTEGER, "
            + "id_tipo INTEGER NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_TIPOS_MOVIMIENTO = "CREATE TABLE " + TABLE_TIPOS_MOVIMIENTO + " ("
            + "id_tipo INTEGER PRIMARY KEY, "
            + "descripcion TEXT NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_ESTADOS_PEDIDO = "CREATE TABLE " + TABLE_ESTADOS_PEDIDO + " ("
            + "id_estado INTEGER PRIMARY KEY, "
            + "descripcion TEXT NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS_POR_VENTA = "CREATE TABLE " + TABLE_PRODUCTOS_POR_VENTA + " ("
            + "id_venta INTEGER, "
            + "id_producto INTEGER, "
            + "cantidad INTEGER NOT NULL, "
            + "subtotal REAL NOT NULL, "
            + "PRIMARY KEY (id_venta, id_producto))";
    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS_POR_PEDIDO = "CREATE TABLE " + TABLE_PRODUCTOS_POR_PEDIDO + " ("
            + "id_pedido INTEGER, "
            + "id_producto INTEGER, "
            + "cantidad INTEGER NOT NULL, "
            + "subtotal REAL NOT NULL, "
            + "PRIMARY KEY (id_pedido, id_producto))";
    private static final String SQL_INSERT_TIPOS_MOVIMIENTO = "INSERT INTO TIPOS_MOVIMIENTO (id_tipo, descripcion) VALUES " +
            "(1, 'VENTA SIMPLE'), " +
            "(2, 'VENTA DETALLADA'), " +
            "(3, 'PAGO'), " +
            "(4, 'COBRO'), " +
            "(5, 'VARIOS'), " +
            "(6, 'COMPRA');";
    private static final String SQL_INSERT_ESTADOS_PEDIDO = "INSERT INTO ESTADOS_PEDIDO (id_estado, descripcion) VALUES " +
            "(1, 'PENDIENTE'), " +
            "(2, 'PREPARADO'), " +
            "(3, 'A ENTREGAR'), " +
            "(4, 'ENTREGADO');";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override //Método de creación de la BDD
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS);
        db.execSQL(SQL_TO_CREATE_TABLE_PEDIDOS);
        db.execSQL(SQL_TO_CREATE_TABLE_MOVIMIENTOS);
        db.execSQL(SQL_TO_CREATE_TABLE_TIPOS_MOVIMIENTO);
        db.execSQL(SQL_TO_CREATE_TABLE_ESTADOS_PEDIDO);
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS_POR_VENTA);
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS_POR_PEDIDO);

        db.execSQL(SQL_INSERT_TIPOS_MOVIMIENTO);
        db.execSQL(SQL_INSERT_ESTADOS_PEDIDO);
    }

    @Override // Método obligatorio de la clase. Se espera no usarlo.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMIENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_MOVIMIENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTADOS_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_POR_VENTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_POR_PEDIDO);
        onCreate(db);
    }

    public long addMovimiento(String fecha, @Nullable String detalle, double monto, @Nullable Integer id_pedido_afectado, int id_tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("detalle", detalle);
        cv.put("monto", monto);
        cv.put("id_pedido_afectado", id_pedido_afectado);
        cv.put("id_tipo", id_tipo);
        return db.insert(TABLE_MOVIMIENTOS, null, cv);
    }

    public long addVenta(int id_movimiento, String fecha, double monto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_movimiento", id_movimiento);
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        return db.insert("VENTAS", null, cv); // Assuming table VENTAS exists
    }

    public long addCompra(int id_movimiento, String fecha, double monto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_movimiento", id_movimiento);
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        return db.insert("COMPRAS", null, cv); // Assuming table COMPRAS exists
    }


    public Cursor getAllMovimientos(){
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
