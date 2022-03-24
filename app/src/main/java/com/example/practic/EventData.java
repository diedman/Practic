package com.example.practic;

import java.sql.Date;

public class EventData {
    private int id;
    private String title;
    private String description;
    private Date meetingDate;
    private String speaker;
    private CoworkingSpace space;

    public EventData(int id, String title, String description, Date meetingDate, String speaker, CoworkingSpace space) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.meetingDate = meetingDate;
        this.speaker = speaker;
        this.space = space;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public String getSpeaker() {
        return speaker;
    }

    public CoworkingSpace getSpace() { return space; }
}
