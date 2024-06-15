package com.example.kauppa_emp.fragments.Adapters;

        import android.content.Context;
        import android.database.Cursor;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.Spinner;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.kauppa_emp.R;
        import com.example.kauppa_emp.database.DatabaseHelper;

        import java.util.ArrayList;

public class CustomAdapterProductosVentasDialog extends RecyclerView.Adapter<CustomAdapterProductosVentasDialog.MyViewHolder> {

    private Context context;
    private ArrayList<String> arrayProdNombre, selectedProducts;

    public CustomAdapterProductosVentasDialog(Context context, ArrayList<String> arrayProdNombre) {
        this.context = context;
        this.arrayProdNombre = arrayProdNombre;
        this.selectedProducts = new ArrayList<>();
        this.selectedProducts.add("");  // Inicialmente con una fila
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_ventas_agregarproductos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arrayProdNombre);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerProductos.setAdapter(adapter);

        // Si ya se seleccionó un producto antes, mantener esa selección
        if (!selectedProducts.get(position).isEmpty()) {
            int spinnerPosition = adapter.getPosition(selectedProducts.get(position));
            holder.spinnerProductos.setSelection(spinnerPosition);
        }

        // Escuchar cambios en el spinner
        holder.spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedProducts.set(position, arrayProdNombre.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedProducts.size();
    }

    // Método para agregar un nuevo ítem
    public void addItem(String productName) {
        selectedProducts.add(productName);
        notifyItemInserted(selectedProducts.size() - 1);
    }

    public ArrayList<String> getSelectedProducts() {
        return selectedProducts;
    }

    public double getSelectedPriceSum(DatabaseHelper dbHelper){
        double montoTotal = 0;
        for (String item: selectedProducts){ // Sería más eficiente traer la Id de los productos y después buscarlos con ella... pero estoy cansado jefe
            Cursor cursor = dbHelper.getProductoByNombre(item);
            if (cursor.getCount() != 0){
                montoTotal += Double.parseDouble(cursor.getString(6));
            }
        }
        return montoTotal;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Spinner spinnerProductos;
        EditText editTextCantidad;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerProductos = itemView.findViewById(R.id.spinnerProductosDialogoVentas);
            editTextCantidad = itemView.findViewById(R.id.editTextCantDialogoVentas);
        }
    }
}