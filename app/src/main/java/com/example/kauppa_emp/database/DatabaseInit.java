package com.example.kauppa_emp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;

public class DatabaseInit extends SQLiteOpenHelper {
    /*
        Clase para la creación de la BDD y sus tablas, así como algunos inserts en ellas.
     */
    private static final String DB_NAME = "mydb.db";
    private static final int DB_VER = 3;

    public static final String TABLE_PRODUCTOS = "PRODUCTOS";
    public static final String TABLE_PEDIDOS = "PEDIDOS";
    public static final String TABLE_MOVIMIENTOS = "MOVIMIENTOS";
    public static final String TABLE_INGRESOS = "INGRESOS";
    public static final String TABLE_EGRESOS = "EGRESOS";
    public static final String TABLE_TIPOS_MOVIMIENTO = "TIPOS_MOVIMIENTO";
    public static final String TABLE_ESTADOS_PEDIDO = "ESTADOS_PEDIDO";
    public static final String TABLE_PRODUCTOS_POR_INGRESO = "PRODUCTOS_POR_INGRESO";
    public static final String TABLE_PRODUCTOS_POR_PEDIDO = "PRODUCTOS_POR_PEDIDO";

    private static final String SQL_TO_CREATE_TABLE_MOVIMIENTOS = "CREATE TABLE " + TABLE_MOVIMIENTOS + " ("
            + "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "monto TEXT NOT NULL, "
            + "detalle TEXT, "
            + "id_tipo INTEGER NOT NULL) ";
    private static final String SQL_TO_CREATE_TABLE_INGRESOS = "CREATE TABLE " + TABLE_INGRESOS + " ("
            + "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "monto TEXT NOT NULL, "
            + "detalle TEXT, "
            + "id_tipo INTEGER NOT NULL, "
            + "nombre_cliente TEXT) ";
    private static final String SQL_TO_CREATE_TABLE_EGRESOS = "CREATE TABLE " + TABLE_EGRESOS + " ("
            + "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "monto TEXT NOT NULL, "
            + "detalle TEXT, "
            + "id_tipo INTEGER NOT NULL, "
            + "nombre_cliente TEXT) ";
    private static final String SQL_TO_CREATE_TABLE_TIPOS_MOVIMIENTO = "CREATE TABLE " + TABLE_TIPOS_MOVIMIENTO + " ("
            + "id_tipo INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "descripcion TEXT NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_PEDIDOS = "CREATE TABLE " + TABLE_PEDIDOS + " ("
            + "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "fecha TEXT NOT NULL, "
            + "fecha_entrega TEXT NOT NULL, "
            + "detalle TEXT, "
            + "senia TEXT NOT NULL, "
            + "resto TEXT NOT NULL, "
            + "nombre_cliente TEXT, "
            + "celular_cliente TEXT, "
            + "id_estado INTEGER NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_ESTADOS_PEDIDO = "CREATE TABLE " + TABLE_ESTADOS_PEDIDO + " ("
            + "id_estado INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "descripcion TEXT NOT NULL)";
    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS = "CREATE TABLE " + TABLE_PRODUCTOS + " ("
            + "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nombre TEXT NOT NULL, "
            + "stock INTEGER, "
            + "fecha_actualizacion TEXT, "
            + "precio_unitario TEXT)";
    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS_POR_INGRESO = "CREATE TABLE " + TABLE_PRODUCTOS_POR_INGRESO + " ("
            + "id_venta INTEGER NOT NULL, "
            + "id_producto INTEGER, "
            + "cantidad INTEGER NOT NULL, "
            + "subtotal TEXT NOT NULL, "
            + "PRIMARY KEY (id_venta, id_producto))";
    private static final String SQL_TO_CREATE_TABLE_PRODUCTOS_POR_PEDIDO = "CREATE TABLE " + TABLE_PRODUCTOS_POR_PEDIDO + " ("
            + "id_pedido INTEGER NOT NULL, "
            + "id_producto INTEGER, "
            + "cantidad INTEGER NOT NULL, "
            + "subtotal TEXT NOT NULL, "
            + "PRIMARY KEY (id_pedido, id_producto))";

