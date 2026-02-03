package com.example.examen_mov_unax_evalu2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.examen_mov_unax_evalu2.fragments.ListaFragment;
import com.example.examen_mov_unax_evalu2.fragments.ResumenFragment;
import com.example.examen_mov_unax_evalu2.fragments.TiendasFragment;

public class MainAdapter extends FragmentStateAdapter {

    public MainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TiendasFragment();
            case 1:
                return new ListaFragment();
            case 2:
                return new ResumenFragment();
            default:
                return new TiendasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Tenemos 3 pestañas
    }
}