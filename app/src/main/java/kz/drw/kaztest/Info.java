package kz.drw.kaztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyRequest;


public class Info extends Fragment {

        TextView tvRegular, tvTheme;
        String regularKZ="", regularRu="";
    public Info() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_regulations, container, false);
        Constants.isTest=false;
        tvRegular = (TextView) view.findViewById(R.id.tvRegular);
        tvRegular.setVisibility(View.GONE);
        tvTheme = (TextView) view.findViewById(R.id.tvTheme);
        tvTheme.setText("Информация");
        Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GetRegular();
            }
        });
        myThread.start();


        return  view;

    }

    private void GetRegular() {
            MyRequest jsonReq = new MyRequest(Request.Method.GET,
                    Constants.GET_INFO, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {

                        try {
                            regularKZ = String.valueOf(response.getString("aboutkz"));
                            regularRu = String.valueOf(response.getString("aboutru"));
                            regularKZ = regularKZ.replace("&nbsp;","");
                            regularKZ = regularKZ.replace("\n\n","\n");
                            regularRu = regularRu.replace("\n\n","\n");
                            regularRu = regularRu.replace("&nbsp;","");
                            regularKZ = String.valueOf(Html.fromHtml(regularKZ));
                            regularRu = String.valueOf(Html.fromHtml(regularRu));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Constants.Hide_ProgressDialog();
                        if(Constants.kaztestLang==true)
                        tvRegular.setText(regularKZ);
                        else  tvRegular.setText(regularRu);
                        tvRegular.setVisibility(View.VISIBLE);
                    }
                }
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
