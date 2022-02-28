package com.example.practic;

import java.sql.Date;

public class Event {
    private int id;
    private String title;
    private String theme;
    private Date meetingDate;


    public Event(int id, String title, String theme, Date meetingDate) {
        this.id = id;
        this.title = title;
        this.theme = theme;
        this.meetingDate = meetingDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTheme() {
        return theme;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }
}
