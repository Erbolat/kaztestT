package kz.drw.kaztest.utils.epay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import kz.drw.kaztest.R;
import kz.drw.kaztest.utils.epay.utils.EpayCallback;
import kz.drw.kaztest.utils.epay.utils.EpayConstants;
import kz.drw.kaztest.utils.epay.utils.EpayLanguage;

/**
 * epay-sdk-android
 * Created by http://beemobile.kz on 3/29/15 4:00 PM.
 */
public class EpayFragment extends Fragment {

    private WebView webView;
    private WebSettings webSettings;

    private EpayCallback successCallback;
    private EpayCallback failureCallback;

    private String postUrl = EpayConstants.EPAY_POST_URL;
    private String signedOrderBase64;
    public static String signedOrderBase64_1;
    private String template;
    private String language;
    private String postLink;
    private String failurePostLink;

    private boolean useTestMode;

    public static EpayFragment newInstance() {
        EpayFragment fragment = new EpayFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_epay, null);

        webView = (WebView) v.findViewById(R.id.webview);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Log.e(EpayConstants.LOG_TAG, "URL loading = " + url);

                        if (EpayConstants.EPAY_BACK_LINK.equals(url)) {
                            successCallback.process(url);

                            return true;
                        } else if (EpayConstants.EPAY_FAILURE_BACK_LINK.equals(url)) {
                            failureCallback.process(url);

                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );

        webView.postUrl(postUrl, buildPostData().getBytes());

        return v;
    }

    private String buildPostData() {
        String postData = "";

        Log.e(EpayConstants.LOG_TAG, "order = " + signedOrderBase64);

        try {
            postData =
                    URLEncoder.encode("Signed_Order_B64", "UTF-8") + "=" + URLEncoder.encode(signedOrderBase64_1, "UTF-8")
                    + "&"
                    + URLEncoder.encode("Language", "UTF-8") + "=" + URLEncoder.encode("rus", "UTF-8")
                    + "&"
                    + URLEncoder.encode("BackLink", "UTF-8") + "=" + URLEncoder.encode("http://kaztest.com/", "UTF-8")
                    + "&"
                    + URLEncoder.encode("FailureBackLink", "UTF-8") + "=" + URLEncoder.encode("http://kaztest.com/Oplata/Error", "UTF-8")
                    + "&"
                    + URLEncoder.encode("PostLink", "UTF-8") + "=" + URLEncoder.encode("http://kaztest.com/Oplata/Oplata", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData;
    }

    public void setSuccessCallback(EpayCallback callback) {
        successCallback = callback;
    }

    public void setFailureCallback(EpayCallback callback) {
        failureCallback = callback;
    }

    public String getSignedOrderBase64() {
        return signedOrderBase64;
    }

    public void setSignedOrderBase64(String signedOrderBase64) {
        this.signedOrderBase64 = signedOrderBase64;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(EpayLanguage lang) {
        this.language = lang.toString();
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public boolean isTestMode() {
        return useTestMode;
    }

    public void useTestMode(boolean useTestMode) {
        postUrl = useTestMode ? EpayConstants.EPAY_TEST_POST_URL : EpayConstants.EPAY_POST_URL;

        this.useTestMode = useTestMode;
    }

    public String getFailurePostLink() {
        return failurePostLink;
    }

    public void setFailurePostLink(String failurePostLink) {
        this.failurePostLink = failurePostLink;
    }
}
