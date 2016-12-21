package kz.drw.kaztest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.FeedItem;
import kz.drw.kaztest.utils.MyRequest;


public class History extends Fragment {

    ListView list;
    List<FeedItem> arrList;
    public  static  String myCorpus="";
    public History() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.listview, container, false);
        list = (ListView) view.findViewById(R.id.list1);
        arrList = new ArrayList<>();
        GetHistory();
        return  view;

    }

    private void GetHistory() {
        Constants.Show_ProgressDialog(getActivity(), getResources().getString(R.string.wait));
        JsonArrayRequest req = new JsonArrayRequest(Constants.GET_HISTORY+MainActivity.userID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Constants.Show_ProgressDialog(getActivity(), getResources().getString(R.string.wait));
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject js = (JSONObject) response.get(i);
                                FeedItem feedItem = new FeedItem();
                                feedItem.setProgram(js.getString("programma"));
                                feedItem.setKorpus(js.getString("korpus"));
                                feedItem.setCol(js.getInt("col"));
                                feedItem.setWrongcol(js.getInt("wrongcol"));
                                feedItem.setRightcol(js.getInt("rightcol"));
                                feedItem.setDate(js.getString("date"));
                                feedItem.setTime(js.getString("time"));
                                arrList.add(feedItem);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Constants.Hide_ProgressDialog();
                        ListAdapter listAdapter = new ListAdapter((AppCompatActivity) getActivity(), arrList);
                        list.setAdapter(listAdapter);


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    static class ListAdapter extends BaseAdapter {
        private Activity activity;
        FeedItem item;
        private LayoutInflater inflater;
        List<FeedItem> arrList;
        String[] d2;
        TextView tvKorpus, tvPoint, tvDate;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        int count;
        public ListAdapter(Activity activity, List<FeedItem> arrList) {
            this.activity = activity;
            this.arrList = arrList;
        }

        @Override
        public int getCount() {
            return arrList.size();
        }

        @Override
        public Object getItem(int location) {
            return arrList.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.history_item, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            tvKorpus = (TextView) convertView.findViewById(R.id.tvKorpus);
            tvPoint = (TextView) convertView.findViewById(R.id.tvPoint);
            item = arrList.get(position);
            if(!item.getDate().equals("")) {
                String[] date = item.getDate().split(" ");
                date[0] = date[0].replace("."," ");
                String[] d =   date[0].split(" ");
                d2=d;
                String month = "";
                if(Constants.kaztestLang) month=setDateKaz(d[1]);
                else month=setDateRus(d[1]);
                tvDate.setText(d2[0]+"\n"+month+"\n"+d2[2]);
               }
                else  tvDate.setText("");
             myCorpus = item.getKorpus();
            if(myCorpus.equals("B")) myCorpus="Б";
            if(myCorpus.equals("Б")) {
                if(!Constants.kaztestLang)
                tvKorpus.setText("Корпус: " + myCorpus + ", " + activity.getResources().getString(R.string.program) + " " + item.getProgram());
                else tvKorpus.setText(myCorpus+" корпусы" + ", " + activity.getResources().getString(R.string.program) + " " + item.getProgram());
            }
            else  if(!Constants.kaztestLang) tvKorpus.setText("Корпус: "+myCorpus);
            else tvKorpus.setText(myCorpus+" корпусы");
//            tvPoint.setText(item.getRightcol()+" / "+item.getCol()+" балл");
            tvPoint.setText(item.getRightcol()+"");




            return  convertView;
        }
        public  static String  setDateRus(String month) {
            int monthInt = Integer.parseInt(month);
            String monStr="";
            switch (monthInt) {
                case 1:    monStr="января"; break;
                case 2:    monStr="февраля"; break;
                case 3:    monStr="марта"; break;
                case 4:    monStr="апреля"; break;
                case 5:    monStr="мая"; break;
                case 6:    monStr="июня"; break;
                case 7:    monStr="июля"; break;
                case 8:    monStr="августа"; break;
                case 9:    monStr="сентября"; break;
                case 10:    monStr="октября"; break;
                case 11:    monStr="ноября"; break;
                case 12:    monStr="декабря"; break;
            }
            return monStr;
        }
        public static String setDateKaz(String month) {
            int monthInt = Integer.parseInt(month);
            String monStr="";
            switch (monthInt) {
                case 1:    monStr="қаңтар"; break;
                case 2:    monStr="ақпан"; break;
                case 3:    monStr="наурыз"; break;
                case 4:    monStr="сәуір"; break;
                case 5:    monStr="мамыр"; break;
                case 6:    monStr="маусым"; break;
                case 7:    monStr="шілде"; break;
                case 8:    monStr="тамыз"; break;
                case 9:    monStr="қыркүйек"; break;
                case 10:    monStr="қазаг"; break;
                case 11:    monStr="қараша"; break;
                case 12:    monStr="желтоқсан"; break;
            }
            return monStr;
        }
    }



}
