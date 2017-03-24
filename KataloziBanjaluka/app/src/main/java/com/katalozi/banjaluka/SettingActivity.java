package com.katalozi.banjaluka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private TextView day, week, month, never;
    private SharedPreferences mSharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initComponents();
        addListeners();
    }

    private void initComponents() {
        day = (TextView)findViewById(R.id.day);
        week = (TextView)findViewById(R.id.week);
        month = (TextView)findViewById(R.id.month);
        never = (TextView)findViewById(R.id.never);

        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);
    }

    private void addListeners() {
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(86400000);
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(604800000);
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime(10000);
                stopService(new Intent(getBaseContext(), NotificationServices.class));
                startService(new Intent(getBaseContext(), NotificationServices.class));
            }
        });
        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getBaseContext(), NotificationServices.class));
            }
        });
    }
    private void setUpTime(long time) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong("time",time);
        editor.apply();
    }
}
