package com.katalozi.banjaluka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView day, week, month, never;
    private SharedPreferences mSharedPrefs;

    @Override
    public void onConfigurationChanged(Configuration config) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initComponents();
        addListeners();
    }

    /**
     * Method where all views components are initialised
     */
    private void initComponents() {
        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);

        day = (TextView) findViewById(R.id.day);
        week = (TextView) findViewById(R.id.week);
        month = (TextView) findViewById(R.id.month);
        never = (TextView) findViewById(R.id.never);

        // set color of days - blue one if checked, others are black.
        if (mSharedPrefs.getLong("time", 0) == Constants.day) {
            day.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (mSharedPrefs.getLong("time", 0) == Constants.week) {
            week.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (mSharedPrefs.getLong("time", 0) == Constants.month) {
            month.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (mSharedPrefs.getLong("time", 0) == 1) {
            never.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    /**
     * Method set click listeners on views.
     * Cliked item is colored blue, and others in black, also clicked values are saved in sharedprefs in miliseconds
     * Also in listeners we stop running service and start new one with new value of time for norification
     * Finish this activity after click.
     */
    private void addListeners() {
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(Constants.day);
                day.setTextColor(getResources().getColor(R.color.colorPrimary));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.black));
                never.setTextColor(getResources().getColor(R.color.black));
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
                finish();
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(Constants.week);
                week.setTextColor(getResources().getColor(R.color.colorPrimary));
                day.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.black));
                never.setTextColor(getResources().getColor(R.color.black));
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
                finish();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(Constants.month);
                month.setTextColor(getResources().getColor(R.color.colorPrimary));
                week.setTextColor(getResources().getColor(R.color.black));
                day.setTextColor(getResources().getColor(R.color.black));
                never.setTextColor(getResources().getColor(R.color.black));
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
                finish();
            }
        });
        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(1);
                never.setTextColor(getResources().getColor(R.color.colorPrimary));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.black));
                day.setTextColor(getResources().getColor(R.color.black));
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                finish();
            }
        });
    }

    /**
     * Set up time for notification based on time in miliseconds.
     *
     * @param time
     */
    private void setUpTime(long time) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong("time", time);
        editor.apply();
    }
}
