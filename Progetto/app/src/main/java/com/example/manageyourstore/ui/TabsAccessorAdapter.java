package com.example.manageyourstore.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.manageyourstore.assistenza.AssistenzaFragment;
import com.example.manageyourstore.ordini.OrdiniFragment;
import com.example.manageyourstore.stock.StockFragment;

public class TabsAccessorAdapter extends FragmentPagerAdapter {


    public TabsAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:
                StockFragment stockFragment = new StockFragment();
                return stockFragment;

            case 1:
                OrdiniFragment ordiniFragment = new OrdiniFragment();
                return ordiniFragment;

            case 2:
                AssistenzaFragment assistenzaFragment = new AssistenzaFragment();
                return assistenzaFragment;

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Stock";
            case 1:
                return "Ordini";
            case 2:
                return "Assistenza";
            default: return null;

        }
    }



}
