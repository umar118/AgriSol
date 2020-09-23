package com.example.agrisolutions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ExpertDashboard extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_dashboard);

        tabLayout =findViewById(R.id.tableLayout);
        viewpager =findViewById(R.id.viewPager);

        ViewPagerAdapter adapterf =new ViewPagerAdapter(getSupportFragmentManager());
        adapterf.add(new HomeFragment(),"Home");
        adapterf.add(new ExpertProfile(),"Profile");
        viewpager.setAdapter(adapterf);
        tabLayout.setupWithViewPager(viewpager);
    }
}
