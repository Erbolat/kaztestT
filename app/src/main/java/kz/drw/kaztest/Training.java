package kz.drw.kaztest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.CircleImageView;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyRequest;


public class Training extends Fragment {

    View view;
    ImageView imgBtnBack, imgBtnNext;
    public  static  String [] questions, variant1, variant2, variant3, variant4, answers, myanswers, myvariants, laws, variants;
    public static  Boolean[] wrongs, answered;
    ImageView img1, img2, img3, img4;
    ScrollView scroll;
    String  lvl="", mylvl="";
    TextView tvQuestion, tvVariant1,tvVariant2,tvVariant3,tvVariant4, tvCount, tvFullCount, tvLaw, tvA, tvB, tvC, tvD;
    LinearLayout lay1, lay2, lay3, lay4, layAll, layLaw, lay1var1, lay1var2, lay2var1, lay2var2, lay3var1, lay3var2, lay4var1, lay4var2, layHor;
    String selectingTest="";
    static int currentQuestionID=0, countCorrect=0, countWrong=0, QuestionCount=0, currentLastID=0;
    Boolean  isTraining=false, isWrong=false, isRest=false, isStart=false, isBack=false;
    Integer[] lastsQuestionIDs;
    private  static  int restCount=0;
    Toast mToast;
    Vibrator mVibrator;
    public Training() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.testing, container, false);
        currentQuestionID=0;
        initResources(); Constants.isCORPUSB=false;
        layAll.setVisibility(View.GONE);
        isTraining=false; isWrong=false;
        countCorrect=0;
        Constants.isTest=true;

        mToast = Toast.makeText(getActivity(), getResources().getString(R.string.isNotAnswered), Toast.LENGTH_SHORT);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            selectingTest = getArguments().getString("id");
            lvl = getArguments().getString("lvl");
            mylvl = getArguments().getString("mylvl");
            StartTesting();
        }

