package com.example.kauppa_emp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MasInfoCajaDiaria extends AppCompatActivity {

    private EditText movimiento_id, movimiento_fecha, movimiento_detalle, movimiento_monto, movimiento_pedidosAfectados, movimiento_tipo;
    private String idPedidoAfectado;
    private Button actualizarButton, anularButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info_caja_diaria);

        movimiento_pedidosAfectados = findViewById(R.id.editTextIdPedidoAfectadoCajaDiariaInfo);

        idPedidoAfectado = obtenerIdPedidoAfectado();

        if (idPedidoAfectado != null && !idPedidoAfectado.isEmpty()) {
            movimiento_pedidosAfectados.setVisibility(View.VISIBLE);
            movimiento_pedidosAfectados.setText(idPedidoAfectado);
        } else {
            movimiento_pedidosAfectados.setVisibility(View.GONE);
        }
    }

    private String obtenerIdPedidoAfectado() {
        // Implementa la lógica para obtener el valor de id_pedido_afectado
        return "12345"; // Ejemplo de valor; reemplázalo con tu lógica
    }
}

 /*
    private void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cajadiaria, null);

        TextView textViewTitulo = dialogView.findViewById(R.id.textViewTitulo);
        EditText editTextFecha = dialogView.findViewById(R.id.editTextFecha);
        EditText editTextMonto = dialogView.findViewById(R.id.editTextMonto);
        EditText editTextDetalle = dialogView.findViewById(R.id.editTextDetalle);
        RadioGroup radioGroupTipo = dialogView.findViewById(R.id.radioGroupTipo);
        RadioButton radioButtonEntradas = dialogView.findViewById(R.id.radioButtonEntradas);
        CheckBox checkBoxAgregar = dialogView.findViewById(R.id.checkBoxAgregar);

        // Configurar la fecha actual por defecto
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        editTextFecha.setText(dateFormat.format(calendar.getTime()));

        // Mostrar el DatePickerDialog al hacer clic en el campo de fecha
        editTextFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                editTextFecha.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        // Cambiar el título y la checkbox según sea una Compra/Venta
        radioButtonEntradas.setChecked(true);
        radioGroupTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEntradas) {
                checkBoxAgregar.setText("Agregar como Venta");
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radioButtonSalidas) {
                checkBoxAgregar.setText("Agregar como Compra");
                textViewTitulo.setText("Agregar Salida");
            }
        });

        // Cuando se aprete el botón "Agregar", se toman todos los inputs y se los procesa.
        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String fecha = editTextFecha.getText().toString();
                    String detalle = editTextDetalle.getText().toString();
                    double monto;
                    try {
                        monto = Double.parseDouble(editTextMonto.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Error, no se ingresó un monto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean esVentaOCompra = checkBoxAgregar.isChecked();
                    boolean esEntrada = radioButtonEntradas.isChecked();

                    if (insertBDD(fecha, detalle, monto, esEntrada, esVentaOCompra) != -1){
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    }else{
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

  */