package kz.drw.kaztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

        TextView tvRegular,tvTheme;
        String regular="", regularRu="";
        WebView web;
    public Info() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_regulations2, container, false);
        Constants.isTest=false;
        web = (WebView) view.findViewById(R.id.web);
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
                            if(Constants.kaztestLang)
                            regular = String.valueOf(response.getString("aboutkz"));
                            else
                            regular = String.valueOf(response.getString("aboutru"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        regular = regular.replace("\n\n","\n");
                        regular = regular.replace("&nbsp;","");
                        web.loadDataWithBaseURL(null, regular,"text/html", "UTF-8", null);
                        Constants.Hide_ProgressDialog();
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