    private static final String SQL_INSERT_TIPOS_MOVIMIENTO = "INSERT INTO TIPOS_MOVIMIENTO (id_tipo, descripcion) VALUES " +
            "(" + TiposMovimiento.VENTA + ", 'Venta Detallada'), " +
            "(" + TiposMovimiento.SENIA + ", 'Seña'), " +
            "(" + TiposMovimiento.VARIOSING + ", 'Ingresos Varios'), " +
            "(" + TiposMovimiento.COMPRA + ", 'Compra'), " +
            "(" + TiposMovimiento.PAGO + ", 'Pago'), " +
            "(" + TiposMovimiento.VARIOSEGR + ", 'Egresos Varios'), " +
            "(" + TiposMovimiento.PEDIDO + ", 'Pedido');";
    private static final String SQL_INSERT_ESTADOS_PEDIDO = "INSERT INTO ESTADOS_PEDIDO (id_estado, descripcion) VALUES " +
            "(1, 'SIN EMPEZAR'), " +
            "(2, 'EN CURSO'), " +
            "(3, 'LISTO'), " +
            "(4, 'ENTREGADO');";
    private static final String SQL_INSERT_PRODUCTOS = "INSERT INTO " + TABLE_PRODUCTOS +
            " (id_producto, nombre, stock, fecha_actualizacion, precio_unitario) VALUES " +
            "(1, 'Producto A', 100, '2023-07-01', '100'), " +
            "(2, 'Producto B', 50, '2023-07-01', '150'), " +
            "(3, 'Producto C', 200, '2023-07-01', '1000'), " +
            "(4, 'Producto D', 0, '2023-07-01', '3000'), " +
            "(5, 'Producto E', 150, '2023-07-01', '777');";
    private static final String SQL_INSERT_INGRESOS = "INSERT INTO " + TABLE_INGRESOS + " (id_movimiento, fecha, monto, detalle, id_tipo, nombre_cliente) VALUES " +
            "(1, '23/07/2024', '100', 'Detalle de ingreso 1', 1, 'Cliente A'), " +
            "(4, '25/04/2023', '300', 'Detalle de ingreso 4', 3, 'Cliente D'), " +
            "(5, '30/05/2023', '250', 'Detalle de ingreso 5', 2, 'Cliente E');";
    private static final String SQL_INSERT_EGRESOS = "INSERT INTO " + TABLE_EGRESOS + " (id_movimiento, fecha, monto, detalle, id_tipo, nombre_cliente) VALUES " +
            "(1, '05/01/2023', '50.0', 'Detalle de egreso 1', 4, 'Prov F'), " +
            "(2, '20/02/2023', '75.0', 'Detalle de egreso 2', 4, 'Prov G'), " +
            "(3, '15/03/2023', '100.0', 'Detalle de egreso 3', 6, 'Prov H'), " +
            "(4, '30/04/2023', '200.0', 'Detalle de egreso 4', 6, 'Aramis'), " +
            "(5, '10/05/2023', '125.0', 'Detalle de egreso 5', 5, 'Gato Negro');";



    public DatabaseInit(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TO_CREATE_TABLE_MOVIMIENTOS);
        db.execSQL(SQL_TO_CREATE_TABLE_INGRESOS);
        db.execSQL(SQL_TO_CREATE_TABLE_EGRESOS);
        db.execSQL(SQL_TO_CREATE_TABLE_TIPOS_MOVIMIENTO);
        db.execSQL(SQL_TO_CREATE_TABLE_PEDIDOS);
        db.execSQL(SQL_TO_CREATE_TABLE_ESTADOS_PEDIDO);
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS);
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS_POR_INGRESO);
        db.execSQL(SQL_TO_CREATE_TABLE_PRODUCTOS_POR_PEDIDO);

        db.execSQL(SQL_INSERT_TIPOS_MOVIMIENTO);
        db.execSQL(SQL_INSERT_ESTADOS_PEDIDO);
        db.execSQL(SQL_INSERT_PRODUCTOS);
        db.execSQL(SQL_INSERT_INGRESOS);
        db.execSQL(SQL_INSERT_EGRESOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMIENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGRESOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EGRESOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_MOVIMIENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTADOS_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_POR_INGRESO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS_POR_PEDIDO);
        onCreate(db);
    }
}
