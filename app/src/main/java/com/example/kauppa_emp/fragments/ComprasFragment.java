package com.example.kauppa_emp.fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.DatabaseHelper;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCompras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ComprasFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private Button buttonFiltrar, buttonResetFiltro;

    private DatabaseHelper dbHelper;
    private ArrayList<String> arrayMovId, arrayMovFecha, arrayMovDetalle, arrayMovMonto, arrayMovIdPedidos, arrayMovIdTipo, arrayMovNomCliente;
    private CustomAdapterCompras customAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        // Ni el detalle ni el pedidos afectados se usa para el recyclerview, pero se necesita
        // almacenar para poder ver todos los datos del movimiento luego
        arrayMovId = new ArrayList<>();
        arrayMovFecha = new ArrayList<>();
        arrayMovMonto = new ArrayList<>();
        arrayMovDetalle = new ArrayList<>();
        arrayMovIdPedidos = new ArrayList<>();
        arrayMovIdTipo = new ArrayList<>();
        arrayMovNomCliente = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);

        buttonFiltrar = view.findViewById(R.id.buttonFiltrarCompras);
        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    buttonFiltrar.setText(dateFormat.format(calendar.getTime()));

                    addElementsToRecyclerView();

                }, year, month, day);
                datePickerDialog.show();
            }
        });

        buttonResetFiltro = view.findViewById(R.id.buttonResetFiltroCompras);
        buttonResetFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpiamos el campo de filtro y llamamos para refrescar los datos del recyclerview
                buttonFiltrar.setText("");
                addElementsToRecyclerView();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewCompras);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addElementsToRecyclerView();

        return view;
    }

    @Override // Usamos este método para que, si borramos o actualizamos desde la actividad MasInfo, se actualice la lista
    public void onResume() {
        super.onResume();
        bddToArraylist();
        addElementsToRecyclerView();
    }

    private void addElementsToRecyclerView(){
        bddToArraylist();
        customAdapter = new CustomAdapterCompras(getActivity(), getContext(), arrayMovId, arrayMovFecha,
                arrayMovDetalle, arrayMovMonto, arrayMovIdPedidos, arrayMovIdTipo, arrayMovNomCliente);
        recyclerView.setAdapter(customAdapter);
    }

    private void bddToArraylist(){
        // Vacío los arraylist antes de volverles a insertar toda la BDD para evitar duplicados
        arrayMovId.clear();
        arrayMovFecha.clear();
        arrayMovMonto.clear();
        arrayMovDetalle.clear();
        arrayMovIdPedidos.clear();
        arrayMovIdTipo.clear();
        arrayMovNomCliente.clear();

        Cursor cursor;
        if (!buttonFiltrar.getText().toString().isEmpty()){
            cursor = dbHelper.getComprasByFecha(buttonFiltrar.getText().toString());
        }else{
            cursor = dbHelper.getAllCompras();
        }

        // Si obtuvimos datos, volcarlos en los arraylists
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                arrayMovId.add(cursor.getString(0));
                arrayMovFecha.add(cursor.getString(1));
                arrayMovDetalle.add(cursor.getString(2));
                arrayMovMonto.add(cursor.getString(3));
                arrayMovIdPedidos.add(cursor.getString(4));
                arrayMovIdTipo.add(cursor.getString(5));
                arrayMovNomCliente.add(cursor.getString(6));
            }
        }
    }
}
