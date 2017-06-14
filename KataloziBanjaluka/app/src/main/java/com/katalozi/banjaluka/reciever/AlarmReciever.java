package com.katalozi.banjaluka.reciever;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AlarmReciever extends BroadcastReceiver {

    private static final String TAG = AlarmReciever.class.getName();
    private SharedPreferences mSharedPrefs;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        makeNotificationObjectRequest(context);

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

                    mSharedPrefs = context.getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);

                    if (!mSharedPrefs.getString("version", "").equalsIgnoreCase(version)) {
                        SharedPreferences.Editor editor = mSharedPrefs.edit();
                        editor.putBoolean("change", true);
                        editor.putString("version", version);
                        editor.apply();
                    }

                    final NotificationItem notificationItem = new NotificationItem(date, title, message);

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
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification notification = new Notification.Builder(context)
                .setContentTitle(notificationItem.title)
                .setContentText(notificationItem.message).setSmallIcon(R.drawable.nova)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }

    public void setRecordingPhoneNotification(Context context, long duration) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Constants.HOUR);
        calendar.set(Calendar.MINUTE, Constants.MINUTE);
        calendar.set(Calendar.SECOND, Constants.SECOND);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long notificationDuration = duration;
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notificationDuration, pendingIntent);
    }

    public void canceRecordingPhonelNotification(Context context) {
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
