package kz.drw.kaztest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.SampleFragmentPagerAdapter;


public class Rating extends AppCompatActivity {
    private PagerSlidingTabStrip tabsStrip;
    private LinearLayout mTabsLinearLayout;
    TextView tvTestStart;
    ImageButton imgBtnBack;
    int thisDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
        String date = dff.format(Calendar.getInstance().getTime());
        String[] dates = date.split("-");
        thisDay = Integer.parseInt(dates[0]);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);
        tvTestStart = (TextView) findViewById(R.id.tvTestStart);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        setUpTabStrip();
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
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
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTestStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(thisDay>=32)
                {
                    startActivity(new Intent(Rating.this, TestRating.class));
                }
                else {
                    if(Constants.kaztestLang)
                    Toast.makeText(Rating.this, 32-thisDay+" күн қалды", Toast.LENGTH_SHORT).show();
                    else  {
                        if(32-thisDay==1)
                            Toast.makeText(Rating.this, "Осталось "+(32-thisDay)+" день", Toast.LENGTH_SHORT).show();
                        else if(32-thisDay==2 || 32-thisDay==3 || 32-thisDay==4)
                        Toast.makeText(Rating.this, "Осталось "+(32-thisDay)+" дня", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(Rating.this, "Осталось "+(32-thisDay)+" дней", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void setUpTabStrip(){
        mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
            if(i == 0){
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tv.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        }
    }
}