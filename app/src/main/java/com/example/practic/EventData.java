package com.example.practic;

import java.sql.Date;

public class EventData {
    private int id;
    private String title;
    private String description;
    private Date meetingDate;
    private String speaker;
    private int spaceId;


    public EventData(int id, String title, String description, Date meetingDate, String speaker, int spaceId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.meetingDate = meetingDate;
        this.speaker = speaker;
        this.spaceId = spaceId;
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

    public int getSpaceId() {
        return spaceId;
    }
}
