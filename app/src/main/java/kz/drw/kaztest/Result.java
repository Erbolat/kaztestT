package kz.drw.kaztest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.widget.HListView;
import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;

public class Result extends AppCompatActivity {

    LinearLayout layAnswer,layTable1,layTable2,layTable3, layLaw;
    TextView tvTestName, tvSumma, tvCorpusB, tvFullCount, tvStartTime, tvFinishTime, tvLang,tvNameLaw;
    Button btnExit;
    String[] tvIDs, Corrects, MyCorrects;
    ImageButton img;
    Spinner spinLaw;
    int kvadrat=0;
    static int oldPosition=0;
    String duration="", dateFinish="";
    int width=0,columsCount=0,origColumsCount=0, last=0, row=0, wrongs=0;
    String[] myLaws2;
    public static int thisPos=0;
    public  static  String[] myIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        myLaws2 = new String[Corpus.mylaws.length];
        for(int i=0; i<Corpus.mylaws.length; i++) {
            myLaws2[i] = (i+1)+". "+Corpus.mylaws[i];
        }
        thisPos=0;
        Constants.isResult=true;
        initTime();
        initResources();

        Corrects =Corpus.variants;
        MyCorrects = Corpus.myvariants;
        tvFullCount.setText(getIntent().getStringExtra("count")+" "+getResources().getString(R.string.suraktan));
        tvNameLaw.setText(myLaws2[0]);
        if(Constants.kaztestLang) tvLang.append(" қазақ тілі");
        else tvLang.append(" русский язык");

