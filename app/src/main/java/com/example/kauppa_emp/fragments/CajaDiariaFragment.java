package com.example.kauppa_emp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.database.dataObjects.TiposMovimiento;
import com.example.kauppa_emp.fragments.Adapters.CustomAdapterCajaDiaria;
import com.example.kauppa_emp.database.dataObjects.Movimientos;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CajaDiariaFragment extends BaseFragment<Movimientos> {
    private TextView textView_IngresosCant_CajaDiaria,
            textView_EgresosCant_CajaDiaria,
            textView_TotalCant_CajaDiaria;

    private FloatingActionButton addButton;
    private FloatingActionButton button_irBalGnral_CajaDiaria;
    //Agregue boton wup
    private FloatingActionButton btn_Wapp_CajaDiaria;

    SimpleDateFormat dateFormat;
    String fechaActual;

    private BigDecimal bigDeciTotalIngres, bigDeciTotalEgres;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_caja_diaria;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recView_CajaDiaria;
    }

    @Override
    protected int getFiltrarButtonId() {
        return R.id.button_Filtrar_CajaDiaria;
    }

    protected int getAddButtonId() {
        return R.id.floatButton_add_CajaDiaria;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        textView_IngresosCant_CajaDiaria = view.findViewById(R.id.textView_IngresosCant_CajaDiaria);
        textView_EgresosCant_CajaDiaria = view.findViewById(R.id.textView_EgresosCant_CajaDiaria);
        textView_TotalCant_CajaDiaria = view.findViewById(R.id.textView_TotalCant_CajaDiaria);

        addButton = view.findViewById(getAddButtonId());
        addButton.setOnClickListener(v -> openAddDialog());

        button_irBalGnral_CajaDiaria = view.findViewById(R.id.button_irBalGnral_CajaDiaria);
        button_irBalGnral_CajaDiaria.setOnClickListener(v -> changeFragment(new BalGnralFragment()));

        //boton wup
        btn_Wapp_CajaDiaria = view.findViewById(R.id.floatButton_Whatsapp_CajaDiaria);
        btn_Wapp_CajaDiaria.setOnClickListener(v -> enviarReporte());

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaActual = dateFormat.format(Calendar.getInstance().getTime());

        return view;
    }



    @Override
    public void onResume(){
        super.onResume();
        calcularIngEgrTotal();
    }

    private void calcularIngEgrTotal() {

        NumberFormat format = NumberFormat.getInstance(new Locale("es", "ES"));
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);

        bigDeciTotalIngres = calcularTextViewIngresos();
        bigDeciTotalEgres = calcularTextViewEgresos();

        textView_IngresosCant_CajaDiaria.setText("$" + format.format(bigDeciTotalIngres));
        textView_EgresosCant_CajaDiaria.setText("$-" + format.format(bigDeciTotalEgres));
        textView_TotalCant_CajaDiaria.setText("$" + format.format(bigDeciTotalIngres.subtract(bigDeciTotalEgres)));
    }

    private BigDecimal calcularTextViewIngresos(){
        BigDecimal total = new BigDecimal(0);
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.VENTA) || itemTipo.equals(TiposMovimiento.SENIA) || itemTipo.equals(TiposMovimiento.VARIOSING)){
                total = total.add(new BigDecimal(item.getMonto()));
            }
        }
        return total;
    }

    private BigDecimal calcularTextViewEgresos(){
        BigDecimal total = new BigDecimal(0);
        for (Movimientos item : items) {
            String itemTipo = item.getIdTipo();
            if (itemTipo.equals(TiposMovimiento.COMPRA) || itemTipo.equals(TiposMovimiento.PAGO) || itemTipo.equals(TiposMovimiento.VARIOSEGR)){
                total = total.add(new BigDecimal(item.getMonto()));
            }
        }
        return total;
    }

    /*
      Este método lo copié del Main activity y no me termina de convencer la idea. Buscando, encontré
      que se puede agregar una interfaz para permitir a cualquier fragmento llamar a ese método, pero
      no sé cuánto me convence hacer eso si ningún otro fragmento tiene necesidad de usar esto.
    */
    public void changeFragment(Fragment newFragment) { //Tuve que repetir
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected Activity getFiltrarActivity() {
        return null;
    }

    @Override
    protected void bddToArraylist() {
        if (fechaActual != null){
            items = Movimientos.bddToArraylistByFecha(dbHelper, fechaActual);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CustomAdapterCajaDiaria(getActivity(), getContext(), items);
    }

    protected void openAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cajadiaria, null);

        TextView textView_Titulo_CajaDiaria = dialogView.findViewById(R.id.textView_Titulo_CajaDiaria);
        TextInputEditText editText_Monto_CajaDiaria = dialogView.findViewById(R.id.editText_Monto_CajaDiaria);
        TextInputEditText editText_Detalle_CajaDiaria = dialogView.findViewById(R.id.editText_Detalle_CajaDiaria);
        RadioGroup radGroup_Tipo_CajaDiaria = dialogView.findViewById(R.id.radGroup_Tipo_CajaDiaria);
        RadioButton radButton_Entrada_CajaDiaria = dialogView.findViewById(R.id.radButton_Entrada_CajaDiaria);

        createRadioGroup(radGroup_Tipo_CajaDiaria, radButton_Entrada_CajaDiaria, textView_Titulo_CajaDiaria);

        new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String monto = String.valueOf(editText_Monto_CajaDiaria.getText());
                    String detalle = String.valueOf(editText_Detalle_CajaDiaria.getText());
                    boolean esEntrada = radButton_Entrada_CajaDiaria.isChecked();



                    if (insertBDD(monto, detalle, esEntrada) != -1) {
                        Toast.makeText(getContext(), "Elemento agregado con éxito", Toast.LENGTH_SHORT).show();
                        addElementsToRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "Error al ingresar elemento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void createRadioGroup(RadioGroup radioGroupTipo, RadioButton radioButtonEntradas, TextView textViewTitulo) {
        radioButtonEntradas.setChecked(true);
        radioGroupTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radButton_Entrada_CajaDiaria) {
                textViewTitulo.setText("Agregar Entrada");
            } else if (checkedId == R.id.radButton_Salida_CajaDiaria) {
                textViewTitulo.setText("Agregar Salida");
            }
        });
    }

    private long insertBDD(String monto, String detalle, boolean esEntrada) {
        String tipo = esEntrada ? TiposMovimiento.VARIOSING : TiposMovimiento.VARIOSEGR;
        return dbHelper.addMovimiento(fechaActual, monto, detalle, Integer.parseInt(tipo));
    }

    //boton de whatsapp

    private void enviarReporte() {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Balance general del mes: " + "\nIngresos: " + textView_IngresosCant_CajaDiaria.getText().toString() + "\nEgresos: " + textView_EgresosCant_CajaDiaria.getText().toString() + "\nTotal: " + textView_TotalCant_CajaDiaria.getText().toString());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp"); //envia el mensaje al num elegido x nosotros
            try{
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getContext(), "Whatsapp no esta instalado", Toast.LENGTH_SHORT).show();
            }
    }
}
