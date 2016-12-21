package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;

import static kz.drw.kaztest.Profile.newPass;
import static kz.drw.kaztest.Profile.newPassRe;
import static kz.drw.kaztest.Profile.oldPass;


public  class Programms extends Fragment {

    View view;

    public  static int program = 0;
   public static String corpus="";
    LinearLayout layPr1, layPr2, layPr3;
    ListView list;
    public static String [] titlesRus, titlesKaz;
    Integer[] ids;
    public  static  int minBall=0;
    FloatingActionButton fab;
    Boolean[] checks;
    int count=0;
    public static   DialogInfo dlg;
    public  static  String[] lawsNames;
    static Boolean isCorpusA=false;
    static String lawsStr="";

    public Programms() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        corpus="";
        Bundle bundle = getArguments();
        if(bundle!=null) {
            corpus = getArguments().getString("select");
        }

        if(corpus.equals("2"))
        {
        view =  inflater.inflate(R.layout.programms, container, false);
        layPr1 = (LinearLayout) view.findViewById(R.id.layProgram1);
        layPr2 = (LinearLayout) view.findViewById(R.id.layProgram2);
        layPr3 = (LinearLayout) view.findViewById(R.id.layProgram3);
        if(corpus.equals("2")) layPr3.setVisibility(View.VISIBLE);

        layPr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new DialogInfo();
                program=1;   minBall = 95;
                dlg.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dlg.show(getActivity().getSupportFragmentManager(), "dlg1");
            }
        });
        layPr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new DialogInfo();
                program=2;  minBall = 72;
                dlg.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dlg.show(getActivity().getSupportFragmentManager(), "dlg2");
            }
        });
        layPr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new DialogInfo();
                program=3;       minBall = 45;
                dlg.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dlg.show(getActivity().getSupportFragmentManager(), "dlg3");
            }
        });

        }
        else {
            view =  inflater.inflate(R.layout.listview, container, false);
            list = (ListView) view.findViewById(R.id.list1);
            fab = (FloatingActionButton) view.findViewById(R.id.fab);
            GetList();
        }
        return  view;
    }

