package com.katalozi.banjaluka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private WebView mWebVIew;
    private ImageView mIvNotificationSettings;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        addListeners();
        setUpWebView();

        if (mSharedPrefs.getLong("time", 0) == 0) {
            setUpTime(Constants.day);
            startService(new Intent(getBaseContext(), NotificationServices.class));
        }

    }


    private void initComponents() {
        mWebVIew = (WebView) findViewById(R.id.webView);
        mIvNotificationSettings = (ImageView) findViewById(R.id.setting);
        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);
    }

    private void addListeners() {
        mIvNotificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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

    private void setUpTime(long time) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong("time", time);
        editor.apply();
    }
}
