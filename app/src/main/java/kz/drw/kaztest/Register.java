package kz.drw.kaztest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.MyRequest;

public class Register extends AppCompatActivity {

    ImageButton imgBack;
    ImageView imgMan, imgWomen;
    EditText editSurname, editName, editPatronymic, editPhone, editPassword;
    FrameLayout layBirth;
    Switch switchAgree;
    TextView editDay, editMonth, editYear, tvRule;
    String School = "";
    int myYear = 2000, myMonth=0, myDay = 1;
    int DIALOG_DATE = 1;
    Button btnCity, btnSchool, btnRegister;
    int idCity=-1, idSchool=-1, isAgree=0, isSex=0;
    String[] city_names, school_names;
    String getNumber, regularKZ, regularRu;
    Integer [] city_ids, school_ids;
    static  String  patr="", sex="";
    int old=0;
    Boolean isSelectedBirth=false;
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        GetRules();
        Constants.Show_ProgressDialog(Register.this, getResources().getString(R.string.wait));
        initResources();
        GetCity();
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idSchool=-1;
//                btnSchool.setText("Школа");
                GetCitiesArray();
            }
        });
        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectedBirth){
                    if(idCity==-1) Toast.makeText(Register.this, getResources().getString(R.string.setCity), Toast.LENGTH_SHORT).show();
                    else
                        GetSchools();

                }
                else Toast.makeText(Register.this, "Сначало введите день рождения", Toast.LENGTH_SHORT).show();

            }
        });
        layBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog tpd = new DatePickerDialog(Register.this, myCallBack, 2000, 0, 1);
                tpd.show();
            }
        });
        editDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog tpd = new DatePickerDialog(Register.this, myCallBack, myYear, myMonth, myDay);
                tpd.show();
            }
        });
        editMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog tpd = new DatePickerDialog(Register.this, myCallBack, myYear, myMonth, myDay);
                tpd.show();
            }
        });
        editYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog tpd = new DatePickerDialog(Register.this, myCallBack, myYear, myMonth, myDay);
                tpd.show();
            }
        });

        tvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Register.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_regulations);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tvInfo = (TextView) dialog.findViewById(R.id.tvRegular);
                ImageView imgExit = (ImageView) dialog.findViewById(R.id.imgExit);
                imgExit.setVisibility(View.VISIBLE);
                tvInfo.setText(regularKZ);
                dialog.show();
                imgExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constants.isEmpty(editSurname)) {
                    if (!Constants.isEmpty(editName)) {
                        if (isSelectedBirth) {
                            if (idCity > -1) {
                                if (isSex != 0) {
                                    if (!Constants.isEmpty(editPhone)) {
                                         getNumber = editPhone.getText().toString();
                                        getNumber=getNumber.replace(" ","");
                                        if (getNumber.length() == 14) {
                                            if (!Constants.isEmpty(editPassword)) {
                                                if (isAgree > 0) {
                                                    Constants.Show_ProgressDialog(Register.this, getResources().getString(R.string.wait));
                                                    setRegister();
                                                } else
                                                    Toast.makeText(Register.this, getResources().getString(R.string.errorAgreement), Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(Register.this, getResources().getString(R.string.fillPass), Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(Register.this, getResources().getString(R.string.wrongNumberPhone), Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(Register.this, getResources().getString(R.string.fillNumberPhone), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(Register.this, getResources().getString(R.string.selectSex), Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(Register.this, getResources().getString(R.string.setCity2), Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(Register.this, getResources().getString(R.string.setBirth), Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(Register.this, getResources().getString(R.string.setName), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(Register.this, getResources().getString(R.string.setSurname), Toast.LENGTH_SHORT).show();

            }
        });


        switchAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isAgree=1;
                }else{
                    isAgree=0;
                }
            }
        });
        editPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length()-editPhone.getSelectionStart();
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
                        editPhone.setText(ans);
                        editPhone.setSelection(editPhone.getText().length()-cursorComplement);
                    }
                    else if (phone.length() == 6 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-";
                        editPhone.setText(ans);
                        editPhone.setSelection(editPhone.getText().length()-cursorComplement);
                    }

                    else if (phone.length() == 8 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-"+phone.substring(6,8) + "-";
                        editPhone.setText(ans);
                        editPhone.setSelection(editPhone.getText().length()-cursorComplement);
                    }

                    else if (phone.length() >= 10 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-"+phone.substring(6,8) + "-"+phone.substring(8,10);
                        editPhone.setText(ans);
                        editPhone.setSelection(editPhone.getText().length()-cursorComplement);
                    }
                } else {
                    editedFlag = false;
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSex=1;
                sex="true";
                imgMan.setImageDrawable(getResources().getDrawable(R.drawable.check));
                 imgWomen.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
            }
        });
        imgWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSex=2;
                sex="false";
                imgWomen.setImageDrawable(getResources().getDrawable(R.drawable.check));
                  imgMan.setImageDrawable(getResources().getDrawable(R.drawable.circle_blue));
            }
        });
}

    private void GetRules() {
        MyRequest jsonReq = new MyRequest(Request.Method.GET,
                Constants.GET_INFO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {

                    try {
                        regularKZ = String.valueOf(response.getString("pravilakz"));
                        regularRu = String.valueOf(response.getString("pravilaru"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    regularKZ = String.valueOf(Html.fromHtml(regularKZ)+"");
                    regularRu = String.valueOf(Html.fromHtml(regularRu)+"");
                    regularKZ = regularKZ.replace("&nbsp;","");
                    regularKZ = regularKZ.replace("\n\n","\n");
                    regularRu = regularRu.replace("\n\n","\n");
                    regularRu = regularRu.replace("&nbsp;","");


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
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear+1;
            myDay = dayOfMonth;
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            String day="",mon="";
            if(myDay<10)  day="0";
            if(myMonth<10)  mon="0";
            isSelectedBirth=true;
            editDay.setText(day+myDay+"");
            editMonth.setText(mon+myMonth+"");
            editYear.setText(myYear+"");
            DIALOG_DATE=2;

            DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
            String date2 = dff.format(Calendar.getInstance().getTime());
            String[] dates = date2.split("-");
            year = Integer.parseInt(dates[2]);
            if(isSelectedBirth){
                     old = year - myYear;
                    if(old<=17) {
                        btnSchool.setTextColor(getResources().getColor(R.color.colorPrimary));
                        btnSchool.setFocusable(true);
                        btnSchool.setEnabled(true);
                        }
                    else {
                        btnSchool.setTextColor(getResources().getColor(R.color.colorWhiter));
                        btnSchool.setFocusable(false);
                        btnSchool.setText(getResources().getString(R.string.school));
                        btnSchool.setEnabled(false);
                    }

                }


        }
    };
    private void GetSchools() {
        Constants.Show_ProgressDialog(Register.this, getResources().getString(R.string.wait));
        JsonArrayRequest req = new JsonArrayRequest(Constants.LIST_SCHOOLS+"id="+(idCity),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            school_names = new String[response.length()];
                            school_ids = new Integer[response.length()];
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject js = (JSONObject) response.get(i);
                                school_names[i] = js.getString("school1");
                                school_ids[i] = js.getInt("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Constants.Hide_ProgressDialog();
                        GetSchoolsArray();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.Hide_ProgressDialog();
                Toast.makeText(Register.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    private void GetSchoolsArray() {
        ArrayAdapter<String> listAdapter;
        final Dialog dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview);
        final ListView listv = (ListView) dialog.findViewById(R.id.list1);
        listAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, android.R.id.text1, school_names);
        listv.setAdapter(listAdapter);
        dialog.show();

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    String txt = ((TextView)view).getText().toString();
                    btnSchool.setText(txt);
                    for(int i=0;i<school_names.length;i++){
                        if(school_names[i].equals(txt))
                            idSchool=school_ids[i];

                    }
                    Log.d("ddd",idSchool+"");
                    dialog.cancel();
                    dialog.dismiss(); }
            }
        });
    }

    private void GetCitiesArray() {
        ArrayAdapter<String> listAdapter;
        final Dialog dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview);
        final ListView listv = (ListView) dialog.findViewById(R.id.list1);
        listAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, android.R.id.text1, city_names);
        listv.setAdapter(listAdapter);
        dialog.show();

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    String txt = ((TextView)view).getText().toString();
                    btnCity.setText(txt);
                    for(int i=0;i<city_names.length;i++){
                        if(city_names[i].equals(txt))
                            idCity=city_ids[i];
                    }
                    dialog.cancel();
                    dialog.dismiss(); }
            }
        });
    }
    private  void GetCity(){
            JsonArrayRequest req = new JsonArrayRequest(Constants.LIST_CITIES,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("ddds", response.toString());

                    try {
                        city_names = new String[response.length()];
                        city_ids = new Integer[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject js = (JSONObject) response.get(i);
                            city_names[i] = js.getString("city1");
                            city_ids[i] = js.getInt("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        Constants.Hide_ProgressDialog();
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Constants.Hide_ProgressDialog();
            Toast.makeText(Register.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    });
    AppController.getInstance().addToRequestQueue(req);
}


    private void setRegister() {
        patr = editPatronymic.getText().toString();
        if(idSchool>0) School = idSchool+"";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("0"))  {
                            Toast.makeText(Register.this,getResources().getString(R.string.errorLoginOrPass), Toast.LENGTH_SHORT).show();
                            Constants.Hide_ProgressDialog();
                        }
                        else if(response.equals("-1"))
                        {
                            Toast.makeText(Register.this,getResources().getString(R.string.userBlocked), Toast.LENGTH_SHORT).show();
                            Constants.Hide_ProgressDialog();
                        }
                        else {
                            Toast.makeText(Register.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//                            SharedPreferences sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedpreferences.edit();
//                            editor.putString("u_id", response+"");
//                            Log.d("ddd", response+"");
//                            editor.putString("password", editPassword.getText().toString());
//                            editor.putString("username",editName.getText().toString());
//                            editor.commit();
                            Constants.Hide_ProgressDialog();
                            startActivity(new Intent(Register.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Constants.Hide_ProgressDialog();
                        Toast.makeText(Register.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Username","7"+getNumber);
                params.put("Password",editPassword.getText().toString());
                params.put("birtdate",myDay+"-"+myMonth+"-"+myYear);
                params.put("cityid",idCity+"");
                if(old<=17)
                params.put("schollid",School+"");
                params.put("FirstName",editName.getText().toString());
                params.put("MiddleName",patr);
                params.put("LastName",editSurname.getText().toString());
                params.put("Gender",sex);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void initResources() {
        this.editDay  = (TextView) findViewById(R.id.editDay);
        this.editSurname  = (EditText) findViewById(R.id.editSurname);
        this.editName  = (EditText) findViewById(R.id.editName);
        this.editPatronymic  = (EditText) findViewById(R.id.editPatronymic);
        this.editMonth  = (TextView) findViewById(R.id.editMonth);
        this.editYear  = (TextView) findViewById(R.id.editYear);
        this.tvRule  = (TextView) findViewById(R.id.tvRule);
        this.editPhone  = (EditText) findViewById(R.id.editPhone);
        this.editPassword  = (EditText) findViewById(R.id.editPassword);

        this.btnCity = (Button) findViewById(R.id.btnCity);
        this.btnSchool = (Button) findViewById(R.id.btnSchool);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);

        this.layBirth = (FrameLayout) findViewById(R.id.frameBirth);

        this.switchAgree = (Switch) findViewById(R.id.switchAgree);
        this.imgBack = (ImageButton) findViewById(R.id.imgBack);
        this.imgMan = (ImageView) findViewById(R.id.imgMan);
        this.imgWomen = (ImageView) findViewById(R.id.imgWomen);
    }


}
