package com.example.kauppa_emp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kauppa_emp.R;
import com.example.kauppa_emp.databinding.ActivityMainBinding;
import com.example.kauppa_emp.fragments.CajaDiariaFragment;
import com.example.kauppa_emp.fragments.EgresosFragment;
import com.example.kauppa_emp.fragments.HerramientasFragment;
import com.example.kauppa_emp.fragments.PedidosFragment;
import com.example.kauppa_emp.fragments.VentasFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Código para la navbar
        configureNavbarandSetFragment();
    }

    private void configureNavbarandSetFragment() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new CajaDiariaFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.caja_diaria) {
                changeFragment(new CajaDiariaFragment());
            } else if (item.getItemId() == R.id.ventas) {
                changeFragment(new VentasFragment());
            } else if (item.getItemId() == R.id.pedidos) {
                changeFragment(new PedidosFragment());
            } else if (item.getItemId() == R.id.compras) {
                changeFragment(new EgresosFragment());
            } else if (item.getItemId() == R.id.herramientas) {
                changeFragment(new HerramientasFragment());
            }
            return true;
        });
    }

    // Función para hacer funcionar la navbar
    private void changeFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment);
        fragmentTransaction.commit();
    }
}