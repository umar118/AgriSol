package com.example.agrisol.Expert;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.agrisol.Market.MarketPriceList;
import com.example.agrisol.R;
import com.example.agrisol.User.UserHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExpertDashboard extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_dashboard);

        loadFragment(new UserHome());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AgriSolutions");
        BottomNavigationView navigation1 = findViewById(R.id.navigation1);
        navigation1.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
    }





    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container1, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new UserHome();
                break;

            case R.id.navigation_expert:
                fragment = new ExpertDetials();
                break;

            case R.id.navigation_market:
                fragment = new MarketPriceList();
                break;

            case R.id.navigation_profile:
                fragment = new ExpertProfile();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
        }

        return loadFragment(fragment);

    }



}
