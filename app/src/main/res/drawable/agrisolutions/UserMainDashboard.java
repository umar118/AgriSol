package com.example.agrisolutions;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class UserMainDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar tool_bar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    TabLayout tabLayout;
    ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_dashboard);

        tool_bar =findViewById(R.id.toolbar2);
        setSupportActionBar(tool_bar);

        drawerLayout =findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,tool_bar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView =findViewById(R.id.navigation_view1);
       navigationView.setNavigationItemSelectedListener(this);






        tabLayout =findViewById(R.id.usertableLayout);
        viewpager =findViewById(R.id.UserViewPager);

        ViewPagerAdapter adapter =new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new UserHome(),"Home");
        adapter.add(new ExpertDetails(),"Expert");
        adapter.add(new MarketPrice(),"Market");
        adapter.add(new UserProfile(),"Profile");
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_About_us:
                Toast.makeText(this,"Updated Profile",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_setting:
                Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_update_Profile){
            
        }
        else 
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
