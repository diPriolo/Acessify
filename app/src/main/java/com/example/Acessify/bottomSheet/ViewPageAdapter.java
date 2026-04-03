package com.example.Acessify.bottomSheet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Acessify.model.Local;

public class ViewPageAdapter extends FragmentStateAdapter {
    Local local;


    public ViewPageAdapter(@NonNull Fragment fragment, Local lc) {
        super(fragment);
        this.local = lc;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment frag;
        switch (position) {

                case 0:

                    return GeralSheet.newInstance(local);
                case 1:
                    return GaleriaSheet.newInstance(local);
                case 2:
                    return AvaliacaoSheet.newInstance(local);
                default:
                    return GeralSheet.newInstance(local);
            }

    }

    @Override
    public int getItemCount() {
        return 3; // número de abas
    }
}
