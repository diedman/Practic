package com.example.practic;

public class Lenta {
    private int id_picture;
    private String tittle, date, more;

    public Lenta(String tittle, String date) {
        this.tittle = tittle;
        this.date = date;
    }

    public Lenta(int id_picture, String tittle, String date) {
        this.id_picture = id_picture;
        this.tittle = tittle;
        this.date = date;
    }

    public Lenta(int id_picture, String tittle, String date, String more) {
        this.id_picture = id_picture;
        this.tittle = tittle;
        this.date = date;
        this.more = more;
    }

    public int getId_picture() {
        return id_picture;
    }

    public void setId_picture(int id_picture) {
        this.id_picture = id_picture;
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

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
