package kz.drw.kaztest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyFirebaseInstanceIDService;

public class Login extends AppCompatActivity {

    EditText editPass, editLogin;
    Button btnEnter;
    ImageView btnBack;
    TextView tvForgot, tvRegister,tvEnter;
    Boolean isForgot = false;
    static  int userID;

    static  String phone = "",number="", password="", username="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(Login.this);
        isForgot=false;
        editPass = (EditText) findViewById(R.id.editPass);
        editLogin = (EditText) findViewById(R.id.editLogin);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvEnter = (TextView) findViewById(R.id.tvEnter);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        btnBack = (ImageView) findViewById(R.id.imgBack);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvForgot.setVisibility(View.GONE);
                editPass.setVisibility(View.GONE);
                tvEnter.setVisibility(View.VISIBLE);
                btnEnter.setText(getResources().getString(R.string.repairPass));
                isForgot = true;
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(Login.this, Register.class));

            }
        });

        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEnter.setText(getResources().getString(R.string.enter));
                tvForgot.setVisibility(View.VISIBLE);
                editPass.setVisibility(View.VISIBLE);
                tvEnter.setVisibility(View.GONE);
                isForgot=false;

            }
        });
        editPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isForgot) {
                    if(!Constants.isEmpty(editLogin)) {
                        if(!Constants.isEmpty(editPass)) {
                            password = editPass.getText().toString();
                            username = editLogin.getText().toString();
                            username=username.replace(" ","");
                            if(username.length()==14){
                            Constants.Show_ProgressDialog(Login.this, getResources().getString(R.string.wait));
                            setLogin();}
                            else Toast.makeText(Login.this, getResources().getString(R.string.wrongNumberPhone), Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(Login.this, getResources().getString(R.string.fillPass), Toast.LENGTH_SHORT).show();
                    }
                else Toast.makeText(Login.this, getResources().getString(R.string.fillNumberPhone), Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!Constants.isEmpty(editLogin)) {
                            username = editLogin.getText().toString();
                        username=username.replace(" ","");
                        if(username.length()==14){
                            Constants.Show_ProgressDialog(Login.this, getResources().getString(R.string.wait));
                            setForgot();}
                            else Toast.makeText(Login.this, getResources().getString(R.string.wrongNumberPhone), Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(Login.this, getResources().getString(R.string.fillNumberPhone), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });
        editLogin.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length()-editLogin.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");
                if (!editedFlag) {
                    if (phone.length() == 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        editLogin.setText(ans);
                        editLogin.setSelection(editLogin.getText().length()-cursorComplement);
                    }
                    else if (phone.length() == 6 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-";
                        editLogin.setText(ans);
                        editLogin.setSelection(editLogin.getText().length()-cursorComplement);
                    }

                    else if (phone.length() == 8 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-"+phone.substring(6,8) + "-";
                        editLogin.setText(ans);
                        editLogin.setSelection(editLogin.getText().length()-cursorComplement);
                    }

                    else if (phone.length() >= 10 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-"+phone.substring(6,8) + "-"+phone.substring(8,10);
                        editLogin.setText(ans);
                        editLogin.setSelection(editLogin.getText().length()-cursorComplement);
                    }

                } else {
                    editedFlag = false;
                }
            }
        });
}
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setForgot() {
        Log.d("!!username","7"+username);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.REPAIR_PASS+"7"+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("0"))  {
                            Toast.makeText(Login.this, getResources().getString(R.string.SMS), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,Login.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            finish();
                            Constants.Hide_ProgressDialog();
                        }
                        else
                        {
                            Toast.makeText(Login.this, getResources().getString(R.string.unregisterNumberPhone), Toast.LENGTH_SHORT).show();
                            Constants.Hide_ProgressDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(Login.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","7"+username);
                params.put("password",password);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private  void setLogin(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("0"))  {
                            Toast.makeText(Login.this, getResources().getString(R.string.errorLoginOrPass), Toast.LENGTH_SHORT).show();
                            Constants.Hide_ProgressDialog();
                        }
                        else if(response.equals("-1"))
                        {
                            Toast.makeText(Login.this, getResources().getString(R.string.userBlocked), Toast.LENGTH_SHORT).show();
                            Constants.Hide_ProgressDialog();
                        }
                      else {
                            userID = Integer.parseInt(response);
                            Toast.makeText(Login.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("u_id", response+"");
                            editor.putString("password", password);
                            editor.putString("username","+"+username);
                            editor.putString("login","7"+username);
                            editor.putString("devic",FirebaseInstanceId.getInstance().getToken()+"");
                            editor.commit();
                            ADD_PUSH();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(Login.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","7"+username);
                params.put("password",password);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void ADD_PUSH() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constants.Hide_ProgressDialog();
                        startActivity(new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(Login.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userid",userID+"");
                params.put("type","false");
                params.put("device1", FirebaseInstanceId.getInstance().getToken()+"");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


}
