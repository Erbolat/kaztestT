package kz.drw.kaztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import kz.drw.kaztest.utils.SampleFragmentPagerAdapter;
import kz.drw.kaztest.utils.SampleFragmentPagerProfile;


public class MyProfile extends Fragment {
    private PagerSlidingTabStrip tabsStrip;
    private LinearLayout mTabsLinearLayout;
    ImageButton imgBtnBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        setUpTabStrip();
        viewPager.setAdapter(new SampleFragmentPagerProfile(getChildFragmentManager()));

        tabsStrip.setViewPager(viewPager);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
                    if(i == position){
                        tv.setTextColor(getResources().getColor(R.color.colorWhite));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.colorWhiter));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return  view;
    }




    public void setUpTabStrip(){
        mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
            if(i == 0){
                tv.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                tv.setTextColor(getResources().getColor(R.color.colorWhiter));
            }
        }
    }
}