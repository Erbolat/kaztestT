package kz.drw.kaztest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.CircleImageView;
import kz.drw.kaztest.utils.Constants;
import kz.drw.kaztest.utils.LocaleHelper;
import kz.drw.kaztest.utils.MyFirebaseInstanceIDService;
import kz.drw.kaztest.utils.MyRequest;
import kz.drw.kaztest.utils.epay.EpayActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public  static  Toolbar toolbar;
    AppBarLayout appbar;
    SharedPreferences sharedpreferences;
    MenuInflater menuInflater;
    public static String userID="";
    String username="";
    NavigationView navigationView;
    TextView tvExit;
    static  TextView tvName , tvBalance, tvAddBalance;
    static  LinearLayout layBalance;
    CircleImageView avatar;
    View headerView;
    String myLogin="", myPassword="";
    public  static  String[] categ = new String[3];
    Drawable drawable=null;
    static String lastname="",name="", patron="", photo="", balance="";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String[] languages= new String[]{"Қазақша","Русский"};
    public  static  String[] profNames ;
    public  static  String err="";
    public  static Boolean isChanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.setLocale(this);
        err =getResources().getString(R.string.no_priced);
        setContentView(R.layout.activity_main);
        profNames= getResources().getStringArray(R.array.profileStr);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        Constants.isTest=false;
        setSupportActionBar(toolbar);
        categ[0] = getResources().getString(R.string.gosTest2);categ[1] = getResources().getString(R.string.entTest);categ[2] = getResources().getString(R.string.pddTest);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        AuthorizedOrNonAuthorized();
//        if(!isChanged)
//        {drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_russia);
//        toolbar.setOverflowIcon(drawable);
//        }
//        else {
//            if(language.equals("ru")) drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_russia);
//            else drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_kazakhstan);
//            toolbar.setOverflowIcon(drawable);
//        }

    }


    private  void AuthorizedOrNonAuthorized(){
        tvName= (TextView) headerView.findViewById(R.id.tvName);
        avatar = (CircleImageView) headerView.findViewById(R.id.prof);
        tvBalance = (TextView) headerView.findViewById(R.id.tvBalance);
        tvAddBalance = (TextView) headerView.findViewById(R.id.tvAddBalance);
        layBalance = (LinearLayout) headerView.findViewById(R.id.layBalance);
        ImageView imgWrite = (ImageView) headerView.findViewById(R.id.imgWrite);
//        tvExit = (TextView) headerView.findViewById(R.id.tvExit);
//        tvExit.setVisibility(View.INVISIBLE);
        layBalance.setVisibility(View.INVISIBLE);
         imgWrite.setVisibility(View.INVISIBLE);
        sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("u_id","")!=null) {
            if(!sharedpreferences.getString("u_id","").equals("")) {
                username = sharedpreferences.getString("username", "");
                userID = sharedpreferences.getString("u_id", "");
                navigationView.getMenu().findItem(R.id.exit).setVisible(true);
                myLogin = sharedpreferences.getString("login","");
                myPassword = sharedpreferences.getString("password","");
                imgWrite.setVisibility(View.VISIBLE);
                layBalance.setVisibility(View.VISIBLE);
                if(isOnline()) {
                    GetRatingGos2();
                    Thread myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseApp.initializeApp(MainActivity.this);
                            GetProfile();
                        }
                    });
                    myThread.start();
                    Thread myThread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setLogin();
                        }
                    });
                    myThread2.start();
                }
                else {
                    layBalance.setVisibility(View.INVISIBLE);
                    imgWrite.setVisibility(View.INVISIBLE);
                    navigationView.getMenu().findItem(R.id.exit).setVisible(false);
                }
                layBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayAdapter<String> listAdapter;
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.priced);
                        Button btnCont = (Button) dialog.findViewById(R.id.btnContinue);
                        final EditText edit = (EditText) dialog.findViewById(R.id.editSum);
                        final LinearLayout laySum = (LinearLayout) dialog.findViewById(R.id.laySum);
                        final WebView web = (WebView) dialog.findViewById(R.id.web);
                        dialog.show();
                        btnCont.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(!edit.getText().toString().equals("")) {
                                    if(Double.parseDouble(edit.getText().toString())>=200){
                                        GetBASE64(edit.getText().toString());
//                        startActivity(new Intent(getActivity().getApplicationContext(), Oplata.class).putExtra("amount",edit.getText().toString()));
                                        dialog.cancel();
                                        dialog.dismiss();}
                                    else Toast.makeText(MainActivity.this, getResources().getString(R.string.minBalanceError), Toast.LENGTH_SHORT).show();
                                } else Toast.makeText(MainActivity.this, getResources().getString(R.string.fillEmpty), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else {layBalance.setVisibility(View.INVISIBLE);
                navigationView.getMenu().findItem(R.id.exit).setVisible(false);}
        }
        else {
            tvName.setText(getResources().getString(R.string.enter));
            layBalance.setVisibility(View.INVISIBLE);;
            userID="";  navigationView.getMenu().findItem(R.id.exit).setVisible(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                avatar.setBackground(getResources().getDrawable(R.drawable.prof));
            }
        }
        headerView.findViewById(R.id.layProf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userID.equals("")){
                    if(isOnline()) {
                        Fragment fragment = new MyProfile();
                        String title = "Профиль";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            toolbar.setElevation(0);
                            appbar.setElevation(0);
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                        getSupportActionBar().setTitle("");
                        getSupportActionBar().setTitle(title);
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }else Toast.makeText(MainActivity.this, getResources().getString(R.string.connectInet), Toast.LENGTH_SHORT).show();
                }
                else {
                    userID="";
                    layBalance.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(MainActivity.this, Login.class));
                }
            }
        });

