package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.CircleImageView;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.FeedItem;

import static kz.drw.kaztest.MainActivity.patron;
import static kz.drw.kaztest.MainActivity.photo;


public class RatingItem extends Fragment implements DatePickerDialog.OnDateSetListener{
    static ListView list;
    static List<FeedItem> arrList;
    static  TextView tvMonYear;
    public static  String myPhoto="";
    public  static  int myrating=0;
    static  TextView tvID, tvName, tvTimes, tvBall, Number;
    static Activity act;
    public static  Boolean canpass=true;
    public static int thisMonth, thisYear,thisDay;
    public  static Boolean firstOpen=false;
    public  static  LinearLayout layMyRating, layMonYear;
    public static CircleImageView imgMyAva;
    public  static  ImageLoader imgLoad = AppController.getInstance().getImageLoader();
    ImageView Next,Back;
    public static  int page=1, mypage=1, base=0;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ratings, container, false);
        page=1; firstOpen=false;
        DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
        String date = dff.format(Calendar.getInstance().getTime());
        String[] dates = date.split("-");
        thisDay = Integer.parseInt(dates[0]);
        thisMonth = Integer.parseInt(dates[1]);
        thisYear = Integer.parseInt(dates[2]);
        initRecources();
        if(Constants.kaztestLang)
            tvMonYear.setText(History.ListAdapter.setDateKaz2(thisMonth+"")+"   "+thisYear);
        else  tvMonYear.setText(History.ListAdapter.setDateRus2(thisMonth+"")+"   "+thisYear);

        act = getActivity();
        Back.setImageDrawable(getResources().getDrawable(R.drawable.back_w));

        arrList = new ArrayList<>();
        Number.setText(page+"");

        if(!MainActivity.userID.equals("")){
            GetRatingGos2();
        GetRatingGos(thisMonth+"",thisYear+"",page);}
        else Toast.makeText(act, getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();
        layMonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener = null;
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(listener);
                pd.show(getChildFragmentManager(), "MonthYearPickerDialog");
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page!=1){
                    page-=1;
                    Number.setText(page+"");
                    Back.setImageDrawable(getResources().getDrawable(R.drawable.le));
                    if(page==1)   Back.setImageDrawable(getResources().getDrawable(R.drawable.back_w));
                    GetRatingGos(thisMonth+"",thisYear+"",page);
                }
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page<=9) {
                page+=1;
                if(page!=1)  Back.setImageDrawable(getResources().getDrawable(R.drawable.le));
                Number.setText(page+"");
                GetRatingGos(thisMonth+"",thisYear+"",page);
                }

            }
        });

        layMyRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myrating>0){
                    if(myrating<=10) {
                        page=1;
                        Back.setImageDrawable(getResources().getDrawable(R.drawable.back_w));}
                    else {
                     mypage = myrating/10+1;
                        base = myrating%10;
                        page=mypage;
                        layMyRating.setVisibility(View.GONE);
                     Back.setImageDrawable(getResources().getDrawable(R.drawable.le));}
                     Number.setText(mypage+"");
                     GetRatingGos(thisMonth+"",thisYear+"",mypage);
                }
            }
        });

        return  view;
    }

    private void initRecources() {
        tvID = (TextView) view.findViewById(R.id.tvID);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvBall = (TextView) view.findViewById(R.id.tvBall);
        Number = (TextView) view.findViewById(R.id.Number);
        tvTimes = (TextView) view.findViewById(R.id.tvTimes);
        Back = (ImageView) view.findViewById(R.id.Back);
        Next = (ImageView) view.findViewById(R.id.Next);
        list = (ListView) view.findViewById(R.id.list1);
        layMyRating = (LinearLayout) view.findViewById(R.id.layMyRating);
        imgMyAva = (CircleImageView) view.findViewById(R.id.imgMyAva);
        tvMonYear = (TextView) view.findViewById(R.id.tvMonYear);
        layMonYear = (LinearLayout) view.findViewById(R.id.layMonYear);
    }
    public static void GetRatingGos2() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.GET_RATING_GOS+"month=null&year=null&userid="+MainActivity.userID+"&page=1", null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Constants.canpas = response.getBoolean("canpass");
                            Constants.ratingID = response.getInt("addratingid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }

    public static void GetRatingGos(String month, String year, final int page) {
         Constants.Show_ProgressDialog(act, act.getResources().getString(R.string.wait));
         arrList = new ArrayList<>();
         myrating = 0;
         JSONObject jsObj=null;
         JsonObjectRequest jsObjRequest = new JsonObjectRequest
                 (Request.Method.GET, Constants.GET_RATING_GOS+"month="+month+"&year="+year+"&userid="+MainActivity.userID+"&page="+page, null, new Response.Listener<JSONObject>() {

                     @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                     @Override
                     public void onResponse(JSONObject response) {
                         try {

                             if(!response.getString("myrating").equals("null")) {
                                 myrating = response.getInt("myrating");
                                 myrating = myrating+1;
                                 if (myrating == 0) {
                                     layMyRating.setVisibility(View.GONE);
                                     tvID.setText("");
                                     tvName.setText("Вас нет в рейтинге");


                                 } else {
                                     tvID.setText(myrating + "");
                                     tvBall.setText(response.getInt("rightcol")+"/"+response.getInt("col")+"");
                                     tvTimes.setText(response.getString("time"));
                                     tvName.setText(act.getResources().getString(R.string.you));
                                     layMyRating.setVisibility(View.VISIBLE);
                                     if(myrating<=10)
                                     layMyRating.setVisibility(View.GONE);
                                     else layMyRating.setVisibility(View.VISIBLE);

                                     mypage = myrating/10+1;
                                     if(page==mypage)
                                     layMyRating.setVisibility(View.GONE);
                                     else layMyRating.setVisibility(View.VISIBLE);
                                 }
                             }
                             else {
                                 tvID.setText("");
                                 tvName.setText(act.getResources().getString(R.string.youAreNot));
                                 layMyRating.setVisibility(View.GONE);

                             }
                             myPhoto = response.getString("photo");

                             if(!myPhoto.equals("null")) {
                                 imgMyAva.setVisibility(View.VISIBLE);
                                 myPhoto = myPhoto.replace("~","");
                                 imgMyAva.setBackground(null);
                                 imgMyAva.setImageUrl("http://www.kaztest.com"+myPhoto, imgLoad);}
                             else {
                                 imgMyAva.setBackground(act.getResources().getDrawable(R.drawable.social));
                             }
                             JSONArray jsArray  = response.getJSONArray("rating");
                            if(jsArray.length()>0) {
                                for (int i = 0; i < jsArray.length(); i++) {
                                    JSONObject js = (JSONObject) jsArray.get(i);
                                    FeedItem feedItem = new FeedItem();
                                    if (!js.getString("middlename").equals("null"))
                                        feedItem.setUserName(js.getString("firstname") + " " + js.getString("middlename"));
                                    else feedItem.setUserName(js.getString("firstname"));
                                    feedItem.setCol(js.getInt("col"));
                                    feedItem.setWrongcol(js.getInt("wrongcol"));
                                    feedItem.setRightcol(js.getInt("rightcol"));
                                    feedItem.setDate(js.getString("date"));
                                    feedItem.setTime(js.getString("time"));
                                    feedItem.setImage(js.getString("photo"));
                                    feedItem.setCity(js.getString("city"));
                                    feedItem.setInRating(js.getInt("inrating"));
                                    arrList.add(feedItem);

                                }
                            }
                            else Toast.makeText(act.getApplicationContext(), act.getResources().getString(R.string.emptyList), Toast.LENGTH_SHORT).show();
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         ListAdapter listAdapter = new ListAdapter((AppCompatActivity) act, arrList);
                         list.setAdapter(listAdapter);
                         Constants.Hide_ProgressDialog();
                     }
                 }, new Response.ErrorListener() {

                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Constants.Hide_ProgressDialog();

                     }
                 });
         AppController.getInstance().addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    static class ListAdapter extends BaseAdapter {
        private Activity activity;
        FeedItem item;
        private LayoutInflater inflater;
        List<FeedItem> arrList;
        TextView tvNumber, tvName, tvCity, tvRating, tvTime;
        CircleImageView imgAva;
        ImageView imgAvaProf;
        LinearLayout layContent;
        ImageLoader imageLoader=null;
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


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.rating_item, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            String photos="";
            imgAva = (CircleImageView) convertView.findViewById(R.id.imgAva);
            tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            tvName = (TextView) convertView.findViewById(R.id.tvName);
            tvCity = (TextView) convertView.findViewById(R.id.tvCity);
            tvRating = (TextView) convertView.findViewById(R.id.tvRating);
            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            layContent = (LinearLayout) convertView.findViewById(R.id.layContent);
            item = arrList.get(position);
            tvNumber.setText(item.getInRating()+"");
            tvName.setText(item.getUserName());
            tvRating.setText(item.getRightcol()+"/"+item.getCol()+"");
            tvTime.setText(item.getTime());
            String myCity=item.getCity();
            if(myCity.equals("null")) myCity="";
            tvCity.setText(myCity);
            photos= item.getImage();
            if(!photos.equals("null")) {
            imgAva.setVisibility(View.VISIBLE);
            photos = photos.replace("~","");
            imgAva.setBackground(null);
            imgAva.setImageUrl("http://www.kaztest.com"+photos, imageLoader);}
            else {
             imgAva.setBackground(activity.getResources().getDrawable(R.drawable.social));
            }

            if(page==1) {

                if(position<=4)
                {
                    if(myrating<=10) {
                        if(position==myrating%10-1)
                            layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_red_test));
                        else
                            layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_green_test2));
                    }
                    else
                        layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_white_test));
                }
                else  {
                    if(myrating>5) {
                        if( position==myrating%10-1) {
                        layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_red_test));
                    }
                    else   layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_white_test));}
                   else{
                    layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_white_test));
              }
             }
            }
            else {
                if(myrating>10) {
                if(position==myrating%10-1) {
                    layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_red_test));
                }
                else {
                    layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_white_test));
                }
                }
                    else {
                        layContent.setBackground(activity.getResources().getDrawable(R.drawable.border_white_test));
                    }


            }

            return  convertView;
        }

    }
    @SuppressLint("ValidFragment")
    public static  class MonthYearPickerDialog extends DialogFragment {

        private static final int MAX_YEAR = 2099;
        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(thisMonth);

            int year = 2016;
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton(getResources().getString(R.string.agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String month = "";
                            if(Constants.kaztestLang)    tvMonYear.setText(History.ListAdapter.setDateKaz2(monthPicker.getValue()+"")+"  "+yearPicker.getValue());
                            else tvMonYear.setText(History.ListAdapter.setDateRus2(monthPicker.getValue()+"")+"  "+yearPicker.getValue());
                            thisMonth  = monthPicker.getValue();
                            thisYear=yearPicker.getValue();
                            page=1;
                            GetRatingGos(monthPicker.getValue()+"",yearPicker.getValue()+"",page);
                            Number.setText(page+"");
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearPickerDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}