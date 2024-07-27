package com.example.kauppa_emp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.kauppa_emp.database.dataObjects.ProductosEnIngresos;

import java.util.ArrayList;

public class DatabaseHelper {
    private DatabaseInit dbInit;

    public DatabaseHelper(@Nullable Context context) {
        dbInit = new DatabaseInit(context);
    }

    public long addMovimiento(String fecha, String monto, @Nullable String detalle, int idTipo) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        return db.insert(DatabaseInit.TABLE_MOVIMIENTOS, null, cv);
    }

    public long addIngreso(String fecha, String monto, @Nullable String detalle, int idTipo, ArrayList<ProductosEnIngresos> prodsEnIngresos) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        db.insert(DatabaseInit.TABLE_MOVIMIENTOS, null, cv);

        Cursor cursor = getLastIngreso();
        String idVenta = "";
        if (cursor.getCount() > 0){idVenta = cursor.getString(0);}

        long result = 0;
        if (!idVenta.isEmpty() && !prodsEnIngresos.isEmpty()){
            for (int i = 0; i < prodsEnIngresos.size(); i++) {
                result = addProductoIngreso(idVenta, prodsEnIngresos.get(i).getIdProducto(), prodsEnIngresos.get(i).getIdProducto());
            }
        }
        return result;
    }

    public long addProductoIngreso(String idVenta, String idProducto, String cantidad) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_venta", Integer.parseInt(idVenta));
        cv.put("id_producto", Integer.parseInt(idProducto));
        cv.put("cantidad", Integer.parseInt(cantidad));

        Cursor cursor = getProductoById(Integer.parseInt(idProducto));
        String valorProducto = "";
        if (cursor.getCount() > 0){
            valorProducto = cursor.getString(6);
        }
        String subtotal = String.valueOf((Double.parseDouble(valorProducto) * Integer.parseInt(cantidad)));
        cv.put("subtotal", subtotal);

        return db.insert(DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO, null, cv);
    }

    // Todo: a implementar
    public long addEgreso(String fecha, String monto, @Nullable String detalle, int idTipo) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        return db.insert(DatabaseInit.TABLE_MOVIMIENTOS, null, cv);
    }

    public long updtMovimiento(String id, String fecha, String monto, @Nullable String detalle) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        return db.update(DatabaseInit.TABLE_MOVIMIENTOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long updtIngreso(String id, String fecha, String monto, @Nullable String detalle, @Nullable String nomCliente) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("nombre_cliente", nomCliente);
        return db.update(DatabaseInit.TABLE_MOVIMIENTOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long updtEgreso(String id, String fecha, String monto, @Nullable String detalle, @Nullable String nomCliente) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("nombre_cliente", nomCliente);
        return db.update(DatabaseInit.TABLE_MOVIMIENTOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long delMovimiento(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_MOVIMIENTOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }

    public long delIngreso(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_INGRESOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }

    public long delEgreso(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_EGRESOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }

    private Cursor getLastIngreso() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS + " ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getMovimientosByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_MOVIMIENTOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getIngresosByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getEgresosByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_EGRESOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getAllMovimientos(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_MOVIMIENTOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getVentasByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getAllVentas(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getComprasByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_EGRESOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getAllCompras(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_EGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getProductoById(int prodId){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " WHERE id_movimiento=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodId)});
        }
        return cursor;
    }

    public Cursor getProductoByNombre(String prodNombre){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " WHERE nombre=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodNombre)});
        }
        return cursor;
    }

    public Cursor getAllProductos(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
