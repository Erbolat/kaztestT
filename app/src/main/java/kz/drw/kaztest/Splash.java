package kz.drw.kaztest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import kz.drw.kaztest.utils.AppController;
import kz.drw.kaztest.utils.Constants;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppController.setLocale(this);
        if(!isOnline())  Toast.makeText(Splash.this, getResources().getString(R.string.connectInet), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, 1200);

    }
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                Splash.this.getSystemService(cs);

        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return  true;
        }
    }
}
