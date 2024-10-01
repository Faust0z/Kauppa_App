package com.example.kauppa_emp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.database.dataObjects.VentaDTO;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReporteAnualFragment extends Fragment {

    private MaterialButton btnVolverReporteAnual;
    private WebView webViewReporteAnual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reporte_anual, container, false);
    }

    @SuppressLint("Range")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Se define un manejador de evento para el botón "retroceder", permitiendo
        // al usuario volver a la pantalla de herramientas.
        btnVolverReporteAnual = view.findViewById(R.id.btnVolverReporteAnual);
        btnVolverReporteAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop fragment from stack
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        // Inicializar el WebView
        webViewReporteAnual = view.findViewById(R.id.webViewReporteAnual);
        // Placeholder:
        //webViewReporteMensual.loadUrl("https://www.google.com");

        DatabaseHelper dbhelper = new DatabaseHelper(ReporteAnualFragment.this.getContext());


        Cursor nose = dbhelper.getIngresosByAnioAndTipo("2024",1);
        Cursor nose1 = dbhelper.getAllVentas();

        //debugging
        System.out.println("ventas por mes y tipo:");
        System.out.println(nose.getCount());
        System.out.println("todas las ventas:");
        System.out.println(nose1.getCount());
        //fin debugging

        // Generar lista de objetos a partir del cursor
        ArrayList<VentaDTO> ventasDTO = new ArrayList<VentaDTO>();
        while (nose.moveToNext()){
            VentaDTO venta = new VentaDTO();
            venta.setId(nose.getInt(nose.getColumnIndex("id")));
            // Parseo manual de fecha
            String dateString = nose.getString(nose.getColumnIndex("date"));
            String day = dateString.substring(0, 2);
            String month = dateString.substring(3, 5);
            String year = dateString.substring(6);
            venta.setDate(year + "-" + month + "-" + day);

            venta.setProducts(nose.getString(nose.getColumnIndex("products")));
            System.out.println(venta.getId());
            System.out.println(venta.getDate());
            System.out.println(venta.getProducts());
            ventasDTO.add(venta);
        }
        // Objetos a json
        Gson gson = new Gson();
        String json = gson.toJson(ventasDTO);
        System.out.println(json);
        // Ejecutar post request en paralelo
        new ExecuteRequest().execute(json);


    }

    public static class LineaProductoMasVendido {

        private final String nombre;
        private final int unidades;
        private final int puesto;

        public LineaProductoMasVendido(String nombre, int unidades, int puesto) {
            this.nombre = nombre;
            this.unidades = unidades;
            this.puesto = puesto;
        }

        public String getNombre() {
            return nombre;
        }

        public int getUnidades() {
            return unidades;
        }

        public int getPuesto() {
            return puesto;
        }

    }

    public static class LineaProductoMayorGanancia {

        private final String nombre;
        private final double ganancia;
        private final int puesto;

        public LineaProductoMayorGanancia(String nombre, double ganancia, int puesto) {
            this.nombre = nombre;
            this.ganancia = ganancia;
            this.puesto = puesto;
        }

        public String getNombre() {
            return nombre;
        }

        public double getGanancia() {
            return ganancia;
        }

        public int getPuesto() {
            return puesto;
        }

    }

    /**
     * Esta clase se usa para ejecuta el request en paralelo evitando el
     * trabajo en el núcleo principal
     */
    private class ExecuteRequest extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            String json = params[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"), json);

            Request request = new Request.Builder()
                    .url("http://192.168.1.152:8080/web-report/monthly")
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);




            try {
                Response response = call.execute();
                //En caso de éxito se retorna el response body
                return(response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String html){
            if (html != null){
                // Exito: se carga el html en el webview
                webViewReporteAnual.loadData(html, "text/html; charset=utf-8", null);
            } else {
                // Falla: se prepara y muestra un diálogo de alerta
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("No se pudo establecer una conexión con el servicio de reportes.")
                        .setTitle("Error de conexión");
                AlertDialog connectionDialog = builder.create();
                connectionDialog.show();
            }
        }
    }
}