//        if(!userID.equals("")) {

//            tvExit.setVisibility(View.VISIBLE);
//            tvExit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(isOnline()){
//                    sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.clear().commit();
//                    lastname="";name="";patron="";userID="";photo="";
//                    tvExit.setVisibility(View.INVISIBLE);
//                    Thread myThread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            DELETE_PUSH();
//                        }
//                    });
//                    myThread.start(); }
//                    else Toast.makeText(MainActivity.this, getResources().getString(R.string.connectInet), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
//        else {
//            layBalance.setVisibility(View.GONE);
//            tvName.setText(getResources().getString(R.string.enter));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                avatar.setBackground(getResources().getDrawable(R.drawable.prof));
//            }
//        }
   }

    private void GetBASE64(final String sum) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kaztest.com/Mobileoplata/Sign?amount="+sum+"&userid="+MainActivity.userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("bbbb",response);
                        startActivity(new Intent(MainActivity.this, EpayActivity.class).putExtra("base64",response));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        });
        queue.add(stringRequest);

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
    private void DELETE_PUSH() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.DELETE_PUSH+userID+"&device="+FirebaseInstanceId.getInstance().getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle("");
        getSupportActionBar().setTitle(title);
    }
    public  void setName(String title){
        patron="";
        if(!title.equals("")) {
        title = title.replaceAll("  "," ");
        String[] titles = title.split(" ");
            if(titles.length>0) {
                if(titles.length==1) {
                    lastname=titles[0];
                }
                if(titles.length==2) {
                    lastname=titles[0];
                    name=titles[1];
                }
                if(titles.length==3) {
                    lastname=titles[0];
                    name=titles[1];
                    patron=titles[2];
                }
            }
            else lastname = title;
            if(!patron.equals(""))
                tvName.setText(lastname+"\n"+name+"\n"+patron+"");
            else  tvName.setText(lastname+"\n"+name);
        }
    }
