package com.katalozi.banjaluka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tamara on 3/27/17.
 */

public class InternetSpeed extends AsyncTask<String, Void, String> {

    long startTime;
    long endTime;
    private long takenTime;
    private static double speed;
    private static  InternetSpeed internetSpeed;

    @Override
    protected String doInBackground(String... paramVarArgs) {

        startTime = System.currentTimeMillis();

        Bitmap bmp = null;
        try {
            URL ulrn = new URL(paramVarArgs[0]);
            HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);

            Bitmap bitmap = bmp;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, stream);
            byte[] imageInByte = stream.toByteArray();
            long lengthbmp = imageInByte.length;

            if (null != bmp) {
                endTime = System.currentTimeMillis();
                return lengthbmp + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    protected void onPostExecute(String result) {

        if (result != null) {
            long dataSize = Integer.parseInt(result) / 1024;
            takenTime = endTime - startTime;
            double s = (double) takenTime / 1000;
             speed = dataSize / s;
        }
    }
    protected double getSpeed() {
        return speed;
    }

    public static InternetSpeed getInstance() {
        if (internetSpeed == null) {
            internetSpeed = new InternetSpeed();
        }
        return internetSpeed;

    }
}