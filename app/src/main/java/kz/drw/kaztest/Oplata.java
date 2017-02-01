package kz.drw.kaztest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import kz.drw.kaztest.utils.AppController;

public class Oplata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oplata);
        WebView web = (WebView) findViewById(R.id.web);
        web.setPadding(0, 0, 0, 0);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        Log.e("aaaa11","http://kaztest.com/Mobileoplata/Index?amount="+getIntent().getStringExtra("amount")+"&userid="+MainActivity.userID);
        web.loadUrl("http://kaztest.com/Mobileoplata/Index?amount="+getIntent().getStringExtra("amount")+"&userid="+MainActivity.userID);
        web.setWebViewClient(
                new SSLTolerentWebViewClient()
        );


    }
    @Override
    public void onBackPressed() {
        finish();
       Profile.isBacked=true;
    }
    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(30);
        val = val * 100d;
        return val.intValue();
    }

}
 class SSLTolerentWebViewClient extends WebViewClient {

//    @Override
//    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        handler.proceed(); // Ignore SSL certificate errors
//    }
     @Override
     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
         super.onReceivedError(view, errorCode, description, failingUrl);
     }


}