//    public  void setAvatar(String photos){
////        Log.e("sfdsf","fdsfdsfdsf");
////        Thread myThread1 = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                GetProfileRes();
////            }
////        });
////        myThread1.start();
//
//
//
//    }
    @Override
    public void onBackPressed() {
        Constants.isTest=false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void GetProfileRes() {
        MyRequest jsonReq = new MyRequest(Request.Method.GET,
                Constants.PROFILE+userID, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        photo = response.getString("photo");
                        if(!photo.equals("")) {
                            photo = photo.replace("~","");
                            photo = "http://www.kaztest.com"+photo;
                            avatar.setImageUrl(photo,imageLoader);
                            Log.d("ffff","1111112");
                            Constants.isChangedImage=0;
//                            avatar.setImageUrl("http://www.kaztest.com"+photo, imageLoader);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    private void GetProfile() {
        MyRequest jsonReq = new MyRequest(Request.Method.GET,
                Constants.PROFILE+userID, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if(!response.getString("LastName").equals("null"))
                            lastname = response.getString("LastName");
                        if(!response.getString("FirstName").equals("null"))
                            name = response.getString("FirstName");
                        if(!response.getString("MiddleName").equals("null"))
                            patron = response.getString("MiddleName");
                        if(!response.getString("Balance").equals("null"))
                            balance = response.getString("Balance");
                        if(!response.getString("photo").equals("null"))
                            photo = response.getString("photo");
                        if(!photo.equals("")) {
                            photo = photo.replace("~","");
                            avatar.setBackground(null);
                            avatar.setImageUrl("http://www.kaztest.com"+photo, imageLoader);
                        }
                        if(!patron.equals(""))
                        tvName.setText(lastname+"\n"+name+"\n"+patron+"");
                        else  tvName.setText(lastname+"\n"+name);
                        if(!balance.equals("")) tvBalance.setText("Баланс: "+balance+" тг");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;

    }
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                MainActivity.this.getSystemService(cs);

        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return  true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_lang) {
            if(!Constants.isTest)

            ChangeLang();
            else Toast.makeText(this, getResources().getString(R.string.disabled), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment=null;
        String title = getString(R.string.app_name);
        int id = item.getItemId();

        if (id == R.id.tests) {
            fragment = new TestMainSelect();
            title = getResources().getString(R.string.tests);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
                appbar.setElevation(0);
            }
        } else if (id == R.id.regular) {
            fragment = new Regulations();
            title = getResources().getString(R.string.regular);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
                appbar.setElevation(0);
            }

        } else if (id == R.id.info) {
            fragment = new Info();
            title = getResources().getString(R.string.info);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
                appbar.setElevation(0);
            }
        }
        else if (id == R.id.exit) {
            if(isOnline()){
                sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().commit();
                lastname="";name="";patron="";userID="";photo="";
                navigationView.getMenu().findItem(R.id.exit).setVisible(false);
                Thread myThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DELETE_PUSH();
                    }
                });
                myThread.start(); }
            else Toast.makeText(MainActivity.this, getResources().getString(R.string.connectInet), Toast.LENGTH_SHORT).show();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void ChangeLang() {
        ArrayAdapter<String> listAdapter;
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview);
        final ListView listv = (ListView) dialog.findViewById(R.id.list1);
        listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, languages);
        listv.setAdapter(listAdapter);
        dialog.show();

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    String txt = ((TextView)view).getText().toString();
                    for(int i=0;i<languages.length;i++){
                        if(languages[i].equals(txt)) {
                            if(i==0) {
                                Constants.kaztestLang=true; Constants.language="kk"; isChanged = true;
                                SharedPreferences sharedpreferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("language", "true");
                                editor.commit();
                                AppController.setLocale(MainActivity.this);
                                restartActivity();
                              }
                            else   {
                                Constants.kaztestLang=false;  Constants.language="ru"; isChanged=true;
                                SharedPreferences sharedpreferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("language", "false");
                                editor.commit();
                                AppController.setLocale(MainActivity.this);
                                restartActivity();
                            }
                            finish();
                        }
                    }
                    dialog.cancel();
                    dialog.dismiss(); }
            }
        });
    }
    private void restartActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private  void setLogin(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("0"))  {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.errorLoginOrPass), Toast.LENGTH_SHORT).show();
                            sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear().commit();
                            DELETE_PUSH();
                        }
                        else if(response.equals("-1"))
                        {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.userBlocked), Toast.LENGTH_SHORT).show();
                            sharedpreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear().commit();
                            DELETE_PUSH();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",myLogin);
                params.put("password",myPassword);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(Constants.isChangedImage==1)GetProfileRes();

        if(Profile.isBacked) {
            Log.e("ffffss","112323");
            GetProfile();
            Profile.isBacked   =false;}

    }

}
