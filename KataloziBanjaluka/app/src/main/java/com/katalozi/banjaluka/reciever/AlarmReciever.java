package com.katalozi.banjaluka.reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.katalozi.banjaluka.R;
import com.katalozi.banjaluka.activity.MainActivity;
import com.katalozi.banjaluka.data.Constants;
import com.katalozi.banjaluka.data.NotificationItem;
import com.katalozi.banjaluka.service.NotificationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AlarmReciever extends WakefulBroadcastReceiver {
    private SharedPreferences mSharedPrefs;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
      makeNotificationObjectRequest(context);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        Log.e("tamara","alarm");

        wl.release();
    }

    private void makeNotificationObjectRequest(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
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

                    Log.e("tamara","radi");


                    mSharedPrefs = context.getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);

                    if (!mSharedPrefs.getString("version", "").equalsIgnoreCase(version)) {
                        SharedPreferences.Editor editor = mSharedPrefs.edit();
                        editor.putBoolean("change", true);
                        editor.putString("version", version);
                        editor.apply();
                    }

                    final NotificationItem notificationItem = new NotificationItem(date, title, message);
                    createNotification(notificationItem, context);

                    if (!notificationItem.date.equalsIgnoreCase(mSharedPrefs.getString("date", ""))) {
                        createNotification(notificationItem, context);
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

    public void createNotification(NotificationItem notificationItem, Context context) {
        Log.e("tamara","ovde");
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification notification = new Notification.Builder(context)
                .setContentTitle(notificationItem.title)
                .setContentText(notificationItem.message).setSmallIcon(R.drawable.nova)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }
}
