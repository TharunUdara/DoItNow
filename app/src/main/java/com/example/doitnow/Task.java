package com.example.doitnow;

import java.util.Locale;

public class Task {
    private String name, category;
    private int hour, minute;
    private boolean isDone, isFocus;

    public Task(String name, int hour, int minute, String category, boolean isFocus) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.category = category;
        this.isFocus = isFocus;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }
    public boolean isFocus() { return isFocus; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }

    public String getTimeFormatted() {
        int h = hour % 12; if (h == 0) h = 12;
        return String.format(Locale.getDefault(), "%d:%02d %s", h, minute, hour < 12 ? "AM" : "PM");
    }
}