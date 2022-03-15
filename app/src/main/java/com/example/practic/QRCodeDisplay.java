package com.example.practic;

public class QRCodeDisplay {
    private String title, time, date, place, workplace, locker;
    private int img;

    public QRCodeDisplay(String title, String time, String date, String place, String workplace,
                         String locker, int img) {
        this.title     = title;
        this.time      = time;
        this.date      = date;
        this.place     = place;
        this.workplace = workplace;
        this.locker    = locker;
        this.img       = img;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public String getWorkplace() {
        return workplace;
    }
    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getLocker() {
        return locker;
    }
    public void setLocker(String locker) {
        this.locker = locker;
    }

    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }
}
