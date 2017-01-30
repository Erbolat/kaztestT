package kz.drw.kaztest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        web.getSettings().setJavaScriptEnabled(true);
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

}
 class SSLTolerentWebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); // Ignore SSL certificate errors
    }


}