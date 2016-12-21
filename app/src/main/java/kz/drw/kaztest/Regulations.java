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

import org.json.JSONException;
import org.json.JSONObject;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyRequest;


public class Regulations extends Fragment {

        TextView tvRegular,tvTheme;
        String regular="", regularRu="";
    public Regulations() {
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
        Constants.Show_ProgressDialog(getActivity(),getResources().getString(R.string.wait));
        GetRegular();

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
                            regular = String.valueOf(response.getString("pravilakz"));
                            else
                            regular = String.valueOf(response.getString("pravilaru"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        regular = String.valueOf(Html.fromHtml(regular)+"");
                        regular = regular.replace("&nbsp;","");
                        regular = regular.replace("\n\n","\n");
                        Constants.Hide_ProgressDialog();
                        tvRegular.setText(regular);
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
