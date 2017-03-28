package com.katalozi.banjaluka;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Service which make notifications based on JSON
 */

public class NotificationServices extends Service {

    private Handler handler;
    private Runnable runnable;
    long delay; // milliseconds
    private SharedPreferences mSharedPrefs;

    public NotificationServices() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        makeNotificationObjectRequest();
        mSharedPrefs = getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);
        delay = mSharedPrefs.getLong("time", 0);
        //make new thread and constantly make json object, deley is set by user (day,week,month or never)
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                makeNotificationObjectRequest();
                handler.postDelayed(this, delay);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, delay);
        return START_STICKY;
    }

    // if service is stoped stop running thread
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * Make networking call and parese JSON into notification object, also check if new version of application is available.
     */
    private void makeNotificationObjectRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                Constants.NOTIFICATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject ob = new JSONObject(response);
                    JSONObject notification = ob.getJSONObject("notification");
                    String date = notification.getString("date");
                    String title = notification.getString("title");
                    String message = notification.getString("message");
                    String version = notification.getString("verzija");

                    if (!mSharedPrefs.getString("version", "").equalsIgnoreCase(version)) {
                        SharedPreferences.Editor editor = mSharedPrefs.edit();
                        editor.putBoolean("change", true);
                        editor.putString("version", version);
                        editor.apply();
                    }

                    final NotificationItem notificationItem = new NotificationItem(date, title, message);

                    if (!notificationItem.date.equalsIgnoreCase(mSharedPrefs.getString("date", ""))) {
                        createNotification(notificationItem);
                    }

                    SharedPreferences.Editor editor = mSharedPrefs.edit();
                    editor.putString("date", date);
                    editor.apply();

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

    /**
     * Create new notification
     *
     * @param notificationItem
     */

    public void createNotification(NotificationItem notificationItem) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification notification = new Notification.Builder(this)
                .setContentTitle(notificationItem.title)
                .setContentText(notificationItem.message).setSmallIcon(R.drawable.nova)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }
}
