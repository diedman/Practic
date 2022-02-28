package com.example.practic;

public class CoworkingSpace {
    private int id;
    private String title;
    private Coordinates coords;

    public CoworkingSpace(int id, String title, Coordinates coords) {
        this.id = id;
        this.title = title;
        this.coords = coords;
    }

    public CoworkingSpace(int id, String title, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.coords = new Coordinates(latitude, longitude);
    }
}
