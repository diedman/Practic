package com.example.practic;

public class CoworkingSpace {
    private int id;
    private String title;
    private Coordinates coordinates;
    private int seats;

    public CoworkingSpace(int id, String title, Coordinates coordinates, int seats) {
        this.id          = id;
        this.title       = title;
        this.coordinates = coordinates;
        this.seats       = seats;
    }

    public CoworkingSpace(int id, String title, double latitude, double longitude, int seats) {
        this.id          = id;
        this.title       = title;
        this.coordinates = new Coordinates(latitude, longitude);
        this.seats       = seats;
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

    public int getSeats() {
        return seats;
    }
}
