package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyRequest;


public class Corpus extends Fragment {

    View view;
    ImageView imgBtnBack, imgBtnNext;
    public  static  String [] questions, variant1, variant2, variant3, variant4, answers, myanswers, myvariants, laws, variants, mylaws, myIndexes;
    public static  Boolean[] wrongs, answered;
    ImageView img1, img2, img3, img4;
    TextView tvQuestion, tvVariant1,tvVariant2,tvVariant3,tvVariant4, tvCount, tvFullCount, tvLaw, tvA, tvB, tvC, tvD, tvSecond;
    LinearLayout lay1, lay2, lay3, lay4, layAll, layLaw, lay1var1, lay1var2, lay2var1, lay2var2, lay3var1, lay3var2, lay4var1, lay4var2;
    String selectingTest="";
    static int currentQuestionID=0, countWrong=0, QuestionCount=0, currentLastID=0;
    Boolean  isTraining=false, isWrong=false, isRest=false, isStart=false, isBack=false, isLast=false;
    public  static Integer[] elementsOfLaws;
    Integer[] lastsQuestionIDs;
    public static int countOfLaws,countCorrect=0;
    ScrollView scroll;
    float timer=60;
    private  static  int restCount=0;
    private  static int lasts=0;
    public  static int[] myLawsCount;
    public  static String time="";

    Toast mToast;

