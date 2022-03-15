package com.example.practic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LentaItem {
    private int idPicture;
    private EventData eventData;
    private String tittle;
    private String date;
    private String description;

    public LentaItem(EventData event, String tittle, String date) {
        this.eventData = event;
        this.tittle    = tittle;
        this.date      = date;
    }

    public LentaItem(int idPicture, EventData event, String tittle, String date) {
        this.idPicture = idPicture;
        this.eventData = event;
        this.tittle    = tittle;
        this.date      = date;
    }

    public LentaItem(int idPicture, EventData eventData, String tittle, String date, String description) {
        this.idPicture   = idPicture;
        this.eventData   = eventData;
        this.tittle      = tittle;
        this.date        = date;
        this.description = description;
    }

    public LentaItem(EventData event) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        this.eventData   = event;
        this.tittle      = event.getTitle();
        this.date        = dateFormat.format(event.getMeetingDate());
        this.description = event.getDescription();
    }

    public LentaItem(int idPicture, EventData event) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        this.idPicture   = idPicture;
        this.eventData   = event;
        this.tittle      = event.getTitle();
        this.date        = dateFormat.format(event.getMeetingDate());
        this.description = event.getDescription();
    }

    public int getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(int idPicture) {
        this.idPicture = idPicture;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }
}
