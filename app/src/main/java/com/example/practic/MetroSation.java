package com.example.practic;

public class MetroSation {
    private int id;
    private String title;
    private Coordinates coords;


    public MetroSation(int id, String title, Coordinates coords) {
        this.id = id;
        this.title = title;
        this.coords = coords;
    }

    public MetroSation(int id, String title, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.coords = new Coordinates(latitude, longitude);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Coordinates getCoords() {
        return coords;
    }
}