    public Corpus() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.testing, container, false);
        currentQuestionID=0;
        Constants.isTest=true;
        Constants.isResult = false;
        Constants.isRating = false; Constants.isCORPUSB=false;
        initResources();
        layAll.setVisibility(View.GONE);
        isTraining=false; isWrong=false;
        countCorrect=0;
        mToast = Toast.makeText(getActivity(), getResources().getString(R.string.isNotLeveled), Toast.LENGTH_SHORT);
        Bundle bundle = getArguments();
        if(bundle!=null)
        selectingTest = getArguments().getString("select");
                    StartTesting();


        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ClearCheckBoxes();
                imgBtnBack.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                imgBtnBack.setClickable(false);
            img1.setImageDrawable(getResources().getDrawable(R.drawable.check));
            if (currentQuestionID <QuestionCount) {
                    myvariants[currentQuestionID]="A";
                    answered[currentQuestionID]=true;
                    if (answers[currentQuestionID].equals(variant1[currentQuestionID])) {
                        myanswers[currentQuestionID]=variant1[currentQuestionID];
                        countCorrect++;
                    }
                    else {
                        myanswers[currentQuestionID]=variant1[currentQuestionID];
                        wrongs[currentQuestionID] = false;
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
                    answered[currentQuestionID]=true;
                    myvariants[currentQuestionID]="B";
                    if (answers[currentQuestionID].equals(variant2[currentQuestionID])) {
                        myanswers[currentQuestionID]=variant2[currentQuestionID];
                        countCorrect++;
                       }
                    else {
                        myanswers[currentQuestionID]=variant2[currentQuestionID];
                        wrongs[currentQuestionID] = false;
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
                    answered[currentQuestionID] = true;
                    myvariants[currentQuestionID] = "C";
                    if (answers[currentQuestionID].equals(variant3[currentQuestionID])) {
                        myanswers[currentQuestionID] = variant3[currentQuestionID];
                        countCorrect++;
                    } else {
                        myanswers[currentQuestionID] = variant3[currentQuestionID];
                        wrongs[currentQuestionID] = false;
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
                        answered[currentQuestionID] = true;
                        myvariants[currentQuestionID] = "D";
                        if (answers[currentQuestionID].equals(variant4[currentQuestionID])) {
                        myanswers[currentQuestionID] = variant4[currentQuestionID];
                        countCorrect++;
                        } else {
                        myanswers[currentQuestionID] = variant4[currentQuestionID];
                        wrongs[currentQuestionID] = false;
                         }
                    }
            }

        });

            imgBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentQuestionID<QuestionCount-1) {
                        NextQuestion();
                    if(!answered[currentQuestionID]) {restCount++;
                        }
                }
                    else {
                        if(restCount==0)
                        { getActivity().startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                        getActivity().finish();
                        Constants.isTest=false;}
                        else {
                            isLast=true;
                            lasts=0;

                            for(int i=0; i<QuestionCount;i++)
                            {
                                if(answered[i].equals(false))
                                    lasts++;
                            }
                            lastsQuestionIDs = new Integer[lasts];
                            for (int j = 0; j < lastsQuestionIDs.length; j++){
                                for(int i=0; i<QuestionCount;i++) {
                                    if (answered[i].equals(false)) {
                                        lastsQuestionIDs[j] = i;
                                        j++;
                                    }
                                }
                            }
                            if(lasts>0) {
                            NextQuestionLast(lastsQuestionIDs[0]);}
                            else   { getActivity().startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                                getActivity().finish();
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
                            if(answered[i].equals(false))
                                lasts++;
                        }
                        lastsQuestionIDs = new Integer[lasts];
                        for (int j = 0; j < lastsQuestionIDs.length; j++){
                            for(int i=0; i<QuestionCount;i++) {
                                if (answered[i].equals(false)) {
                                    lastsQuestionIDs[j] = i;
                                    j++;
                                }
                            }
                        }
                        if(lasts>0) {
                            NextQuestionLast(lastsQuestionIDs[0]);}

                }
            });
        return  view;
    }

    private void StartTimer() {
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
            }

            public void onFinish() {
                if(Constants.isTest){
                if(!Constants.isResult)
                {  getActivity().startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                    Constants.isTest=false;
                }}}
        }.start();
    }

    private void isBackYesorNo(){
        lasts=0;
        Boolean isVisible=false;
        for(int i=0; i<QuestionCount;i++)
        {
            if(answered[i].equals(false))
                lasts++;
        }
        lastsQuestionIDs = new Integer[lasts];
        if(lastsQuestionIDs!=null) {
        for (int j = 0; j < lastsQuestionIDs.length; j++){
            for(int i=0; i<QuestionCount;i++) {
                if (answered[i].equals(false)) {
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
            if(!answered[currentQuestionID])
            WriteTest();
            else {
                if(currentQuestionID!=QuestionCount-1) NextQuestion();
                else {
                    if(restCount==0)
                    {    getActivity().startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                        getActivity().finish();}
                    else {
                        lasts=0;

                        for(int i=0; i<QuestionCount;i++)
                        {
                            if(answered[i].equals(false))
                                lasts++;
                        }
                        lastsQuestionIDs = new Integer[lasts];
                        for (int j = 0; j < lastsQuestionIDs.length; j++){
                            for(int i=0; i<QuestionCount;i++) {
                                if (answered[i].equals(false)) {
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
                        else   { getActivity().startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
                            getActivity().finish();}
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
                    tvQuestion.setText(questions[currentQuestionID]);
                    tvVariant1.setText(variant1[currentQuestionID]);
                    tvVariant2.setText(variant2[currentQuestionID]);
                    tvVariant3.setText(variant3[currentQuestionID]);
                    tvVariant4.setText(variant4[currentQuestionID]);
                    tvLaw.setText(laws[currentQuestionID]);
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
                    tvQuestion.setText(questions[currentQuestionID]);
                    tvLaw.setText(laws[currentQuestionID]);
                    tvVariant1.setText(variant1[currentQuestionID]);
                    tvVariant2.setText(variant2[currentQuestionID]);
                    tvVariant3.setText(variant3[currentQuestionID]);
                    tvVariant4.setText(variant4[currentQuestionID]);
                }
    }

    private void StartTesting() {
        String query="";
        Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));

        if(selectingTest.equals("1pr1"))  {query = Constants.CORPUSA_PROGRAMMA1+"language=false"; Constants.isCORPUSB=false; Constants.isCORPUSA=true;  }
        else if(selectingTest.equals("1pr2"))  { query = Constants.CORPUSA_PROGRAMMA2+"zakon="+getArguments().getString("zakon")+"&language="+Constants.kaztestLang; Constants.isCORPUSA=true;  Constants.isCORPUSB=false;} //APR2
        else if(selectingTest.equals("2pr1"))  {query = Constants.CORPUSB_PROGRAMMA1+"language="+Constants.kaztestLang;  timer = 110; Constants.isCORPUSB=true; Constants.isCORPUSA=false; }
        else if(selectingTest.equals("2pr2"))  {query = Constants.CORPUSB_PROGRAMMA2+"language="+Constants.kaztestLang+"&dd=1"; timer = 100; Constants.isCORPUSB=true; Constants.isCORPUSA=false; }
        else if(selectingTest.equals("2pr3")) { query = Constants.CORPUSB_PROGRAMMA3+"language="+Constants.kaztestLang+"&d=true"; timer = 80;Constants.isCORPUSB=true;  Constants.isCORPUSA=false; }
        else { isTraining=true;
            Constants.isCORPUSB=false;
            layLaw.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getResources().getString(R.string.alertTraining), Toast.LENGTH_LONG).show();
            if(!selectingTest.equals(""))
                query = Constants.TREN_SELECT_LAW+"page=1&zakonid=5&userid="+MainActivity.userID;   //SELECT TEST
            else Toast.makeText(getActivity(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();}

        Log.d("query!!",query);
        if(Constants.isCORPUSB) {
        JsonArrayRequest req = new JsonArrayRequest(query,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()>0) {
                            questions = new String[response.length()];
                            variant1 = new String[response.length()];
                            variant2 = new String[response.length()];
                            variant3 = new String[response.length()];
                            variant4 = new String[response.length()];
                            answers = new String[response.length()];
                            myanswers = new String[response.length()];
                            myvariants = new String[response.length()];
                            variants = new String[response.length()];
                            laws = new String[response.length()];

                            wrongs = new Boolean[response.length()];
                            answered = new Boolean[response.length()];
                            if(selectingTest.equals("1pr1") ||selectingTest.equals("1pr2")) timer = (float) (0.75*response.length());
                            Arrays.fill(wrongs, true);
                            Arrays.fill(answered, false);
                            Arrays.fill(myvariants, "-");
                            tvFullCount.setText(response.length()+"");
                            QuestionCount = response.length();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject js = (JSONObject) response.get(i);
                                    questions[i] = js.getString("question");
                                    variant1[i] = js.getString("answer1");
                                    variant2[i] = js.getString("answer2");
                                    variant3[i] = js.getString("answer3");
                                    variant4[i] = js.getString("answer4");
                                    answers[i] = js.getString("right");
                                    laws[i] = js.getString("law");
                                    if(answers[i].equals(variant1[i])) variants[i]="A";
                                    if(answers[i].equals(variant2[i])) variants[i]="B";
                                    if(answers[i].equals(variant3[i])) variants[i]="C";
                                    if(answers[i].equals(variant4[i])) variants[i]="D";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Set<String> set = new HashSet<String>();
                            Set<Integer> setID = new HashSet<Integer>();
                            Boolean[] bool =  new Boolean[laws.length];
                            for (int i = 0; i < laws.length; i++) {
                                bool[i]=contains(set,laws[i]);
                                set.add(laws[i]);
                                setID.add(i);

                            }
                            countOfLaws = set.size();
                            mylaws = new String[countOfLaws];

                           for(int h =0; h<mylaws.length; h++)
                           {
                               mylaws[h] = laws[h*15];
                           }

                            elementsOfLaws = new Integer[countOfLaws];
                            for(int j=0; j<elementsOfLaws.length;j++)
                                {    for(int i = 0; i<bool.length; i++){
                                    if(!bool[i])
                                    {  elementsOfLaws[j] = i;
                                        j++;
                                   }
                                }
                            }
                            Constants.Hide_ProgressDialog();
                            WriteTestStart();
                        }
                        else{
                            Constants.Hide_ProgressDialog();
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NoData), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            getActivity().finish();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
        }
        else if(Constants.isCORPUSA) {
                MyRequest jsonReq = new MyRequest(Request.Method.GET,
                        query, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responsed) {
                        if (responsed != null) {
                            JSONArray  response = null;
                            JSONArray  lawResp = null;
                            try {

                              lawResp =  responsed.getJSONArray("info");
                              countOfLaws = lawResp.length();
                                mylaws = new String[countOfLaws];
                                myLawsCount = new int[countOfLaws];
                                myIndexes = new String[countOfLaws];
                                for (int j = 0; j < countOfLaws; j++) {
                                    JSONObject js2 = (JSONObject) lawResp.get(j);
                                    mylaws[j] =  js2.getString("law");
                                    myLawsCount[j] = js2.getInt("count");
                                    myIndexes[j] = js2.getString("indexes");
                                }
                            response = responsed.getJSONArray("test");
                            questions = new String[response.length()];
                            variant1 = new String[response.length()];
                            variant2 = new String[response.length()];
                            variant3 = new String[response.length()];
                            variant4 = new String[response.length()];
                            answers = new String[response.length()];
                            myanswers = new String[response.length()];
                            myvariants = new String[response.length()];
                            variants = new String[response.length()];
                            laws = new String[response.length()];

                            wrongs = new Boolean[response.length()];
                            answered = new Boolean[response.length()];
                            if(selectingTest.equals("1pr1") ||selectingTest.equals("1pr2")) timer = (float) (0.75*response.length());
                            Arrays.fill(wrongs, true);
                            Arrays.fill(answered, false);
                            Arrays.fill(myvariants, "-");
                            tvFullCount.setText(response.length()+"");
                            QuestionCount = response.length();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject js = (JSONObject) response.get(i);
                                    questions[i] = js.getString("question");
                                    variant1[i] = js.getString("answer1");
                                    variant2[i] = js.getString("answer2");
                                    variant3[i] = js.getString("answer3");
                                    variant4[i] = js.getString("answer4");
                                    answers[i] = js.getString("right");
                                    laws[i] = js.getString("law");
                                    if(answers[i].equals(variant1[i])) variants[i]="A";
                                    if(answers[i].equals(variant2[i])) variants[i]="B";
                                    if(answers[i].equals(variant3[i])) variants[i]="C";
                                    if(answers[i].equals(variant4[i])) variants[i]="D";
                                }
                             }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Constants.Hide_ProgressDialog();
                            WriteTestStart();
                    }

                    else{
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NoData), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        getActivity().finish();
                    }}
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("", "Error: " + error.getMessage());
                        Constants.Hide_ProgressDialog();
                    }
                });
                AppController.getInstance().addToRequestQueue(jsonReq);

        }


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
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvVariant1 = (TextView) view.findViewById(R.id.tvVariant1);
        tvVariant2 = (TextView) view.findViewById(R.id.tvVariant2);
        tvVariant3 = (TextView) view.findViewById(R.id.tvVariant3);
        tvVariant4 = (TextView) view.findViewById(R.id.tvVariant4);
        scroll = (ScrollView) view.findViewById(R.id.scroll);
        tvLaw = (TextView) view.findViewById(R.id.tvLaw);
        tvSecond = (TextView) view.findViewById(R.id.tvSecond);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        tvFullCount = (TextView) view.findViewById(R.id.tvFullCount);
        lay1 = (LinearLayout) view.findViewById(R.id.lay1);
        lay2 = (LinearLayout) view.findViewById(R.id.lay2);
        lay3 = (LinearLayout) view.findViewById(R.id.lay3);
        lay4 = (LinearLayout) view.findViewById(R.id.lay4);
        layAll = (LinearLayout) view.findViewById(R.id.layAll);
        layLaw = (LinearLayout) view.findViewById(R.id.layLaw);
        lay1var1 = (LinearLayout) view.findViewById(R.id.lay1var1);
        lay1var2 = (LinearLayout) view.findViewById(R.id.lay1var2);
        lay2var1 = (LinearLayout) view.findViewById(R.id.lay2var1);
        lay2var2 = (LinearLayout) view.findViewById(R.id.lay2var2);
        lay3var1 = (LinearLayout) view.findViewById(R.id.lay3var1);
        lay3var2 = (LinearLayout) view.findViewById(R.id.lay3var2);
        lay4var1 = (LinearLayout) view.findViewById(R.id.lay4var1);
        lay4var2 = (LinearLayout) view.findViewById(R.id.lay4var2);
        tvA = (TextView) view.findViewById(R.id.A);
        tvB = (TextView) view.findViewById(R.id.B);
        tvB = (TextView) view.findViewById(R.id.B);
        tvC = (TextView) view.findViewById(R.id.C);
        tvD = (TextView) view.findViewById(R.id.D);
        imgBtnBack = (ImageView) view.findViewById(R.id.imgBtnBack);
        imgBtnBack.setClickable(false);
        imgBtnNext = (ImageView) view.findViewById(R.id.imgBtnNext);

        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);

    }

}
