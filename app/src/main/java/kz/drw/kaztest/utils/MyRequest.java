package kz.drw.kaztest.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MyRequest extends JsonRequest<JSONObject> {
    public MyRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest == null?null:jsonRequest.toString(), listener, errorListener);
    }

    public MyRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(jsonRequest == null?0:1, url, jsonRequest, listener, errorListener);
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String utf8String = new String(response.data, "UTF-8");
            return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }
}