//        if(Constants.kaztestLang)
//            tvLaw.setText(Programms.titlesKaz[Integer.parseInt(selectingTest)]);
//        else   tvLaw.setText(Programms.titlesRus[Integer.parseInt(selectingTest)]);


        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered[currentQuestionID]){
                if (currentQuestionID <QuestionCount) {
                    myvariants[currentQuestionID]="A";
                    answered[currentQuestionID]=true;
                    if (answers[currentQuestionID].equals(variant1[currentQuestionID])) {
                        if(currentQuestionID<QuestionCount)
                        {

                            myanswers[currentQuestionID]=variant1[currentQuestionID];
                            answered[currentQuestionID]=true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    NextQuestion();

                                }
                            }, 500);
                        }
                        lay1var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        lay1var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        img1.setImageDrawable(getResources().getDrawable(R.drawable.check));
                        tvVariant1.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvA.setTextColor(getResources().getColor(R.color.colorWhite));
                        countCorrect++;

                    }
                    else {
                        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                        mVibrator.vibrate(520);
                        lay1var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        lay1var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        tvVariant1.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvA.setTextColor(getResources().getColor(R.color.colorWhite));
                        if(isTraining) RightAnswer();
                        if(currentQuestionID<QuestionCount)
                            wrongs[currentQuestionID] = false;
                    }}
                }
            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered[currentQuestionID]){
                    if (currentQuestionID <QuestionCount) {
                  answered[currentQuestionID]=true;
                    myvariants[currentQuestionID]="B";
                    if (answers[currentQuestionID].equals(variant2[currentQuestionID])) {
                        if(currentQuestionID<QuestionCount)
                        {
                            myanswers[currentQuestionID]=variant2[currentQuestionID];
                            answered[currentQuestionID]=true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        NextQuestion();
                                }
                            }, 500);
                        }
                        lay2var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        lay2var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        img2.setImageDrawable(getResources().getDrawable(R.drawable.check));
                        tvVariant2.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvB.setTextColor(getResources().getColor(R.color.colorWhite));
                        countCorrect++;

                       }
                    else {
                        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                        mVibrator.vibrate(520);
                        lay2var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        lay2var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        tvVariant2.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvB.setTextColor(getResources().getColor(R.color.colorWhite));
                        if(isTraining) RightAnswer();
                        if(currentQuestionID<QuestionCount)
                        wrongs[currentQuestionID] = false;
                    }


            }}
               }
        });
        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered[currentQuestionID]){
                    if (currentQuestionID <QuestionCount) {
                    answered[currentQuestionID] = true;
                    myvariants[currentQuestionID] = "C";
                    if (answers[currentQuestionID].equals(variant3[currentQuestionID])) {
                        lay3var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        lay3var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        tvVariant3.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvC.setTextColor(getResources().getColor(R.color.colorWhite));
                        img3.setImageDrawable(getResources().getDrawable(R.drawable.check));
                        if (currentQuestionID < QuestionCount) {

                            myanswers[currentQuestionID] = variant3[currentQuestionID];
                            answered[currentQuestionID] = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        NextQuestion();
                                }
                            }, 500);
                        }
                        countCorrect++;
                    } else {
                        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                        mVibrator.vibrate(520);
                        lay3var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        lay3var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        tvVariant3.setTextColor(getResources().getColor(R.color.colorWhite));
                        tvC.setTextColor(getResources().getColor(R.color.colorWhite));
                        if (isTraining) RightAnswer();
                        if (currentQuestionID < QuestionCount)
                            wrongs[currentQuestionID] = false;
                    }
                }
                }

            }
        });
        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered[currentQuestionID]){
                    if (currentQuestionID <QuestionCount)
                  {
                answered[currentQuestionID] = true;
                myvariants[currentQuestionID] = "D";
                if (answers[currentQuestionID].equals(variant4[currentQuestionID])) {
                    if (currentQuestionID < QuestionCount) {

                        myanswers[currentQuestionID] = variant4[currentQuestionID];
                        answered[currentQuestionID] = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                    NextQuestion();
                            }
                        }, 500);

                    }
                    lay4var1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    lay4var2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    img4.setImageDrawable(getResources().getDrawable(R.drawable.check));
                    tvVariant4.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvD.setTextColor(getResources().getColor(R.color.colorWhite));
                    countCorrect++;


                } else {
                    mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                    mVibrator.vibrate(520);
                    lay4var1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    lay4var2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    tvVariant4.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvD.setTextColor(getResources().getColor(R.color.colorWhite));
                    if (isTraining) RightAnswer();

                    if (currentQuestionID < QuestionCount)
                        wrongs[currentQuestionID] = false;
                }
            }
                }
            }
        });

            imgBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    answered[currentQuestionID]=false;
                    imgBtnBack.setVisibility(View.VISIBLE);

                }
            });
        return  view;
    }


    void NextQuestion(){
            if(currentQuestionID!=QuestionCount-1) {
                currentQuestionID=currentQuestionID+1;
                if(!answered[currentQuestionID])
                    WriteTest();
                else NextQuestion();
             }
        else {
                if(Integer.parseInt(mylvl)==Integer.parseInt(lvl))
                {
                   // Toast.makeText(getActivity().getApplicationContext(), "Поздравляю! Вы прошли этот уровень.", Toast.LENGTH_SHORT).show();
                    GetSmileInfo(2);

                }
                else  {
                    GetSmileInfo(3);

                        }


            }

    }
    private void ADDLEVEL() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.FINISH_LEVEL+"user="+MainActivity.userID+"&zakon="+selectingTest+"&language="+Constants.kaztestLang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        OpenLevel();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void OpenLevel() {
        Bundle bundle = new Bundle();
        bundle.putString("id", selectingTest+"");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = new Levels();
        fragment.setArguments(bundle);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }
    private void RightAnswer() {

        if(answers[currentQuestionID].equals(variant1[currentQuestionID]))
            lay1.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        else if(answers[currentQuestionID].equals(variant2[currentQuestionID]))
            lay2.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        else if(answers[currentQuestionID].equals(variant3[currentQuestionID]))
            lay3.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        else if(answers[currentQuestionID].equals(variant4[currentQuestionID]))
            lay4.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        isWrong=true;

//            Toast.makeText(getActivity(), "Вы не прошли тренировку", Toast.LENGTH_SHORT).show();
            GetSmileInfo(1);




    }
    private void GetSmileInfo(final int type) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView img = (ImageView) dialog.findViewById(R.id.imgSmile);
        TextView tv = (TextView) dialog.findViewById(R.id.tvText);
        TextView tv2 = (TextView) dialog.findViewById(R.id.tvText2);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        if(type==1) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.smile_bad));
            tv.setText(getActivity().getResources().getString(R.string.isFailTraining));

            tv2.setVisibility(View.VISIBLE);
        }
        else if(type==2) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.smile));
            tv.setText(getActivity().getResources().getString(R.string.isGoodTraining));
            tv2.setVisibility(View.GONE);
        }
        else {
            img.setImageDrawable(getResources().getDrawable(R.drawable.smile));
            tv.setText(getActivity().getResources().getString(R.string.isGoodTraining));
            tv2.setVisibility(View.GONE);
        }
        dialog.show();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==2)   ADDLEVEL();
                else if(type==1)   OpenTest(Integer.parseInt(Levels.LevelLawID));
                else   OpenLevel();
                dialog.cancel();
                dialog.dismiss();

            }
        });

    }
    private void OpenTest(int id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id+"");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = new Levels();
        fragment.setArguments(bundle);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    private void initResources() {
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvVariant1 = (TextView) view.findViewById(R.id.tvVariant1);
        tvVariant2 = (TextView) view.findViewById(R.id.tvVariant2);
        tvVariant3 = (TextView) view.findViewById(R.id.tvVariant3);
        tvVariant4 = (TextView) view.findViewById(R.id.tvVariant4);
        tvLaw = (TextView) view.findViewById(R.id.tvLaw);
        scroll = (ScrollView) view.findViewById(R.id.scroll);
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
        imgBtnNext = (ImageView) view.findViewById(R.id.imgBtnNext);

        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);

        imgBtnNext.setVisibility(View.GONE);
        imgBtnBack.setVisibility(View.GONE);
        tvLaw.setVisibility(View.GONE);

    }

    private void StartTesting() {
            String query="";
            isTraining=true;
                layLaw.setVisibility(View.GONE);
//                if(Integer.parseInt(lvl)>=Integer.parseInt(mylvl))
//                Toast.makeText(getActivity(), getResources().getString(R.string.alertTraining), Toast.LENGTH_LONG).show();
                if(!selectingTest.equals("")) {
                query = Constants.TREN_SELECT_LAW+"page="+lvl+"&zakonid="+selectingTest+"&userid="+MainActivity.userID+"&language="+Constants.kaztestLang;   //SELECT TEST

                    if(!query.equals("")) GetTest(query);
                     }
                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();
    }

    private  void  GetTest(String query){
        JsonArrayRequest req = new JsonArrayRequest(query,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()>0) {
                            Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));
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
                        Arrays.fill(wrongs, true);
                        Arrays.fill(answered, false);
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

                        Constants.Hide_ProgressDialog();
                        WriteTestStart();
                    } else { Constants.Hide_ProgressDialog();
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NoData), Toast.LENGTH_SHORT).show();}

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
    private void WriteTest() {
//        if(currentQuestionID==QuestionCount-1) imgBtnNext.setVisibility(View.GONE);
        if(currentQuestionID<QuestionCount) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layAll.setVisibility(View.VISIBLE);
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
                    scroll.smoothScrollTo(0,0);
                }
            }, 100);

        }
    }
    private void WriteTestStart() {
        if(currentQuestionID<QuestionCount) {

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
        else {

            startActivity(new Intent(getActivity(), Result.class).putExtra("right",countCorrect+"").putExtra("count",QuestionCount+""));
            Constants.isTest=false;
        }
    }


}
