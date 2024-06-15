package com.example.kauppa_emp.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kauppa_emp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CalcularPrecioFragment extends Fragment {

    private TextInputEditText inputCostoProducto;
    private TextInputEditText inputGastoAdicional;
    private TextInputEditText inputPorcentajeGanancia;
    private TextView textPrecioVenta;
    private MaterialButton btnCalcularPrecio;
    private MaterialButton btnVolverCalcularPrecio;
    private MaterialButton btnCopiarPrecioVenta;

    private ClipboardManager clipboardManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calcular_precio, container, false);

        // Se define un manejador de evento para el botón "retroceder", permitiendo
        // al usuario volver a la pantalla de herramientas.
        btnVolverCalcularPrecio = view.findViewById(R.id.btnVolverCalcularPrecio);
        btnVolverCalcularPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace fragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new HerramientasFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Se obienen los datos necesarios para realizar el calculo del precio final.
        inputCostoProducto = view.findViewById(R.id.inputCostoProducto);
        inputGastoAdicional = view.findViewById(R.id.inputGastoAdicional);
        inputPorcentajeGanancia = view.findViewById(R.id.inputPorcentajeGanancia);
        textPrecioVenta = view.findViewById(R.id.textPrecioVenta);
        btnCalcularPrecio = view.findViewById(R.id.btnCalcularPrecio);
        btnCopiarPrecioVenta = view.findViewById(R.id.btnCopiarPrecioVenta);

        clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        // Se define un manejador de evento para el botón "calcular", permitiendo
        // al usuario realizar el cualculo del precio de venta.
        btnCalcularPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String precioVenta = textPrecioVenta.getText().toString();

                // Operaciones necesarias con los valores obtenidos. Preparandolos
                // para posteriormente realizar el calculo del precio de venta.

                double costo = getDoubleValue(inputCostoProducto);
                double gasto = getDoubleValue(inputGastoAdicional);
                double porcentaje = getDoubleValue(inputPorcentajeGanancia);
                double precio = calcularPrecioVenta(costo, gasto, porcentaje);

                textPrecioVenta.setText(String.format("%.2f", precio));

            }
        });

        btnCopiarPrecioVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precioVenta = textPrecioVenta.getText().toString();
                if (!precioVenta.isEmpty()) {
                    ClipData clipData = ClipData.newPlainText("Precio de Venta", precioVenta);
                    clipboardManager.setPrimaryClip(clipData);
                    // Notificar al usuario que se ha copiado al portapapeles
                    Toast.makeText(requireContext(), "Precio de Venta copiado al portapapeles.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;

    }

    private double getDoubleValue(TextInputEditText editText) {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    private double calcularPrecioVenta(double costo, double gasto, double porcentaje) {
        // Formula para calcular el precio de venta

        return (costo + gasto) * ((porcentaje / 100) + 1);
    }

}
