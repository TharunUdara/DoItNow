package com.example.doitnow;

import java.util.Locale;

/**
 * Model class representing a single Task in the application.
 * Stores task details like name, category, time, and priority status.
 */
public class Task {
    private String name, category;
    private int hour, minute;
    private boolean isDone, isFocus;

    /**
     * Constructor for a new Task.
     * @param name The title of the task.
     * @param hour Hour in 24-hour format.
     * @param minute Minute of the task time.
     * @param category Task category (e.g., Work, Personal).
     * @param isFocus Whether the task belongs to the FOCUS section.
     */
    public Task(String name, int hour, int minute, String category, boolean isFocus) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.category = category;
        this.isFocus = isFocus;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }
    public boolean isFocus() { return isFocus; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }

    /**
     * Converts the 24-hour time to a user-friendly AM/PM string.
     * @return Formatted time string (e.g., "9:30 AM").
     */
    public String getTimeFormatted() {
        int h = hour % 12; if (h == 0) h = 12;
        return String.format(Locale.getDefault(), "%d:%02d %s", h, minute, hour < 12 ? "AM" : "PM");
    }
}