        initTable(-1,thisPos);
        layLaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialog();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constants.isRating)
                setResultat();
                else setResultatRating();
                startActivity(new Intent(Result.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constants.isRating)
                setResultat();
                else setResultatRating();
                startActivity(new Intent(Result.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });

        tvStartTime.append(dateFinish);
        tvFinishTime.append(duration);

        tvSumma.setText(Corpus.countCorrect+"");
        if(Constants.isCORPUSB){
            tvCorpusB.setVisibility(View.GONE);

            if(Corpus.countCorrect==0) {
                tvSumma.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            else {
            if(Corpus.countCorrect<Programms.minBall)
                tvSumma.setTextColor(getResources().getColor(R.color.colorAccent));
            else   tvSumma.setTextColor(getResources().getColor(R.color.colorGreen));
            }
        }
    }

    private void setAlertDialog() {
        final Dialog dialog = new Dialog(Result.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listvieww);
//        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//        layoutParams.x = 20; // right margin
//        layoutParams.y = 30; // top margin
//        dialog.getWindow().setAttributes(layoutParams);
//        // e.g. bottom + left margins:
//        dialog.getWindow().setGravity(Gravity.BOTTOM|Gravity.LEFT);
//        WindowManager.LayoutParams layoutParams2 = dialog.getWindow().getAttributes();
//        layoutParams2.x = 20; // left margin
//        layoutParams2.y = 30; // bottom margin
//        dialog.getWindow().setAttributes(layoutParams2);
        ListView list = (ListView) dialog.findViewById(R.id.list1);

        Lists lists = new Lists(Result.this, myLaws2);
        list.setAdapter(lists);
        dialog.show();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvNameLaw.setText(Corpus.mylaws[position]);
                initTable(oldPosition,position);
                thisPos = position;
                dialog.cancel();
                dialog.dismiss();

            }
        });



    }

    private void initTime() {
        DateFormat dff = new SimpleDateFormat("dd.MM.yyyy");
        dateFinish = dff.format(Calendar.getInstance().getTime());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); //
        width = size.x;
        String zero="", zero2="";
        String[] times = Corpus.time.split(":");
        if(Integer.parseInt(times[0])<10) zero="0";
        if(Integer.parseInt(times[1])<10) zero2="0";
        duration = zero+times[0]+":"+zero2+times[1];
    }

    private void initTable(int remView , int position) {
        oldPosition=position;
        if(remView != -1)
            layTable1.removeAllViews();
        oldPosition = position;


        if(!Constants.isCORPUSA) {
            if(width<850) kvadrat = 45;
            else  if(width>=850 && width <1120) kvadrat = 95;
            else kvadrat = 110;
            columsCount  = (width-32)/(kvadrat+3);
            origColumsCount  = (width-32)/(kvadrat+3);
        if(position!=Corpus.elementsOfLaws.length-1) {
            row =  (Corpus.elementsOfLaws[position+1]-Corpus.elementsOfLaws[position])/columsCount;    // kanwa katar
            last = (Corpus.elementsOfLaws[position+1]-Corpus.elementsOfLaws[position])%columsCount;  // en songy katardagy elementter sany
        }
        else {
            row =  (Integer.parseInt(getIntent().getStringExtra("count"))-Corpus.elementsOfLaws[position])/columsCount;    // kanwa katar
            last = (Integer.parseInt(getIntent().getStringExtra("count"))-Corpus.elementsOfLaws[position])%columsCount;  // en songy katardagy elementter sany
        }
        if(last>0) row=row+1;            //en songy katardagy elementter sany > 0  onda row = row + 1;

        for(int i=0; i<row;i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout1.setLayoutParams(layoutParams);
            linearLayout1.setPadding(0,30,0,0);
            TextView[] tvID=new TextView[columsCount];
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(kvadrat, kvadrat);
            if(last>0)
                if(i==row-1)  {
                    columsCount = last;
                    tvID = new TextView[columsCount];}   //Songy katar olwemi
                else  tvID = new TextView[columsCount];
            for (int l = 0; l < columsCount; l++) {
                tvID[l] = new TextView(this);
              //  tvID[l].setTag((l+1)+(i*columsCount));
                tvID[l].setTag(i*origColumsCount+Corpus.elementsOfLaws[position]+l);
                tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                tvID[l].setLayoutParams(lp);
                tvID[l].setGravity(Gravity.CENTER);
                tvID[l].setId(l);
                tvID[l].setText(i*origColumsCount+Corpus.elementsOfLaws[position]+l+1+ "");

                if(!MyCorrects[i*origColumsCount+Corpus.elementsOfLaws[position]+l].equals(Corrects[i*origColumsCount+Corpus.elementsOfLaws[position]+l])){
                    tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_red_test));
                    tvID[l].setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else{
                    tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_green_test));
                    tvID[l].setTextColor(getResources().getColor(R.color.colorWhite));
                }
                linearLayout1.addView(tvID[l]);
                tvID[l].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetAnswer(Integer.parseInt(String.valueOf(view.getTag())));
                    }
                });
            }
            layTable1.addView(linearLayout1);

            LinearLayout linearLayout3 = new LinearLayout(this);
            layoutParams.setMargins(0, 0, 0, 0);
            linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout3.setLayoutParams(layoutParams);

            TextView[] tvMyCorrect=new TextView[columsCount];
            LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(kvadrat, kvadrat);
            if(last>0)
                if(i==row-1)  {
                    columsCount = last;
                    tvMyCorrect = new TextView[columsCount];}
                else  tvMyCorrect = new TextView[columsCount];
            for (int l = 0; l < columsCount; l++) {
                tvMyCorrect[l] = new TextView(this);
                tvMyCorrect[l].setTag(i*origColumsCount+Corpus.elementsOfLaws[position]+l);
                tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                tvMyCorrect[l].setLayoutParams(lp3);
                tvMyCorrect[l].setId(l);
                tvMyCorrect[l].setGravity(Gravity.CENTER);
                tvMyCorrect[l].setText(MyCorrects[i*origColumsCount+Corpus.elementsOfLaws[position]+l]);
                if(!MyCorrects[i*origColumsCount+Corpus.elementsOfLaws[position]+l].equals(Corrects[i*origColumsCount+Corpus.elementsOfLaws[position]+l])){
                    tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_red_test));
                    tvMyCorrect[l].setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else{
                    tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_green_test));
                    tvMyCorrect[l].setTextColor(getResources().getColor(R.color.colorWhite));
                }
                final int finalL = l;
                tvMyCorrect[l].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetAnswer(Integer.parseInt(String.valueOf(view.getTag())));
                    }
                });
                linearLayout3.addView(tvMyCorrect[l]);
            }

            layTable1.addView(linearLayout3);}
    }
        else {
            if(width<850) kvadrat = 65;
            else  if(width>=850 && width <1120) kvadrat = 115;
            else kvadrat = 130;
            columsCount  = (width-32)/(kvadrat+3);
            origColumsCount  = (width-32)/(kvadrat+3);
            myIndex = Corpus.myIndexes[position].split(",");
            row =  Corpus.myLawsCount[position]/columsCount;    // kanwa katar
            last = Corpus.myLawsCount[position]%columsCount;  // en songy katardagy elementter sany
            if(last>0) row=row+1;

            for(int i=0; i<row;i++) {
                LinearLayout linearLayout1 = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout1.setLayoutParams(layoutParams);
                linearLayout1.setGravity(Gravity.CENTER);
                linearLayout1.setPadding(0,30,0,0);
                TextView[] tvID=new TextView[columsCount];
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(kvadrat, kvadrat);

                if(last>0)
                    if(i==row-1)  {
                        columsCount = last;
                        tvID = new TextView[columsCount];}   //Songy katar olwemi
                    else  tvID = new TextView[columsCount];
                for (int l = 0; l < columsCount; l++) {
                    tvID[l] = new TextView(this);
                    tvID[l].setTag(myIndex[i*origColumsCount+l]);
                    tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvID[l].setLayoutParams(lp);
                    tvID[l].setGravity(Gravity.CENTER);
                    tvID[l].setId(l);
                    tvID[l].setText(1+Integer.parseInt(myIndex[i*origColumsCount+l])+ "");

                    if(!MyCorrects[Integer.parseInt(myIndex[i*origColumsCount+l])].equals(Corrects[Integer.parseInt(myIndex[i*origColumsCount+l])])){
                        tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_red_test));
                        tvID[l].setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    else{
                        tvID[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_green_test));
                        tvID[l].setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    linearLayout1.addView(tvID[l]);
                    tvID[l].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetAnswer(Integer.parseInt(String.valueOf(view.getTag())));
                        }
                    });
                }
                layTable1.setGravity(Gravity.CENTER);
                layTable1.addView(linearLayout1);

                LinearLayout linearLayout3 = new LinearLayout(this);
                layoutParams.setMargins(0, 0, 0, 0);
                linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout3.setLayoutParams(layoutParams);
                linearLayout3.setGravity(Gravity.CENTER);
                TextView[] tvMyCorrect=new TextView[columsCount];
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(kvadrat, kvadrat);
                if(last>0)
                    if(i==row-1)  {
                        columsCount = last;
                        tvMyCorrect = new TextView[columsCount];}
                    else  tvMyCorrect = new TextView[columsCount];
                for (int l = 0; l < columsCount; l++) {
                    tvMyCorrect[l] = new TextView(this);
                    tvMyCorrect[l].setTag(myIndex[i*origColumsCount+l]);
                    tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvMyCorrect[l].setLayoutParams(lp3);
                    tvMyCorrect[l].setId(l);
                    tvMyCorrect[l].setGravity(Gravity.CENTER);
                    tvMyCorrect[l].setText(MyCorrects[Integer.parseInt(myIndex[i*origColumsCount+l])]);
                    if(!MyCorrects[Integer.parseInt(myIndex[i*origColumsCount+l])].equals(Corrects[Integer.parseInt(myIndex[i*origColumsCount+l])])){
                        tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_red_test));
                        tvMyCorrect[l].setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    else{
                        tvMyCorrect[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.border_green_test));
                        tvMyCorrect[l].setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    final int finalL = l;
                    tvMyCorrect[l].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetAnswer(Integer.parseInt(String.valueOf(view.getTag())));
                        }
                    });
                    linearLayout3.addView(tvMyCorrect[l]);
                }
                layTable1.setGravity(Gravity.CENTER);
                layTable1.addView(linearLayout3);
            }
        }
    }

    private void setResultatRating() {
        if(Constants.canpas) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SET_RESULT_RATING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("aaa1111",response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Constants.Hide_ProgressDialog();
                            Toast.makeText(Result.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    //params.put("Content-Type", "application/json");
                    return params;
                }
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("userid",MainActivity.userID+"");
                    params.put("ratingid","1");
                    params.put("date",dateFinish+" 0:00:00");
                    params.put("time",duration+":00");
                    params.put("col",getIntent().getStringExtra("count"));
                    params.put("rightcol",Corpus.countCorrect+"");
                    params.put("wrongcol",Integer.parseInt(getIntent().getStringExtra("count"))-Corpus.countCorrect+"");
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
  }
  }

    private void setResultat() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SET_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(Result.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userid",MainActivity.userID+"");
                params.put("korpus",Constants.Corpus+"");
                params.put("programma",Constants.Program+"");
                params.put("date",dateFinish);
                params.put("time",duration);
                params.put("col",getIntent().getStringExtra("count"));
                params.put("rightcol",Corpus.countCorrect+"");
                params.put("wrongcol",Integer.parseInt(getIntent().getStringExtra("count"))-Corpus.countCorrect+"");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GetAnswer(int questionID) {
        Log.e("questyion", questionID+"");

        final Dialog dialog = new Dialog(Result.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout lay1var1 = (LinearLayout) dialog.findViewById(R.id.lay1var1);
        LinearLayout lay1var2 = (LinearLayout) dialog.findViewById(R.id.lay1var2);
        LinearLayout lay2var1 = (LinearLayout) dialog.findViewById(R.id.lay2var1);
        LinearLayout lay2var2 = (LinearLayout) dialog.findViewById(R.id.lay2var2);
        LinearLayout lay3var1 = (LinearLayout) dialog.findViewById(R.id.lay3var1);
        LinearLayout  lay3var2 = (LinearLayout) dialog.findViewById(R.id.lay3var2);
        LinearLayout  lay4var1 = (LinearLayout) dialog.findViewById(R.id.lay4var1);
        LinearLayout  lay4var2 = (LinearLayout) dialog.findViewById(R.id.lay4var2);
        TextView tvQuestion = (TextView) dialog.findViewById(R.id.tvQuestion);
        TextView tvVariant1 = (TextView) dialog.findViewById(R.id.tvVariant1);
        TextView tvVariant2 = (TextView) dialog.findViewById(R.id.tvVariant2);
        TextView  tvVariant3 = (TextView) dialog.findViewById(R.id.tvVariant3);
        TextView  tvVariant4 = (TextView) dialog.findViewById(R.id.tvVariant4);
        TextView  tvA = (TextView) dialog.findViewById(R.id.A);
        TextView  tvB = (TextView) dialog.findViewById(R.id.B);
        TextView tvC = (TextView) dialog.findViewById(R.id.C);
        TextView tvD = (TextView) dialog.findViewById(R.id.D);
        ImageView img1 = (ImageView) dialog.findViewById(R.id.img1);
        ImageView img2 = (ImageView) dialog.findViewById(R.id.img2);
        ImageView img3 = (ImageView) dialog.findViewById(R.id.img3);
        ImageView img4 = (ImageView) dialog.findViewById(R.id.img4);
        ImageView imgEXIT = (ImageView) dialog.findViewById(R.id.imgEXIT);
        imgEXIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        tvVariant1.setText(Corpus.variant1[questionID]);
        tvVariant2.setText(Corpus.variant2[questionID]);
        tvVariant3.setText(Corpus.variant3[questionID]);
        tvVariant4.setText(Corpus.variant4[questionID]);
        if(!Constants.isCORPUSA)
        tvQuestion.setText((questionID+1)+". "+Corpus.questions[questionID]);
        else   tvQuestion.setText((questionID+1)+". "+Corpus.questions[questionID]);


            if(Corrects[questionID].equals("A")) {
        lay1var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lay1var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        img1.setImageDrawable(getResources().getDrawable(R.drawable.check));
        tvVariant1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvA.setTextColor(getResources().getColor(R.color.colorWhite));}
            else if(Corrects[questionID].equals("B")) {
        lay2var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lay2var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        img2.setImageDrawable(getResources().getDrawable(R.drawable.check));
        tvVariant2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvB.setTextColor(getResources().getColor(R.color.colorWhite));}
            else if(Corrects[questionID].equals("C")) {
        lay3var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lay3var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        img3.setImageDrawable(getResources().getDrawable(R.drawable.check));
        tvVariant3.setTextColor(getResources().getColor(R.color.colorWhite));
        tvC.setTextColor(getResources().getColor(R.color.colorWhite));}
            else {
        lay4var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        lay4var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        img4.setImageDrawable(getResources().getDrawable(R.drawable.check));
        tvVariant4.setTextColor(getResources().getColor(R.color.colorWhite));
        tvD.setTextColor(getResources().getColor(R.color.colorWhite));}

        if(!MyCorrects[questionID].equals(Corrects[questionID])) {
            if(MyCorrects[questionID].equals("A")) {
            lay1var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lay1var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvVariant1.setTextColor(getResources().getColor(R.color.colorWhite));
            tvA.setTextColor(getResources().getColor(R.color.colorWhite)); }
            else if(MyCorrects[questionID].equals("B")) {
            lay2var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lay2var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvVariant2.setTextColor(getResources().getColor(R.color.colorWhite));
            tvB.setTextColor(getResources().getColor(R.color.colorWhite)); }
            else if(MyCorrects[questionID].equals("C")) {
            lay3var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lay3var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvVariant3.setTextColor(getResources().getColor(R.color.colorWhite));
            tvC.setTextColor(getResources().getColor(R.color.colorWhite)); }
            else if(MyCorrects[questionID].equals("D")) {
            lay4var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lay4var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            tvVariant4.setTextColor(getResources().getColor(R.color.colorWhite));
            tvD.setTextColor(getResources().getColor(R.color.colorWhite)); }
        }
        dialog.show();

    }


    @Override
    public void onBackPressed() {
        if(!Constants.isRating)
        setResultat();
        else setResultatRating();
        startActivity(new Intent(Result.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }

    static class Lists extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        TextView  tvText,tvSumma;
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
            if (convertView == null)
                convertView = inflater.inflate(R.layout.textview3, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            tvText = (TextView) convertView.findViewById(R.id.text);
            tvText.setText(titles[position]);
            if(position==thisPos)
                tvText.setTextColor(activity.getResources().getColor(R.color.colorGreen));
            else
            tvText.setTextColor(activity.getResources().getColor(R.color.colorBlack));

            return  convertView;
        }



    }
    private void initResources() {
        this.tvTestName= (TextView) findViewById(R.id.tvTestName);
        this.tvSumma= (TextView) findViewById(R.id.tvSumma);
        this.tvCorpusB= (TextView) findViewById(R.id.tvCorpusB);
        this.tvFullCount= (TextView) findViewById(R.id.tvFullCount);
        this.tvStartTime= (TextView) findViewById(R.id.tvStartTime);
        this.tvFinishTime= (TextView) findViewById(R.id.tvFinishTime);
        this.tvNameLaw= (TextView) findViewById(R.id.tvNameLaw);
        this.tvLang= (TextView) findViewById(R.id.tvLang);
        this.layAnswer= (LinearLayout) findViewById(R.id.layAnswer);
        this.layTable1= (LinearLayout) findViewById(R.id.layTable1);
        this.btnExit= (Button) findViewById(R.id.btnExit);
        this.img= (ImageButton) findViewById(R.id.back);
        this.layLaw= (LinearLayout) findViewById(R.id.layLaw);
    }

    public  void  onExit(View v){
        startActivity(new Intent(Result.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }
}
