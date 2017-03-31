package com.katalozi.banjaluka;

import android.app.Application;

import com.katalozi.banjaluka.reciever.ConnectivityReceiver;

/**
 * Created by tamara on 3/27/17.
 * Extend Application class and add method for connectivity listener.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
