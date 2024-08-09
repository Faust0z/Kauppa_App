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

    // ------------------ TABLA MOVIMIENTOS ------------------

    public long addMovimiento(String fecha, String monto, @Nullable String detalle, int idTipo) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        return db.insert(DatabaseInit.TABLE_MOVIMIENTOS, null, cv);
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

    public Cursor getMovimientosByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_MOVIMIENTOS + " WHERE fecha=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public long updtMovimiento(String id, String fecha, String monto, @Nullable String detalle) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        return db.update(DatabaseInit.TABLE_MOVIMIENTOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long delMovimiento(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_MOVIMIENTOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }


    // ------------------ TABLA INGRESO ------------------


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

    public Cursor getAllIngresos(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
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

    public long updtIngreso(String id, String fecha, String monto, @Nullable String detalle, @Nullable String nomCliente) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("nombre_cliente", nomCliente);
        return db.update(DatabaseInit.TABLE_INGRESOS, cv, "id_movimiento=?", new String[]{id});
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

    public long delIngreso(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_INGRESOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
    }


    // ------------------ TABLA EGRESOS ------------------


    public long addEgreso(String fecha, String monto, @Nullable String detalle, int idTipo, String nomProv) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        cv.put("nom_prov", nomProv);
        return db.insert(DatabaseInit.TABLE_EGRESOS, null, cv);
    }

    public long updtEgreso(String id, String fecha, String monto, @Nullable String detalle, @Nullable String nomProv) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("nom_prov", nomProv);
        return db.update(DatabaseInit.TABLE_EGRESOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long delEgreso(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_EGRESOS, "id_movimiento=?", new String[]{String.valueOf(movId)});
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

    public Cursor getAllEgresos(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_EGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    // ------------------ TABLA TIPOS MOVIMIENTOS ------------------


    public String getTipoMovById(String tipoMovId){
        String query = "SELECT descripcion FROM " + DatabaseInit.TABLE_TIPOS_MOVIMIENTO + " WHERE id_tipo=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        String descripcion = "Tipo no definido";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{tipoMovId});
            if (cursor != null && cursor.moveToFirst()) {
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                cursor.close();
            }
        }
        return descripcion;
    }


    // ------------------ TABLA PRODUCTOS ------------------


    public long addProducto(String nombre, String stock, String fechaAct, String precioUnit) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("stock", stock);
        cv.put("fecha_actualizacion", fechaAct);
        cv.put("precio_unitario", precioUnit);
        return db.insert(DatabaseInit.TABLE_PRODUCTOS, null, cv);
    }

    public long updtProducto(String id, String nombre, String stock, String fechaAct, String precioUnit) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("stock", stock);
        cv.put("fecha_actualizacion", fechaAct);
        cv.put("precio_unitario", precioUnit);
        return db.update(DatabaseInit.TABLE_PRODUCTOS, cv, "id_producto=?", new String[]{id});
    }

    public long delProducto(String movId) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_PRODUCTOS, "id_producto=?", new String[]{movId});
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

    public Cursor getAllProductos(){
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS;
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
}
