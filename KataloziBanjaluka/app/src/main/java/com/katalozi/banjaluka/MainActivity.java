package com.katalozi.banjaluka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private WebView mWebVIew;
    private ImageView mIvNotificationSettings;
    private SharedPreferences mSharedPrefs;

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

        if (mSharedPrefs.getLong("time", 0) == 0) {
            setUpTime(Constants.day);
            startService(new Intent(getBaseContext(), NotificationServices.class));
        }

    }


    private void initComponents() {
        mWebVIew = (WebView) findViewById(R.id.webView);
        mIvNotificationSettings = (ImageView) findViewById(R.id.setting);
        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);

        //   if(mSharedPrefs.getBoolean("change",false)) {
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.show(getSupportFragmentManager(), "sd");
        //  }
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

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showAlert(isConnected);
    }

    // Showing the status in Snackbar
    private void showAlert(boolean isConnected) {
        if (isConnected) {
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Upozorenje:")
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
        showAlert(isConnected);
    }
}