private  void GetList(){

    Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));
    JsonArrayRequest req = new JsonArrayRequest(Constants.GET_LIST_TREN,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    titlesKaz = new String[response.length()];
                    titlesRus = new String[response.length()];
                    ids = new Integer[response.length()];
                    count = response.length();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject js = (JSONObject) response.get(i);
                            titlesRus[i] = js.getString("zakon1");
                            titlesKaz[i] = js.getString("zakonkz");
                            ids[i] = js.getInt("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Constants.Hide_ProgressDialog();
                    if(getArguments().getString("isA").equals("0")){
                          checks = new Boolean[titlesKaz.length];
                        Arrays.fill(checks, false);
                        fab.setVisibility(View.VISIBLE);
                        String[] laws;
                        if(Constants.kaztestLang==true) laws=titlesKaz;
                        else laws=titlesRus;
                        ListAdapterCorpusA listAdapter = new ListAdapterCorpusA((AppCompatActivity) getActivity(), laws, ids);
                         list.setAdapter(listAdapter);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int countChecks=0;
                                int lawID = 0;
                                for(int i=0; i<checks.length; i++) {
                                    if(checks[i]) {
                                        countChecks++;
                                        lawID=ids[i];
                                    }
                                }
                                if(countChecks>0)
                                    OpenTestLevel(lawID);

                                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotSelected), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        checks = new Boolean[titlesKaz.length];
                        Arrays.fill(checks, false);
                        fab.setVisibility(View.VISIBLE);
                        final String[] laws;
                        final Integer[] positions;

                        if(Constants.kaztestLang==true) laws=titlesKaz;
                        else laws=titlesRus;
                        ListAdapterCorpusA listAdapter = new ListAdapterCorpusA((AppCompatActivity) getActivity(), laws, ids);
                        list.setAdapter(listAdapter);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int countChecks=0;
                                String idsStr="";
                                for(int i=0; i<checks.length; i++) {
                                    if(checks[i]) {
                                        countChecks++;
                                        lawsStr+=ids[i]+",";
                                        idsStr+=i+",";
                                    }
                                    else lawsStr+="";
                                }
                                if(countChecks>0){
                                    char[] d = lawsStr.toCharArray();
                                    lawsStr="";
                                    lawsNames = new String[countChecks];
                                    String[] lawids = idsStr.split(",");
                                    for(int i=0; i<countChecks; i++){
                                        lawsNames[i]= laws[Integer.parseInt(lawids[i])];
                                    }
                                    for(int i=0; i<d.length-1; i++)
                                        lawsStr+=d[i];
                                    dlg = new DialogInfo();
                                    program=4;
                                    dlg.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                                    dlg.show(getChildFragmentManager(), "dlg1");

                                }
                                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotSelected), Toast.LENGTH_SHORT).show();
                            }
                        });
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
    private void OpenTestLevel(int id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id+"");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = new Levels();
        fragment.setArguments(bundle);
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }


    class ListAdapterCorpusA extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        NetworkImageView image;
        Integer[] ids;
        TextView  tvNumber,tvVariant;
        NetworkImageView imageview;
        String[] titles;
        ImageView img;
        LinearLayout layVer1, layVer2;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public ListAdapterCorpusA(Activity activity, String[] titles, Integer[] ids) {
            this.activity = activity;
            this.titles = titles;
            this.ids = ids;
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
                convertView = inflater.inflate(R.layout.training_item, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            tvVariant = (TextView) convertView.findViewById(R.id.tvVariant);
            layVer1 = (LinearLayout) convertView.findViewById(R.id.layVer1);
            layVer2 = (LinearLayout) convertView.findViewById(R.id.layVer2);
            img = (ImageView) convertView.findViewById(R.id.img);
            img.setTag(position);
            tvNumber.setText(position+1+"");
            tvVariant.setText(titles[position]);
            if(!checks[position]) img.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
            else img.setImageDrawable(getResources().getDrawable(R.drawable.check));
            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getArguments().getString("isA").equals("0")){
                        if(checks[position]) checks[position] = false;
                        else  {
                            Arrays.fill(checks,false);
                            checks[position]=true;}
                        notifyDataSetChanged();
                    }
                    else {
                        if(checks[position]) checks[position] = false;
                        else  checks[position]=true;
                         notifyDataSetChanged();
                    }
                }
            });

            return  convertView;
        }



}
    @SuppressLint("ValidFragment")
   public static class DialogInfo extends DialogFragment {
        final String LOG_TAG = "myLogs";
        ListView list;
        String[]info;
        LinearLayout lay, layCorpusA;
        Button  btnCancel, btnOK;
        TextView tvLawCount, tvLawCountText;
        View v;
        public DialogInfo() {
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(Programms.program==1)
                info = getResources().getStringArray(R.array.priceProg1);
            else if(Programms.program==2)
                info = getResources().getStringArray(R.array.priceProg2);
            else if(Programms.program==3) info = getResources().getStringArray(R.array.priceProg3);
            else {
                info = lawsNames;

            }
            if(program!=4) {
            v = inflater.inflate(R.layout.listview, null);
            list = (ListView) v.findViewById(R.id.list1);
            lay = (LinearLayout) v.findViewById(R.id.lay);
            btnCancel = (Button) v.findViewById(R.id.btnCancel);
            layCorpusA = (LinearLayout) v.findViewById(R.id.layCorpusA);
            btnOK = (Button) v.findViewById(R.id.btnOK);
            tvLawCount = (TextView) v.findViewById(R.id.tvLawCount);
            tvLawCountText = (TextView) v.findViewById(R.id.tvLawCountText);
            lay.setVisibility(View.VISIBLE);
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(),R.layout.textview2, R.id.text, info);
                list.setAdapter(listAdapter);
            }
            else {
                v = inflater.inflate(R.layout.listview_new, null);
                list = (ListView) v.findViewById(R.id.list1);
                lay = (LinearLayout) v.findViewById(R.id.lay);
                btnCancel = (Button) v.findViewById(R.id.btnCancel);
                layCorpusA = (LinearLayout) v.findViewById(R.id.layCorpusA);
                btnOK = (Button) v.findViewById(R.id.btnOK);
                tvLawCount = (TextView) v.findViewById(R.id.tvLawCount);
                tvLawCountText = (TextView) v.findViewById(R.id.tvLawCountText);
                lay.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams mParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,800);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) list
                        .getLayoutParams();
                layoutParams.setMargins(0, 30, 0, 0);
                list.setLayoutParams(mParam);
                list.setLayoutParams(layoutParams);
                layCorpusA.setVisibility(View.VISIBLE);

                tvLawCount.setText("Cумма: "+5*info.length+" тг");
                tvLawCountText.append(" "+info.length);

                Lists listAdapter = new Lists(getActivity(), info);
                list.setAdapter(listAdapter);
            }
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(program!=4)
                    OpenTest(Programms.program);
                    else OpenCorpus(lawsStr);
                    Programms.dlg.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Programms.dlg.dismiss();
                }
            });
            return v;
        }
        private void OpenTest(int selected) {
            Bundle bundle = new Bundle();
            Constants.Program = selected+"";
            bundle.putString("select", Programms.corpus+"pr"+selected);
            Fragment fragment;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragment = new Corpus();
            fragment.setArguments(bundle);
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        private  void OpenCorpus(String ids) {
            Bundle bundle = new Bundle();
            bundle.putString("zakon", ids+"");
            bundle.putString("select", "1pr2");
            Fragment fragment;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragment = new Corpus();
            fragment.setArguments(bundle);
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
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
            if (convertView == null)
                convertView = inflater.inflate(R.layout.alert_price_corpus_a, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            tvLaw = (TextView) convertView.findViewById(R.id.tvLaw);
            tvSumma = (TextView) convertView.findViewById(R.id.tvSumma);
            tvLaw.setText(titles[position]+"");

            return  convertView;
        }



    }
}
