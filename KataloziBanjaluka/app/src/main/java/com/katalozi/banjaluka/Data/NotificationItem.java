package com.katalozi.banjaluka.data;

/**
 * Class which hold notification and all details about it.
 */

public class NotificationItem {

    public String date, title, message;

    /**
     * making new NotificationItem using this constructor.
     * @param date
     * @param title
     * @param message
     */
    public NotificationItem(String date, String title, String message) {
        this.date = date;
        this.title = title;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Date " + date + " title " + title + " message " + message;
    }
}
