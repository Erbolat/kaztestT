package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.astuetz.PagerSlidingTabStrip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.ListViewMaxHeight;
import kz.drw.kaztest.utils.SampleFragmentPagerAdapter;


public class Rating extends AppCompatActivity {
    private PagerSlidingTabStrip tabsStrip;
    private LinearLayout mTabsLinearLayout, layTestStart;
    TextView tvTestStart;
    ImageButton imgBtnBack;
    public static DialogInf dlgInf;
    public  static String [] info2;
    int thisDay;
    static  int isTime=0;
   static long currentTime=0, lastDay=0, nextFirstDay=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
        final String date = dff.format(Calendar.getInstance().getTime());
        String[] dates = date.split("-");
        thisDay = Integer.parseInt(dates[0]);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);
        tvTestStart = (TextView) findViewById(R.id.tvTestStart);
        layTestStart = (LinearLayout) findViewById(R.id.layTestStart);
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
        if(Constants.canpas) {
            layTestStart.setBackgroundColor(getResources().getColor(R.color.colorRed));
            tvTestStart.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        else {
            tvTestStart.setTextColor(getResources().getColor(R.color.colorWhiter));
            layTestStart.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
        layTestStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constants.canpas) {
                    DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                    String date2 = dff.format(Calendar.getInstance().getTime());
                    String date22;
                    String[] dates = date2.split("-");
                    String[] datesNext = new String [3];
                    datesNext[0]="01";

                    if(Integer.parseInt(dates[1])==12) {
                        datesNext[1]="01";
                        datesNext[2] = Integer.parseInt(dates[2])+1+"";
                    }
                    else {
                        datesNext[1] = Integer.parseInt(dates[1])+1+"";
                        String zero="";
                        if(Integer.parseInt(datesNext[1])<10) zero="0";
                        datesNext[1]=zero+datesNext[1];
                        datesNext[2] = dates[2];
                    }
                    date22 = datesNext[0]+"-"+datesNext[1]+"-"+datesNext[2];
                    try {
                        Date  datte = (Date)dff.parse(date2);
                        Date  datte2 = (Date)dff.parse(date22);
                        long  output=datte.getTime()/1000;
                        long  output2=datte2.getTime()/1000;
                        String str=Long.toString(output);
                        String str2=Long.toString(output2);
                        currentTime = Long.parseLong(str);
                        nextFirstDay = Long.parseLong(str2);
                        lastDay = nextFirstDay-currentTime;
                        lastDay = lastDay/86400;

                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dlgInf = new DialogInf();
                    isTime=1;
                    dlgInf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    dlgInf.show(getSupportFragmentManager(), "dlg2");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dlgInf.dismiss();
                        }
                    }, 2000);

                }
                else {
                    dlgInf = new DialogInf();
                    isTime=0;
                    dlgInf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    dlgInf.show(getSupportFragmentManager(), "dlg2");
//                    startActivity(new Intent(Rating.this, TestRating.class));

                }
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
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
    @SuppressLint("ValidFragment")
    public static class DialogInf extends DialogFragment {
        String[]info;
        LinearLayout lay, layCorpusA;
        Button btnCancel, btnOK;
        TextView tvLawCount, tvLawCountText;
        View v;
        public DialogInf() {
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(isTime==0) {
                info = getResources().getStringArray(R.array.priceRating1);
                info2 = getResources().getStringArray(R.array.priceRating2);
                v = inflater.inflate(R.layout.listview, null);
                ListView list = (ListView) v.findViewById(R.id.list1);
                lay = (LinearLayout) v.findViewById(R.id.lay);
                btnCancel = (Button) v.findViewById(R.id.btnCancel);
                layCorpusA = (LinearLayout) v.findViewById(R.id.layCorpusA);
                btnOK = (Button) v.findViewById(R.id.btnOK);
                tvLawCount = (TextView) v.findViewById(R.id.tvLawCount);
                TextView tvText = (TextView) v.findViewById(R.id.tvText);
                TextView tvText2 = (TextView) v.findViewById(R.id.tvText2);
                tvLawCountText = (TextView) v.findViewById(R.id.tvLawCountText);
                lay.setVisibility(View.VISIBLE);
                tvText.setVisibility(View.VISIBLE);
                tvText2.setVisibility(View.VISIBLE);

                tvText.setText(getResources().getString(R.string.priceInfo));
                Lists listAdapter = new Lists(getActivity(), info);
                list.setAdapter(listAdapter);


                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), TestRating.class));
                        dlgInf.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlgInf.dismiss();
                    }
                });
            }
            else {
                v = inflater.inflate(R.layout.textview22, null);
                TextView tvText = (TextView) v.findViewById(R.id.text);
                        if(Constants.kaztestLang)
                    tvText.setText(lastDay+" күн қалды");
                    else  {
                        if(lastDay==1)
                            tvText.setText("Осталось "+lastDay+" день");
                        else if(lastDay==2 || lastDay==3 || lastDay==4)
                            tvText.setText("Осталось "+(lastDay)+" дня");
                        else   tvText.setText("Осталось "+(lastDay)+" дней");
                    }
            }
            return v;
        }

    }
    static class Lists extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        TextView  tvLaw,tvSumma;
        String[] titles;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public Lists(Activity activity, String[] titles) {
            this.activity = activity;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)  {
                    convertView = inflater.inflate(R.layout.alert_price_corpus_b, null);
            }
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            tvLaw = (TextView) convertView.findViewById(R.id.tvLaw);
           // tvSumma = (TextView) convertView.findViewById(R.id.tvSumma);
            tvLaw.setText(titles[position]+" "+info2[position]);



//
            return  convertView;
        }



    }
}