package com.example.practic;

public class CoworkingSpace {
    private int id;
    private String title;
    private Coordinates coordinates;

    public CoworkingSpace(int id, String title, Coordinates coordinates) {
        this.id = id;
        this.title = title;
        this.coordinates = coordinates;
    }

    public CoworkingSpace(int id, String title, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.coordinates = new Coordinates(latitude, longitude);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
