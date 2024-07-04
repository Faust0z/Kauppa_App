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
import com.example.kauppa_emp.fragments.dataObjects.Egresos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ComprasFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private Button buttonFiltrar, buttonResetFiltro;

    private DatabaseHelper dbHelper;
    private ArrayList<Egresos> egresos;
    private CustomAdapterCompras customAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        egresos = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);

        createButtonFiltrar(view);

        // Todo: eliminar este botón
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
        addElementsToRecyclerView();
    }

    private void createButtonFiltrar(View view){
        buttonFiltrar = view.findViewById(R.id.buttonFiltrarCompras);
        buttonFiltrar.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);
                buttonFiltrar.setText(dateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();

            addElementsToRecyclerView();
        });
    }

    private void addElementsToRecyclerView(){
        bddToArraylist();
        customAdapter = new CustomAdapterCompras(getActivity(), getContext(), egresos);
        recyclerView.setAdapter(customAdapter);
    }

    private void bddToArraylist(){
        egresos.clear();

        // Todo: la funcionalidad de esto pasará a una activity, por lo que no es necesaria acá
        Cursor cursor;
        if (!buttonFiltrar.getText().toString().isEmpty()){
            cursor = dbHelper.getComprasByFecha(buttonFiltrar.getText().toString());
        }else{
            cursor = dbHelper.getAllCompras();
        }

        // Si obtuvimos datos, volcarlos en los arraylists
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String fecha = cursor.getString(1);
                String monto = cursor.getString(2);
                String detalle = cursor.getString(3);
                String idTipo = cursor.getString(4);
                String nomCliente = cursor.getString(5);

                Egresos egreso = new Egresos(id, fecha, monto, detalle, idTipo, nomCliente);
                egresos.add(egreso);
            }
        }
    }
}
