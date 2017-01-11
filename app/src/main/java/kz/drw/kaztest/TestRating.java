package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;

public class TestRating extends AppCompatActivity {
    ImageView imgBtnBack, imgBtnNext;
    ImageView img1, img2, img3, img4;
    TextView tvQuestion, tvVariant1,tvVariant2,tvVariant3,tvVariant4, tvCount, tvFullCount, tvLaw, tvA, tvB, tvC, tvD, tvSecond;
    LinearLayout lay1, lay2, lay3, lay4, layAll, layLaw, lay1var1, lay1var2, lay2var1, lay2var2, lay3var1, lay3var2, lay4var1, lay4var2;
    String selectingTest="";
    static int currentQuestionID=0, countCorrect=0, countWrong=0, QuestionCount=0, currentLastID=0;
    Boolean  isTraining=false, isWrong=false, isRest=false, isStart=false, isBack=false, isLast=false;
    Integer[] lastsQuestionIDs;
    String query="";
    ScrollView scroll;
    int timer=60;
    private  static  int restCount=0, lasts=0;
    public  static String time="";
    ImageView imgBack;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rating);
        mToast = Toast.makeText(TestRating.this, getResources().getString(R.string.isNotLeveled), Toast.LENGTH_SHORT);
        currentQuestionID=0;
        isWrong=false; Constants.isCORPUSB=false;
        Constants.isTest=true;
        Constants.isResult = false;
        Constants.isRating = true;  Constants.isCORPUSA=false;
        Corpus.countCorrect=0;
        initResources();
        StartTesting();
        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCheckBoxes();
                imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                imgBtnBack.setClickable(false);
                img1.setImageDrawable(getResources().getDrawable(R.drawable.check));
                if (currentQuestionID <QuestionCount) {
                    Corpus.myvariants[currentQuestionID]="A";
                    Corpus.answered[currentQuestionID]=true;
                    if ( Corpus.answers[currentQuestionID].equals( Corpus.variant1[currentQuestionID])) {
                        Corpus.myanswers[currentQuestionID]= Corpus.variant1[currentQuestionID];
                        Corpus.countCorrect++;
                    }
                    else {
                        Corpus.myanswers[currentQuestionID]= Corpus.variant1[currentQuestionID];
                        Corpus.wrongs[currentQuestionID] = false;
                    }
                }

            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCheckBoxes();
                imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                imgBtnBack.setClickable(false);
                img2.setImageDrawable(getResources().getDrawable(R.drawable.check));
                if (currentQuestionID <QuestionCount) {
                    Corpus.answered[currentQuestionID]=true;
                    Corpus.myvariants[currentQuestionID]="B";
                    if ( Corpus.answers[currentQuestionID].equals( Corpus.variant2[currentQuestionID])) {
                        Corpus.myanswers[currentQuestionID]= Corpus.variant2[currentQuestionID];
                        Corpus.countCorrect++;
                    }
                    else {
                        Corpus.myanswers[currentQuestionID]= Corpus.variant2[currentQuestionID];
                        Corpus.wrongs[currentQuestionID] = false;
                    }
                }
            }
        });
        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCheckBoxes();
                imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                imgBtnBack.setClickable(false);
                img3.setImageDrawable(getResources().getDrawable(R.drawable.check));
                if (currentQuestionID <QuestionCount) {
                    Corpus.answered[currentQuestionID] = true;
                    Corpus.myvariants[currentQuestionID] = "C";
                    if ( Corpus.answers[currentQuestionID].equals( Corpus.variant3[currentQuestionID])) {
                        Corpus.myanswers[currentQuestionID] =  Corpus.variant3[currentQuestionID];
                        Corpus.countCorrect++;
                    } else {
                        Corpus.myanswers[currentQuestionID] =  Corpus.variant3[currentQuestionID];
                        Corpus.wrongs[currentQuestionID] = false;
                    }
                }
            }
        });
        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCheckBoxes();
                imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                imgBtnBack.setClickable(false);
                img4.setImageDrawable(getResources().getDrawable(R.drawable.check));
                if (currentQuestionID <QuestionCount)
                {
                    Corpus.answered[currentQuestionID] = true;
                    Corpus.myvariants[currentQuestionID] = "D";
                    if ( Corpus.answers[currentQuestionID].equals( Corpus.variant4[currentQuestionID])) {
                        Corpus.myanswers[currentQuestionID] =  Corpus.variant4[currentQuestionID];
                        Corpus.countCorrect++;
                    } else {
                        Corpus. myanswers[currentQuestionID] =  Corpus.variant4[currentQuestionID];
                        Corpus.wrongs[currentQuestionID] = false;
                    }
                }
            }

        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuestionID<QuestionCount-1) {
                    NextQuestion();
                    if(! Corpus.answered[currentQuestionID]) {restCount++;
                    }
                }
                else {
                    if(restCount==0)
                    { startActivity(new Intent(TestRating.this, Result.class).putExtra("right",Corpus.countCorrect+"").putExtra("count",QuestionCount+""));
                        finish();
                        Constants.isTest=false;}
                    else {
                        isLast=true;
                        lasts=0;

                        for(int i=0; i<QuestionCount;i++)
                        {
                            if( Corpus.answered[i].equals(false))
                                lasts++;
                        }
                        lastsQuestionIDs = new Integer[lasts];
                        for (int j = 0; j < lastsQuestionIDs.length; j++){
                            for(int i=0; i<QuestionCount;i++) {
                                if ( Corpus.answered[i].equals(false)) {
                                    lastsQuestionIDs[j] = i;
                                    j++;
                                }
                            }
                        }
                        if(lasts>0) {
                            NextQuestionLast(lastsQuestionIDs[0]);}
                        else   {
                            startActivity(new Intent(TestRating.this, Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                           finish();
                            Constants.isTest=false;
                        }
                    }
                }
            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLast=true;
                lasts=0;

                for(int i=0; i<QuestionCount;i++)
                {
                    if( Corpus.answered[i].equals(false))
                        lasts++;
                }
                lastsQuestionIDs = new Integer[lasts];
                for (int j = 0; j < lastsQuestionIDs.length; j++){
                    for(int i=0; i<QuestionCount;i++) {
                        if ( Corpus.answered[i].equals(false)) {
                            lastsQuestionIDs[j] = i;
                            j++;
                        }
                    }
                }
                if(lasts>0) {
                    NextQuestionLast(lastsQuestionIDs[0]);}

            }
        });
    }

    private void StartTimer() {
        timer=110 ;
        final long minute = (long) (timer*60000);
        final String[] zero = {""},zero2 = {""},zero3 = {""};
        new CountDownTimer(minute, 1000) { // adjust the milli seconds here

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                zero[0]= String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                if(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))<10)
                    zero[0] ="0"+(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                zero2[0] = String.valueOf(TimeUnit.MILLISECONDS.toHours( millisUntilFinished));
                if(TimeUnit.MILLISECONDS.toHours( millisUntilFinished)<10) zero2[0] = "0"+String.valueOf(TimeUnit.MILLISECONDS.toHours( millisUntilFinished));

                zero3[0] = String.valueOf(TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)-(TimeUnit.MILLISECONDS.toHours( millisUntilFinished)*60));
                if( TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)-(TimeUnit.MILLISECONDS.toHours( millisUntilFinished)*60)<10)
                    zero3[0] = "0"+String.valueOf(TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)-(TimeUnit.MILLISECONDS.toHours( millisUntilFinished)*60));
                if(Constants.kaztestLang) {
                    tvSecond.setText(""+String.format("%s сағ %s мин %s ",
                            zero2[0], zero3[0], zero[0])); }
                else{
                    tvSecond.setText(""+String.format("%s час %s мин %s ",
                            zero2[0], zero3[0],zero[0]));
                }

                time =  TimeUnit.MILLISECONDS.toMinutes(  minute - millisUntilFinished)+":"+(TimeUnit.MILLISECONDS.toSeconds(minute - millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(minute - millisUntilFinished)));


                Corpus.time =  TimeUnit.MILLISECONDS.toMinutes(  minute - millisUntilFinished)+":"+(TimeUnit.MILLISECONDS.toSeconds(minute - millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(minute - millisUntilFinished)));
            }

            public void onFinish() {
                if(!Constants.isResult)
                {  startActivity(new Intent(TestRating.this, Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                    Constants.isTest=false;
                }}
        }.start();
    }

    private void isBackYesorNo(){
        lasts=0;
        Boolean isVisible=false;
        for(int i=0; i<QuestionCount;i++)
        {
            if( Corpus.answered[i].equals(false))
                lasts++;
        }
        lastsQuestionIDs = new Integer[lasts];
        if(lastsQuestionIDs!=null) {
            for (int j = 0; j < lastsQuestionIDs.length; j++){
                for(int i=0; i<QuestionCount;i++) {
                    if ( Corpus.answered[i].equals(false)) {
                        lastsQuestionIDs[j] = i;
                        if(i<=currentQuestionID) {
                            if(currentQuestionID>=i) isVisible=true;
                        }

                        j++;

                    }
                }
            }}
        if(lasts>0)  {
            if(isVisible)imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.le));
            imgBtnBack.setClickable(true);
        }
        else { imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
            imgBtnBack.setClickable(false);}
    }
    private void NextQuestionLast(int restQuestID) {
        currentQuestionID=restQuestID;
        isBackYesorNo();
        WriteTest();
    }

    private void NextQuestion() {
        isBackYesorNo();
        if(currentQuestionID<QuestionCount-1) {
            currentQuestionID++;
            if(! Corpus.answered[currentQuestionID])
                WriteTest();
            else {
                if(currentQuestionID!=QuestionCount-1) NextQuestion();
                else {
                    if(restCount==0)
                    {    startActivity(new Intent(TestRating.this, Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                         finish();}
                    else {
                        lasts=0;

                        for(int i=0; i<QuestionCount;i++)
                        {
                            if( Corpus.answered[i].equals(false))
                                lasts++;
                        }
                        lastsQuestionIDs = new Integer[lasts];
                        for (int j = 0; j < lastsQuestionIDs.length; j++){
                            for(int i=0; i<QuestionCount;i++) {
                                if ( Corpus.answered[i].equals(false)) {
                                    lastsQuestionIDs[j] = i;
                                    j++;
                                }
                            }
                        }
                        restCount=lasts;
                        if(lasts>0) {
                            imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.le));
                            imgBtnBack.setClickable(true);
                            NextQuestionLast(lastsQuestionIDs[0]);}
                        else   { startActivity(new Intent(TestRating.this, Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                                  finish();}
                    }
                }
            }
        }
    }

    private void ClearCheckBoxes() {
        img1.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
        img2.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
        img3.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
        img4.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
    }



    private void WriteTest() {
        if(currentQuestionID<QuestionCount) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layAll.setVisibility(View.VISIBLE);
                    mToast.cancel();
                    lay1var1.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    lay1var2.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvVariant1.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvA.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    img1.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));

                    lay2var1.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    lay2var2.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvVariant2.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvB.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    img2.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));

                    lay3var1.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    lay3var2.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvVariant3.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvC.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    img3.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));

                    lay4var1.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    lay4var2.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_whiter));
                    tvVariant4.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvD.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    img4.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));

                    tvCount.setText((currentQuestionID+1)+"");
                    tvQuestion.setText( Corpus.questions[currentQuestionID]);
                    tvVariant1.setText( Corpus.variant1[currentQuestionID]);
                    tvVariant2.setText( Corpus.variant2[currentQuestionID]);
                    tvVariant3.setText( Corpus.variant3[currentQuestionID]);
                    tvVariant4.setText( Corpus.variant4[currentQuestionID]);
                    tvLaw.setText( Corpus.laws[currentQuestionID]);
                    scroll.smoothScrollTo(0,0);
                    if(lasts>0)  {
                        if(currentQuestionID>lastsQuestionIDs[0])
                        {  imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.le));
                            imgBtnBack.setClickable(true);}
                        else {imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                            imgBtnBack.setClickable(false);}
                    }

                }
            }, 100);

        }
    }
    private void WriteTestStart() {
        if(currentQuestionID<QuestionCount) {
            StartTimer();
            layAll.setVisibility(View.VISIBLE);
            mToast.cancel();
            lay1.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            lay2.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            lay3.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            lay4.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            tvCount.setText((currentQuestionID+1)+"");
            tvQuestion.setText( Corpus.questions[currentQuestionID]);
            tvLaw.setText( Corpus.laws[currentQuestionID]);
            tvVariant1.setText( Corpus.variant1[currentQuestionID]);
            tvVariant2.setText( Corpus.variant2[currentQuestionID]);
            tvVariant3.setText( Corpus.variant3[currentQuestionID]);
            tvVariant4.setText( Corpus.variant4[currentQuestionID]);
        }
    }

    private void StartTesting() {
        Constants.Show_ProgressDialog(TestRating.this,getResources().getString(R.string.wait));
        query = Constants.CORPUSB_PROGRAMMA1+"language="+Constants.kaztestLang;
        JsonArrayRequest req = new JsonArrayRequest(query,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()>0) {
                            Corpus.questions = new String[response.length()];
                            Corpus.variant1 = new String[response.length()];
                            Corpus.variant2 = new String[response.length()];
                            Corpus.variant3 = new String[response.length()];
                            Corpus.variant4 = new String[response.length()];
                            Corpus.answers = new String[response.length()];
                            Corpus.myanswers = new String[response.length()];
                            Corpus.myvariants = new String[response.length()];
                            Corpus.variants = new String[response.length()];
                            Corpus.laws = new String[response.length()];
                            Corpus.wrongs = new Boolean[response.length()];
                            Corpus. answered = new Boolean[response.length()];
                            Arrays.fill( Corpus.wrongs, true);
                            Arrays.fill( Corpus.answered, false);
                            Arrays.fill( Corpus.myvariants, "-");
                            tvFullCount.setText(response.length()+"");
                            QuestionCount = response.length();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject js = (JSONObject) response.get(i);
                                    Corpus.questions[i] = js.getString("question");
                                    Corpus.variant1[i] = js.getString("answer1");
                                    Corpus.variant2[i] = js.getString("answer2");
                                    Corpus.variant3[i] = js.getString("answer3");
                                    Corpus.variant4[i] = js.getString("answer4");
                                    Corpus.answers[i] = js.getString("right");
                                    Corpus.laws[i] = js.getString("law");
                                    if( Corpus.answers[i].equals( Corpus.variant1[i]))  Corpus.variants[i]="A";
                                    if( Corpus.answers[i].equals( Corpus.variant2[i]))  Corpus.variants[i]="B";
                                    if( Corpus.answers[i].equals( Corpus.variant3[i]))  Corpus.variants[i]="C";
                                    if( Corpus.answers[i].equals( Corpus.variant4[i]))  Corpus.variants[i]="D";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Set<String> set = new HashSet<String>();
                            Set<Integer> setID = new HashSet<Integer>();
                            Boolean[] bool =  new Boolean[Corpus.laws.length];
                            for (int i = 0; i < Corpus.laws.length; i++) {
                                bool[i]=contains(set,Corpus.laws[i]);
                                set.add(Corpus.laws[i]);
                                setID.add(i);

                            }
                            Corpus.countOfLaws = set.size();
                            Corpus.mylaws = new String[Corpus.countOfLaws];

                            for(int h =0; h<Corpus.mylaws.length; h++)
                            {
                                Corpus.mylaws[h] = Corpus.laws[h*15];
                            }

                            Corpus.elementsOfLaws = new Integer[Corpus.countOfLaws];
                            for(int j=0; j<Corpus.elementsOfLaws.length;j++)
                            {    for(int i = 0; i<bool.length; i++){
                                if(!bool[i])
                                {  Corpus.elementsOfLaws[j] = i;
                                    j++;
                                }
                            }
                            }
                            Constants.Hide_ProgressDialog();
                            WriteTestStart();
                        }
                        else{
                            Constants.Hide_ProgressDialog();
                            Toast.makeText(TestRating.this.getApplicationContext(), getResources().getString(R.string.NoData), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TestRating.this.getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            finish();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(TestRating.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    boolean contains(Set<String> s, String item) {
        for(String toCompare: s) {
            if(toCompare.equals(item)) {
                return true;
            }
        }
        return false;
    }

    private void initResources() {
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvVariant1 = (TextView) findViewById(R.id.tvVariant1);
        tvVariant2 = (TextView) findViewById(R.id.tvVariant2);
        tvVariant3 = (TextView) findViewById(R.id.tvVariant3);
        tvVariant4 = (TextView) findViewById(R.id.tvVariant4);
        scroll = (ScrollView) findViewById(R.id.scroll);
        tvLaw = (TextView) findViewById(R.id.tvLaw);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvFullCount = (TextView) findViewById(R.id.tvFullCount);
        lay1 = (LinearLayout) findViewById(R.id.lay1);
        lay2 = (LinearLayout) findViewById(R.id.lay2);
        lay3 = (LinearLayout) findViewById(R.id.lay3);
        lay4 = (LinearLayout) findViewById(R.id.lay4);
        layAll = (LinearLayout) findViewById(R.id.layAll);
        layLaw = (LinearLayout) findViewById(R.id.layLaw);
        lay1var1 = (LinearLayout) findViewById(R.id.lay1var1);
        lay1var2 = (LinearLayout) findViewById(R.id.lay1var2);
        lay2var1 = (LinearLayout) findViewById(R.id.lay2var1);
        lay2var2 = (LinearLayout) findViewById(R.id.lay2var2);
        lay3var1 = (LinearLayout) findViewById(R.id.lay3var1);
        lay3var2 = (LinearLayout) findViewById(R.id.lay3var2);
        lay4var1 = (LinearLayout) findViewById(R.id.lay4var1);
        lay4var2 = (LinearLayout) findViewById(R.id.lay4var2);
        tvA = (TextView) findViewById(R.id.A);
        tvB = (TextView) findViewById(R.id.B);
        tvB = (TextView) findViewById(R.id.B);
        tvC = (TextView) findViewById(R.id.C);
        tvD = (TextView) findViewById(R.id.D);
        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
        imgBtnBack.setClickable(false);
        imgBtnNext = (ImageView) findViewById(R.id.imgBtnNext);
        imgBack = (ImageView) findViewById(R.id.back);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

    }
}
