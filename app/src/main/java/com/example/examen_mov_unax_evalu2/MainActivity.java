package com.example.examen_mov_unax_evalu2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        MainAdapter adapter = new MainAdapter(this);
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
    }
}