package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.database.DatabaseHelper;

import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public abstract class BaseFragment<T> extends Fragment {
    protected RecyclerView recyclerView;
    protected Button buttonFiltrar;

    protected DatabaseHelper dbHelper;
    protected ArrayList<T> items;
    protected RecyclerView.Adapter customAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        items = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        createButtonFiltrar(view);

        recyclerView = view.findViewById(getRecyclerViewId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addElementsToRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addElementsToRecyclerView();
    }

    protected void createButtonFiltrar(View view) {
        buttonFiltrar = view.findViewById(getFiltrarButtonId());
        buttonFiltrar.setOnClickListener(v -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                calendar.set(year1, month1, dayOfMonth);

                Intent intent = new Intent(getContext(), getFiltrarActivity().getClass());
                intent.putExtra("fecha", dateFormat.format(calendar.getTime()));
                startActivity(intent);
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    protected void addElementsToRecyclerView() {
        bddToArraylist();
        customAdapter = getAdapter();
        recyclerView.setAdapter(customAdapter);
    }

    protected abstract Activity getFiltrarActivity();

    protected abstract void bddToArraylist();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract int getLayoutId();

    protected abstract int getRecyclerViewId();

    protected abstract int getFiltrarButtonId();
}
