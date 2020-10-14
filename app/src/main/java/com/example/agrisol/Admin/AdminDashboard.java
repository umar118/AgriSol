package com.example.agrisol.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.agrisol.Expert.ExpertDetials;
import com.example.agrisol.Market.MarketPriceList;
import com.example.agrisol.R;
import com.example.agrisol.UpdateExpert.Admin_UpdateExpert;
import com.example.agrisol.Market.UpdateMarketList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView  navigationView;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AgriSolutions");

        loadFragment(new AdminHome());

        drawerLayout =findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView =findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(navDraw);

        nav = findViewById(R.id.navigation5);
        nav.setOnNavigationItemSelectedListener(navCustomView);
    }




private  BottomNavigationView.OnNavigationItemSelectedListener navCustomView =new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new AdminHome();
                break;

            case R.id.navigation_expert:
                fragment = new ExpertDetials();
                break;

            case R.id.navigation_market:
                fragment = new MarketPriceList();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
        }
        return loadFragment(fragment);
    }
};


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container5, fragment)
                    .commit();
            return true;
        }
        return false;
    }


private NavigationView.OnNavigationItemSelectedListener navDraw = new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.nav_update:
                break;

            case R.id.nav_add_expert:
                startActivity(new Intent(getApplicationContext(),AddExpertDetials.class));

                break;
            case R.id.nav_update_expert:
                startActivity(new Intent(getApplicationContext(), Admin_UpdateExpert.class));
                break;
            case R.id.nav_add_price:
              startActivity(new Intent(getApplicationContext(), AddMarketList.class));
                break;
            case R.id.nav_update_market:
               startActivity(new Intent(getApplicationContext(), UpdateMarketList.class));
                break;

            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("com.example.agrisol"));
                startActivity(intent);

                break;
            case R.id.nav_send:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Your body here";
                String sub = "Your Subject";
                myIntent.putExtra(Intent.EXTRA_PACKAGE_NAME,body);
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
};

}
