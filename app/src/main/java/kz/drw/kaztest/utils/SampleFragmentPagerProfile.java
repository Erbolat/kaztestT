package kz.drw.kaztest.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kz.drw.kaztest.History;
import kz.drw.kaztest.MainActivity;
import kz.drw.kaztest.Profile;


public class SampleFragmentPagerProfile extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String[] tabTitles = MainActivity.profNames;


    public SampleFragmentPagerProfile(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Profile tab1 = new Profile();
                return tab1;
            case 1:
                History tab2 = new History();
                return tab2;

            default:
                return null;
        }}

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}