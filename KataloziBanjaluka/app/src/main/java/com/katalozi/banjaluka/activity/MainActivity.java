package com.katalozi.banjaluka.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.katalozi.banjaluka.reciever.ConnectivityReceiver;
import com.katalozi.banjaluka.fragment.MessageFragment;
import com.katalozi.banjaluka.MyApplication;
import com.katalozi.banjaluka.service.NotificationServices;
import com.katalozi.banjaluka.R;
import com.katalozi.banjaluka.data.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The MainActivity is main class in this application. There is load WebView and shown all messages to users.
 */

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private WebView mWebVIew;
    private ImageView mIvNotificationSettings;
    private SharedPreferences mSharedPrefs;
    private Handler handler;
    private Runnable runnable;
    long delay = 30000; // milliseconds

    // spplication will working if orientation of screen is changed
    @Override
    public void onConfigurationChanged(Configuration config) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        initComponents();
        addListeners();
        setUpWebView();

        // if application is running for the fist time or do not have set time for notification set it on daily.
        if (mSharedPrefs.getLong("time", 0) == 0) {
            setUpTime(Constants.day);
            startService(new Intent(getBaseContext(), NotificationServices.class));
        }

    }

    /**
     * init of all View components in this class and check of version saved in memory, if version changed show alert.
     */
    private void initComponents() {
        mWebVIew = (WebView) findViewById(R.id.webView);
        mIvNotificationSettings = (ImageView) findViewById(R.id.setting);
        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);
        //check version of application
        if (mSharedPrefs.getBoolean("change", false)) {
            MessageFragment messageFragment = new MessageFragment();
            messageFragment.show(getSupportFragmentManager(), "katalog");
        }
    }

    /**
     * On click listeners on view components
     */
    private void addListeners() {
        mIvNotificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }

    /**
     * seting up webView and webClient into application
     */
    private void setUpWebView() {
        WebSettings settings = mWebVIew.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(true);
        settings.setBuiltInZoomControls(true);
        mWebVIew.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        mWebVIew.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                handler.removeCallbacks(runnable);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                handler = new Handler();
                runnable = new Runnable() {
                    public void run() {
                        handler.postDelayed(this, delay);
                        Toast.makeText(getApplicationContext(),"Brzina Vaseg interneta nije odgovarajuca za koriscenje ove " +
                                "aplikacije",Toast.LENGTH_SHORT).show();
                        handler.removeCallbacks(this);
                    }
                };
                handler.postDelayed(runnable, delay);
            }
        });

        mWebVIew.loadUrl(Constants.BASE_URL);

    }

    // if we click on webView and open new page, backbutton will trun us back.
    @Override
    public void onBackPressed() {
        if (mWebVIew.canGoBack()) {
            mWebVIew.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * set time for notifications
     *
     * @param time
     */
    private void setUpTime(long time) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong("time", time);
        editor.apply();
    }

    /**
     * Check network state - on/off
     */
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showAlert(isConnected);
    }

    /**
     * Show alert in case of lose internet
     *
     * @param isConnected
     */
    private void showAlert(boolean isConnected) {
        if (isConnected) {
            makeVersionRequest();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Upozorenje:")
                    .setCancelable(false)
                    .setIcon(R.mipmap.ikonica)
                    .setMessage("Vaš telefon trenutno nije povezan na Internet. Da biste koristili ovu aplikaciju potrebeno je da budete povezani na Internet.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        try {
            showAlert(isConnected);
        } catch (Exception e) {

        }

    }
    /**
     * Check if new version of application is available.
     */
    private void makeVersionRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.NOTIFICATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject ob = new JSONObject(response);
                    JSONObject notification = ob.getJSONObject("notification");
                    String version = notification.getString("verzija");

                    if (!mSharedPrefs.getString("version", "").equalsIgnoreCase(version)) {
                        SharedPreferences.Editor editor = mSharedPrefs.edit();
                        editor.putBoolean("change", true);
                        editor.putString("version", version);
                        editor.apply();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjReq);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
