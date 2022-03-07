package com.example.practic;

import java.util.Arrays;

public class Coordinates {
    public double latitude;
    public double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinates parse(String coordinates) {
        double[] coords = Arrays.stream(coordinates.split(":"))
                .mapToDouble(Double::parseDouble)
                .toArray();

        return new Coordinates(coords[0], coords[1]);
    }
}
