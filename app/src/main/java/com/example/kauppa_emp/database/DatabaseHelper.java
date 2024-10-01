package com.example.kauppa_emp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

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

    public Cursor getAllMovimientos() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_MOVIMIENTOS;
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


    public long addIngreso(String fecha, String monto, @Nullable String detalle, int idTipo, String nombreCliente) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", monto);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        cv.put("nombre_cliente", nombreCliente);
        return db.insert(DatabaseInit.TABLE_INGRESOS, null, cv);
    }

    public Cursor getAllIngresos() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
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

    public Cursor getUltimoIngreso() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_INGRESOS + " ORDER BY id_movimiento DESC LIMIT 1";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public long updtIngreso(String id, String fecha, String total, @Nullable String detalle, int idTipo, @Nullable String nomCliente) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("monto", total);
        cv.put("detalle", detalle);
        cv.put("id_tipo", idTipo);
        cv.put("nombre_cliente", nomCliente);
        return db.update(DatabaseInit.TABLE_INGRESOS, cv, "id_movimiento=?", new String[]{id});
    }

    public long delIngreso(String id) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        delALLProductosByIngrId(id);
        return db.delete(DatabaseInit.TABLE_INGRESOS, "id_movimiento=?", new String[]{id});
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

    public Cursor getAllEgresos() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_EGRESOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    // ------------------ TABLA TIPOS MOVIMIENTOS ------------------


    public String getTipoMovById(String tipoMovId) {
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


    public long addProducto(String nombre, String stock, String fechaAct, String precioUnit, boolean esFantasma) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("stock", stock);
        cv.put("fecha_actualizacion", fechaAct);
        cv.put("precio_unitario", precioUnit);
        cv.put("es_fantasma", esFantasma ? 1 : 0);

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

    public Cursor getAllProductos() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " WHERE es_fantasma=0 ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getProductoById(String prodId) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " WHERE id_producto=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodId)});
        }
        return cursor;
    }

    public Cursor getProductoByNombre(String prodNombre) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " WHERE nombre=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(prodNombre)});
        }
        return cursor;
    }

    public Cursor getUltimoProducto() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS + " ORDER BY id_producto DESC LIMIT 1 ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    // ------------------ TABLA PRODUCTOS_POR_INGRESO ------------------


    public long addProductosIngr(String idIngreso, String idProducto, int cantidad) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_ingreso", Integer.parseInt(idIngreso));
        cv.put("id_producto", Integer.parseInt(idProducto));
        cv.put("cantidad", cantidad);
        return db.insert(DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO, null, cv);
    }

    public long updtProductosIngr(String idIngreso, String idProducto, int cantidad) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_ingreso", Integer.parseInt(idIngreso));
        cv.put("id_producto", Integer.parseInt(idProducto));
        cv.put("cantidad", cantidad);

            return db.update(DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO, cv,
                    "id_ingreso=? AND id_producto=?", new String[]{idIngreso, idProducto});
    }

    public long delProductosIngr(String idIngreso, String idProducto) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO,
                "id_ingreso=? AND id_producto=?",
                new String[]{idIngreso, idProducto});
    }

    public long delALLProductosByIngrId(String idIngreso) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO, "id_ingreso=?", new String[]{idIngreso});
    }

    public Cursor getProductosIngrById(String ingreId) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO + " WHERE id_ingreso=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(ingreId)});
        }
        return cursor;
    }


    // ------------------ TABLA PEDIDOS ------------------


    public long addPedido(String fecha, String fechaEntrega, @Nullable String detalle, @Nullable String senia, String total, @Nullable String nombreCliente, @Nullable String celularCliente, int idEstado) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha", fecha);
        cv.put("fecha_entrega", fechaEntrega);
        cv.put("detalle", detalle);
        cv.put("senia", senia);
        cv.put("total", total);
        cv.put("nombre_cliente", nombreCliente);
        cv.put("celular_cliente", celularCliente);
        cv.put("id_estado", idEstado);
        return db.insert(DatabaseInit.TABLE_PEDIDOS, null, cv);
    }

    public Cursor getAllPedidos() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PEDIDOS;
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getPedidosByFecha(String movFecha) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PEDIDOS + " WHERE fecha_entrega=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{movFecha});
        }
        return cursor;
    }

    public Cursor getUltimoPedido() {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PEDIDOS + " ORDER BY id_pedido DESC LIMIT 1";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public long updtPedido(String id, String fechaEntrega, @Nullable String detalle, String senia, String total, String resto, @Nullable String nomCliente, @Nullable String celCliente, int idEstado) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fecha_entrega", fechaEntrega);
        cv.put("detalle", detalle);
        cv.put("senia", senia);
        cv.put("resto", resto);
        cv.put("total", total);
        cv.put("nombre_cliente", nomCliente);
        cv.put("celular_cliente", celCliente);
        cv.put("id_estado", idEstado);
        return db.update(DatabaseInit.TABLE_PEDIDOS, cv, "id_pedido=?", new String[]{id});
    }

    public long delPedido(String id) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        delALLProductosByPdidId(id);
        return db.delete(DatabaseInit.TABLE_PEDIDOS, "id_pedido=?", new String[]{id});
    }


    // ------------------ TABLA PRODUCTOS_POR_INGRESO ------------------


    public long addProductosPdid(String idPedido, String idProducto, int cantidad) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_pedido", Integer.parseInt(idPedido));
        cv.put("id_producto", Integer.parseInt(idProducto));
        cv.put("cantidad", cantidad);
        return db.insert(DatabaseInit.TABLE_PRODUCTOS_POR_PEDIDO, null, cv);
    }

    public long updtProductosPdid(String idPedido, String idProducto, int cantidad) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_pedido", Integer.parseInt(idPedido));
        cv.put("id_producto", Integer.parseInt(idProducto));
        cv.put("cantidad", cantidad);

        return db.update(DatabaseInit.TABLE_PRODUCTOS_POR_PEDIDO, cv,
                "id_pedido=? AND id_producto=?", new String[]{idPedido, idProducto});
    }

    public long delProductosPdid(String idPedido, String idProducto) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_PRODUCTOS_POR_PEDIDO,
                "id_pedido=? AND id_producto=?",
                new String[]{idPedido, idProducto});
    }

    public long delALLProductosByPdidId(String idPedido) {
        SQLiteDatabase db = dbInit.getWritableDatabase();
        return db.delete(DatabaseInit.TABLE_PRODUCTOS_POR_PEDIDO, "id_pedido=?", new String[]{idPedido});
    }

    public Cursor getProductosPdidById(String pedidId) {
        String query = "SELECT * FROM " + DatabaseInit.TABLE_PRODUCTOS_POR_PEDIDO + " WHERE id_pedido=? ";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(pedidId)});
        }
        return cursor;
    }


    // ------------------ QUERIES ESPECÍFICAS PARA REPORTES ------------------

    /**
     * Retorna un cursor con todos los ingresos de un tipo específico y los productos
     * involucrados en cada uno de ellos.
     * @param mes formato MM/YYYY
     * @param tipoId ID de tipo
     * @return cursor
     */
    public Cursor getIngresosByMesAndTipo(String mes, int tipoId) {
        String query = "SELECT " +
                "i.id_movimiento AS id, " +
                "i.fecha AS date, " +
                "GROUP_CONCAT(p.nombre || ' (' || ppi.cantidad || ')') AS products " +
                "FROM " + DatabaseInit.TABLE_INGRESOS + " i " +
                "JOIN " + DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO + " ppi ON i.id_movimiento = ppi.id_ingreso " +
                "JOIN " + DatabaseInit.TABLE_PRODUCTOS + " p ON ppi.id_producto = p.id_producto " +
                "WHERE i.id_tipo=? AND " +
                "i.fecha LIKE ? " +
                "GROUP BY i.id_movimiento";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            mes = "%" + mes;
            cursor = db.rawQuery(query, new String[]{String.valueOf(tipoId), String.valueOf(mes)});
        }
        return cursor;
    }

    public Cursor getAllVentas() {
        String query = "SELECT " +
                "*" +
                "FROM " + DatabaseInit.TABLE_INGRESOS + " i " +
                "JOIN " + DatabaseInit.TABLE_PRODUCTOS_POR_INGRESO + " ppi ON i.id_movimiento = ppi.id_ingreso " +
                "JOIN " + DatabaseInit.TABLE_PRODUCTOS + " p ON ppi.id_producto = p.id_producto " +
                "WHERE i.id_tipo=?";
        SQLiteDatabase db = dbInit.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf("1")});
        }
        return cursor;
    }
}

