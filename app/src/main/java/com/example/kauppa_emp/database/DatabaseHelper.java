package com.example.kauppa_emp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VER = 1;

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
            + "detalle TEXT NOT NULL, "
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


    public DatabaseHelper(Context context) {
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
}
