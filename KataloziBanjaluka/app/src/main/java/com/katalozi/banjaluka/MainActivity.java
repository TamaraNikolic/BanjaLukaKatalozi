package com.katalozi.banjaluka;

import android.app.Notification;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private WebView mWebVIew;
    private ImageView mIvNotificationSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        addListeners();
        setUpWebView();

    }


    private void initComponents() {
        mWebVIew = (WebView) findViewById(R.id.webView);
        mIvNotificationSettings = (ImageView)findViewById(R.id.setting);
    }

    private void addListeners() {
        mIvNotificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });
    }

    private void setUpWebView() {
        WebSettings settings = mWebVIew.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebVIew.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        mWebVIew.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //  Log.e(TAG, "Error: " + description);
                Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();

            }
        });
        mWebVIew.loadUrl(Constants.BASE_URL);

    }

    @Override
    public void onBackPressed() {
        if (mWebVIew.canGoBack()) {
            mWebVIew.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
