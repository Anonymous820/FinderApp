package com.example.finderapp.model;

public class SuitcasePlace {

    double lat;
    double lng;
    String name;


    public SuitcasePlace() {
    }

    public SuitcasePlace(double lat, double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SuitcasePlace{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                '}';
    }
}
