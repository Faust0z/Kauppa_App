package com.example.kauppa_emp.fragments.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.Productos;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CustomAdapterProdsAsociados extends RecyclerView.Adapter<CustomAdapterProdsAsociados.MyViewHolder> {
    private final Context context;
    protected ArrayList<Productos> prodsAsociados;
    protected ArrayList<Productos> prodsEliminados;
    protected Button buttonActualizar;
    private prodEditadoListener listener;

    private boolean isUserInput = false;

    public interface prodEditadoListener { // Listener para la comunicación con el dialog
        void seEditoProd(); // Se llamará a esto cada vez que se modifique un prod o su cant
    }

    public void setProdEditadoListener(prodEditadoListener listener) {
        this.listener = listener;
    }

    public ArrayList<Productos> getProdsAsociados(){
        return prodsAsociados;
    }

    public ArrayList<Productos> getProdsEliminados(){
        return prodsEliminados;
    }

    public CustomAdapterProdsAsociados(Context context, ArrayList<Productos> prodsAsociados, Button buttonActualizar) {
        this.context = context;
        this.prodsAsociados = prodsAsociados;
        this.prodsEliminados = new ArrayList<>();
        this.buttonActualizar = buttonActualizar;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filas_prods_asociados, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Productos prodActual = prodsAsociados.get(position);
        holder.textView_NombreProd_ProdsAsociados.setText(prodActual.getNombre());
        holder.textView_PrecUnit_ProdsAsociados.setText(prodActual.getPrecioUnitario());

        // Es necesario desvincularle cualquier TextWatcher al campo de cantidad primero
        if (holder.editText_Cant_ProdsAsociados.getTag() instanceof TextWatcher) {
            holder.editText_Cant_ProdsAsociados.removeTextChangedListener((TextWatcher) holder.editText_Cant_ProdsAsociados.getTag());
        }
        holder.editText_Cant_ProdsAsociados.setText(String.valueOf(prodActual.getCant()));

        TextWatcher textWatcher = new TextWatcher() {  // Este TextWatcher es para detectar cambios en el EditText de cantidad
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isUserInput = after > 0;  // Esto es para asegurarnos de que es input del usuario y del código que disparó la llamada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String editText_Cant = s.toString();
                if (!isUserInput) return;
                if (editText_Cant.isEmpty()) return;

                int cantidad = Integer.parseInt(editText_Cant);
                if (!prodActual.esFantasma() && cantidad > Integer.parseInt((prodActual.getStock()))){
                    Toast.makeText(context, "La cantidad del producto debe ser menor al stock existente", Toast.LENGTH_SHORT).show();
                    buttonActualizar.setEnabled(false);
                    holder.editText_Cant_ProdsAsociados.setTextColor(Color.RED);
                } else if (listener != null) {
                    prodActual.setCant(cantidad);
                    listener.seEditoProd();
                    buttonActualizar.setEnabled(true);
                    holder.editText_Cant_ProdsAsociados.setTextColor(Color.BLACK);
                }
            }
        };

        holder.editText_Cant_ProdsAsociados.addTextChangedListener(textWatcher);
        holder.editText_Cant_ProdsAsociados.setTag(textWatcher);
        holder.button_EliminarProd_ProdsAsociados.setOnClickListener(v -> createDeleteDialog(prodActual, position));
    }

    public String getPrecioTotal() {
        BigDecimal total = new BigDecimal(0);

        for (Productos producto : prodsAsociados) {
            BigDecimal precio = new BigDecimal(producto.getPrecioUnitario());
            BigDecimal cantidad = new BigDecimal(producto.getCant());
            total = total.add(precio.multiply(cantidad));
        }

        return String.valueOf(total);
    }

    private void createDeleteDialog(Productos prodActual, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar el producto " + prodActual.getId() + "?");

        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            prodsEliminados.add(prodActual);
            prodsAsociados.remove(prodActual);
            notifyItemRemoved(position);
            if (listener != null) {listener.seEditoProd();}
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return prodsAsociados.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_NombreProd_ProdsAsociados, textView_PrecUnit_ProdsAsociados;
        EditText editText_Cant_ProdsAsociados;
        Button button_EliminarProd_ProdsAsociados;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_NombreProd_ProdsAsociados = itemView.findViewById(R.id.textView_NombreProd_ProdsAsociados);
            textView_PrecUnit_ProdsAsociados = itemView.findViewById(R.id.textView_PrecUnit_ProdsAsociados);
            editText_Cant_ProdsAsociados = itemView.findViewById(R.id.editText_Cant_ProdsAsociados);
            button_EliminarProd_ProdsAsociados = itemView.findViewById(R.id.button_EliminarProd_ProdsAsociados);
        }
    }
}