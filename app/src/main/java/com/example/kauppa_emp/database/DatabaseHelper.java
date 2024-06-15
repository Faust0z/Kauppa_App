package com.example.kauppa_emp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
            + "a_pedido BOOLEAN NOT NULL, "
            + "precio TEXT)";
    private static final String SQL_TO_CREATE_TABLE_PEDIDOS = "CREATE TABLE " + TABLE_PEDIDOS + " ("
            + "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "nombre_cliente TEXT NOT NULL, "
            + "monto TEXT NOT NULL, "
            + "total_pagado REAL NOT NULL, "
            + "pagado BOOLEAN NOT NULL, "
            + "id_estado INTEGER NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_MOVIMIENTOS = "CREATE TABLE " + TABLE_MOVIMIENTOS + " ("
            + "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "detalle TEXT, "
            + "monto TEXT NOT NULL, "
            + "id_pedido_afectado INTEGER, "
            + "id_tipo INTEGER NOT NULL, "
            + "nombre_cliente TEXT) ";
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
    private static final String SQL_INSERT_PRODUCTOS = "INSERT INTO " + TABLE_PRODUCTOS + " (id_producto, nombre, stock, stock_minimo, activo, a_pedido, precio) VALUES " +
            "(1, 'Producto A', 100, 10, 1, 0, '100'), " +
            "(2, 'Producto B', 50, 5, 1, 1, '150'), " +
            "(3, 'Producto C', 200, 20, 1, 0, '1000'), " +
            "(4, 'Producto D', 0, 5, 0, 1, '3000'), " +
            "(5, 'Producto E', 150, 15, 1, 0, '777');";


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
        db.execSQL(SQL_INSERT_PRODUCTOS);
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

    public long addMovimiento(String fecha, @Nullable String detalle, String monto, @Nullable Integer id_pedido_afectado, int id_tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("detalle", detalle);
        cv.put("monto", monto);
        cv.put("id_pedido_afectado", id_pedido_afectado);
        cv.put("id_tipo", id_tipo);
        return db.insert(TABLE_MOVIMIENTOS, null, cv);
    }

    public long updtMovimiento(String movId, String movFecha, String movMonto, @Nullable String movNomCliente, @Nullable String movDetalle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", movFecha);
        cv.put("monto", movMonto);
        cv.put("nombre_cliente", movNomCliente);
        cv.put("detalle", movDetalle);
        return db.update(TABLE_MOVIMIENTOS, cv, "id_movimiento=?", new String[]{movId});
    }

    public long delMovimiento(String movId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MOVIMIENTOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }

    public Cursor getMovimientosByFecha(String movFecha) {
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE fecha=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
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

    public Cursor getVentasByFecha(String movFecha) {
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE fecha=? AND id_tipo = 2";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getAllVentas(){
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE id_tipo = 2";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getComprasByFecha(String movFecha) {
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE fecha=? AND id_tipo = 6";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getAllCompras(){
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE id_tipo = 6";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getProductoById(int prodId){
        String query = "SELECT * FROM " + TABLE_PRODUCTOS + " WHERE id_movimiento=? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodId)});
        }
        return cursor;
    }

    public Cursor getProductoByNombre(String prodNombre){
        String query = "SELECT * FROM " + TABLE_PRODUCTOS + " WHERE nombre=? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodNombre)});
        }
        return cursor;
    }

    public Cursor getAllProductos(){
        String query = "SELECT * FROM " + TABLE_PRODUCTOS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
