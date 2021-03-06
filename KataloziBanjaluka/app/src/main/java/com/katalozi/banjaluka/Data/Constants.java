package com.katalozi.banjaluka.data;

import android.app.AlarmManager;

/**
 * Class which holds all constant string in application
 */

public class Constants {

    public static String BASE_URL = "http://\u200Bkatalozi.banjaluka.com?utm_source=BanjalukaCom&utm_medium=AndroidApp&utm_campaign=AndroidAppKatalozi";
    public static final String NOTIFICATION_URL = "http://katalozi.banjaluka.com/notifikacije.php";
    public static final String NAME = "KataloziBanjaluka";
    public static final String FACEBOOK = "https://www.facebook.com/BanjalukaShoping1/";
    public static final String EMAIL = "katalozi@banjaluka.com";
    public static final String PHONE = "051 962 405";
    public static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=com.katalozi.banjaluka";

    public static final int HOUR = 18;
    public static final int MINUTE = 0;
    public static final int SECOND = 0;

    public static final long day = AlarmManager.INTERVAL_DAY;
    public static final long week = day * 7;
    public static final long month = week * 4;
}
