package com.example.manageyourstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manageyourstore.ui.TabsAccessorAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.app_bar_layout, null);

        logout = v.findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
                Toast.makeText(MainActivity.this, "Logout avvenuto con successo", Toast.LENGTH_SHORT).show();
            }
        });

        actionBar.setCustomView(v);

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager(), 0);
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        myTabLayout.getTabAt(0).setIcon(R.drawable.ic_stock);
        myTabLayout.getTabAt(1).setIcon(R.drawable.ic_orders);
        myTabLayout.getTabAt(2).setIcon(R.drawable.ic_assistance);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Intent pauseIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(pauseIntent);
    }
}