package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;


public class Levels extends Fragment {

    View view;
    public static String LevelLawID="";
    public  static  int mylvl;
    int count=1;
    ListView list;
    public static DialogInfo2 dlgInf;
    public static Boolean[] isPriced;
    public static Integer[] drFull = new Integer[]{R.drawable.roundedbtn_full,R.drawable.roundedbtn_green2,R.drawable.roundedbtn_black4,R.drawable.roundedbtn_yellow3,R.drawable.roundedbtn_blue5} ;
    public static Integer[] drEmpty = new Integer[]{R.drawable.roundedbtn_empty,R.drawable.roundedbtn_empty2,R.drawable.roundedbtn_empty4,R.drawable.roundedbtn_empty3,R.drawable.roundedbtn_empty5} ;
    public Levels() {
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.listview, container, false);
        list = (ListView) view.findViewById(R.id.list1);
        mylvl=1; count=1;
        Bundle bundle = getArguments();
        if(bundle!=null) {
            LevelLawID = getArguments().getString("id");
            GetLEVEL();
        }
        return  view;
    }

    private void GetLEVEL() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_LEVEL_TREN+"userid="+MainActivity.userID+"&zakonid="+LevelLawID+"&language="+Constants.kaztestLang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("null"))
                        {   mylvl = 1;
                            isPriced = new Boolean[count];
                            Arrays.fill(isPriced, true);
                            ListAdapter listAdapter = new ListAdapter((AppCompatActivity) getActivity(), count);
                            list.setAdapter(listAdapter);
                        }

                        else {

                            try {
                                JSONObject js = new JSONObject(response);
                                mylvl = js.getInt("lvl");
                                count = js.getInt("count");
                                isPriced = new Boolean[count];
                                Arrays.fill(isPriced, true);
                                if(count>=15) {
                                int integ = count/15;
                                int rest = count%15;
                                 count = integ+rest;
                                }
                                else count=1;
                                ListAdapter listAdapter = new ListAdapter((AppCompatActivity) getActivity(), count);
                                list.setAdapter(listAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

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
    class ListAdapter extends BaseAdapter {
        private Activity activity;

        private LayoutInflater inflater;
        int count;
        ImageButton imgStatus, imgBtnBackgr;

        Button btnLevel;
        public ListAdapter(Activity activity, int count) {
            this.activity = activity;
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.level_item, null);
            btnLevel = (Button) convertView.findViewById(R.id.btnLevel);
            imgStatus = (ImageButton) convertView.findViewById(R.id.imgStatus);
            imgBtnBackgr = (ImageButton) convertView.findViewById(R.id.imgBtnBackgr);
            int mPosition=position%5;
            btnLevel.setText(position+1+ " "+activity.getResources().getString(R.string.level));

//            if(position+1<=mylvl)
//            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.kul));
//            else
//            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.kulyp));

//             if(mylvl>1) {
                if(position+1<mylvl)
                {   imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.check));
                    btnLevel.setTextColor(getResources().getColor(R.color.colorWhite));
                    isPriced[position] = false;
                    btnLevel.setBackground(getResources().getDrawable(drFull[mPosition]));}
                else if(position+1<=mylvl) {
                    isPriced[position] = true;
                    btnLevel.setTextColor(getResources().getColor(R.color.colorBlack));
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.kul));
                    btnLevel.setBackground(getResources().getDrawable(drEmpty[mPosition]));}
                else {imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.kulyp));
                      btnLevel.setTextColor(getResources().getColor(R.color.colorBlack));
                    isPriced[position] = true;
                       btnLevel.setBackground(getResources().getDrawable(drEmpty[mPosition]));}
//            }
//            else {
//                 imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.kulyp));
//                btnLevel.setTextColor(getResources().getColor(R.color.colorBlack));
//                btnLevel.setBackground(getResources().getDrawable(drEmpty[mPosition]));
//             }

            btnLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position+1<=mylvl)
                    {
                        dlgInf = new DialogInfo2(position+1,isPriced[position]);
                        dlgInf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                        dlgInf.show(getActivity().getSupportFragmentManager(), "dlg1");
                    }
                    else Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.isNotLeveled), Toast.LENGTH_SHORT).show();
                }
            });



            imgStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position+1<=mylvl)
                    {
                        dlgInf = new DialogInfo2(position+1, isPriced[position]);
                        dlgInf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                        dlgInf.show(getActivity().getSupportFragmentManager(), "dlg1");
                    }
                    else Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.isNotLeveled), Toast.LENGTH_SHORT).show();
                }
            });
            imgBtnBackgr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position+1<=mylvl)
                    {
                        dlgInf = new DialogInfo2(position+1,isPriced[position]);
                        dlgInf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                        dlgInf.show(getActivity().getSupportFragmentManager(), "dlg1");
                    }
                    else Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.isNotLeveled), Toast.LENGTH_SHORT).show();
                }
            });

            return  convertView;
        }




    }
    @SuppressLint("ValidFragment")
    public static class DialogInfo2 extends DialogFragment {

        TextView tvInfo;
        View v;
        Button btnOK, btnCancel;
        int pos;
        LinearLayout layPrice;
        Boolean isPriced;
        public DialogInfo2(int pos, boolean isPriced) {
            this.pos = pos;
            this.isPriced = isPriced;
        }

        @SuppressLint("SetTextI18n")
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.textview, null);
            tvInfo = (TextView) v.findViewById(R.id.tvInfo);
            btnCancel = (Button) v.findViewById(R.id.btnCancel);
            layPrice = (LinearLayout) v.findViewById(R.id.layPrice);
            btnOK = (Button) v.findViewById(R.id.btnOK);
            tvInfo.setText(getActivity().getResources().getString(R.string.tvPrice)+"");
            if(!isPriced) layPrice.setVisibility(View.GONE);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   OpenTestLevel(pos);
                    dlgInf.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgInf.dismiss();
                }
            });
            return v;
        }
        private void OpenTestLevel(int lvl) {
            Bundle bundle = new Bundle();
            bundle.putString("lvl", lvl+"");
            bundle.putString("mylvl", mylvl+"");
            bundle.putString("id", LevelLawID+"");
            Fragment fragment;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragment = new Training();
            fragment.setArguments(bundle);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        }

    }

}

