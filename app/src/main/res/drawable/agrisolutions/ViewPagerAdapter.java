package com.example.agrisolutions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments =new ArrayList<>();
    ArrayList<String> strings =new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new UserHome();
            case 1:
                return new ExpertDetails();
            case 2:
                return new MarketPrice();
            case 3:
                return new UserProfile();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void add(Fragment fr, String str){
        fragments.add(fr);
        strings.add(str);
    }
    @NonNull
    @Override
    public CharSequence getPageTitle(int position){
        return strings.get(position);
    }
}
