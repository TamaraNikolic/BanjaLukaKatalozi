package com.katalozi.banjaluka;

/**
 * Created by tamara.nikolic on 3/24/2017.
 */

public class NotificationItem {
    public String date, title, message;

    public NotificationItem(String date, String title, String message) {
        this.date = date;
        this.title = title;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Date "+date+" title "+title + " message "+message;
    }
}
