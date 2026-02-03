package com.example.examen_mov_unax_evalu2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.OnBackPressedCallback; // IMPORTANTE: Importar esto
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examen_mov_unax_evalu2.adapters.MainAdapter;
import com.example.examen_mov_unax_evalu2.fragments.TiendasFragment;
import com.example.examen_mov_unax_evalu2.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TiendasFragment.OnTiendaInteractionListener {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        MainAdapter adapter = new MainAdapter(this);
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager.setVisibility(View.VISIBLE);

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }

                if (item.getItemId() == R.id.nav_tiendas) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.nav_lista) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() == R.id.nav_resumen) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_tiendas);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_lista);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_resumen);
                        break;
                }
            }
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    viewPager.setVisibility(View.VISIBLE); // Volvemos a mostrar el ViewPager
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onMapRequested(double lat, double lon, String name) {

        Store store = new Store();
        store.setLat(lat);
        store.setLon(lon);
        store.setName(name);

        String uriString = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + android.net.Uri.encode(name) + ")";

        android.net.Uri uri = android.net.Uri.parse(uriString);
        android.content.Intent mapIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, uri));
        }
    }
    }
