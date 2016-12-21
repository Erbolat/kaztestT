package kz.drw.kaztest.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

import java.util.Locale;

import kz.drw.kaztest.MainActivity;


public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	LruBitmapCache mLruBitmapCache;

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		setLocale(this);
		mInstance = this;

		LocaleHelper.onCreate(this, Constants.language);
		FirebaseApp.initializeApp(this);
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			getLruBitmapCache();
			mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
		}

		return this.mImageLoader;
	}
	public static void setLocale (Context context){
		MainActivity.isChanged=true;
		SharedPreferences sharedpreferences = context.getSharedPreferences("lang", Context.MODE_PRIVATE);
		if(sharedpreferences.getString("language","")!=null) {
			Log.e("RRRRR", sharedpreferences.getString("language", ""));
			if(sharedpreferences.getString("language","").equals("true")) Constants.language="kk";
			else Constants.language="ru";
		}

		Locale locale = new Locale(Constants.language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getApplicationContext().getResources().updateConfiguration(config, null);

	}
	public LruBitmapCache getLruBitmapCache() {
		if (mLruBitmapCache == null)
			mLruBitmapCache = new LruBitmapCache();
		return this.mLruBitmapCache;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setLocale(this);
	}


	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}



