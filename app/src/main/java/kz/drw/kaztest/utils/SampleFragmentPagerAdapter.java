package kz.drw.kaztest.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kz.drw.kaztest.MainActivity;
import kz.drw.kaztest.RatingItem;
import kz.drw.kaztest.isDev;


public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String[] tabTitles = new String[]{MainActivity.categ[0], MainActivity.categ[1],MainActivity.categ[2]};


    public SampleFragmentPagerAdapter(FragmentManager fragmentManager) {
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
                RatingItem tab1 = new RatingItem();
                return tab1;
            case 1:
                isDev tab2 = new isDev();
                return tab2;
            case 2:
                isDev tab3 = new isDev();
                return tab3;

            default:
                return null;
        }}

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}