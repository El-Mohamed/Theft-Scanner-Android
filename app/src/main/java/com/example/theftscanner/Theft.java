package com.example.theftscanner;

public class Theft {

    private String NameOwner;
    private String Brand;
    private String Model;
    private String Street;
    private String City;

    private double Latitude;
    private double Longitude;


    public Theft(String nameOwner, String brand, String model, String street, String city, double latitude, double longitude) {
        NameOwner = nameOwner;
        Brand = brand;
        Model = model;
        Street = street;
        City = city;
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getNameOwner() {
        return NameOwner;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getStreet() {
        return Street;
    }

    public String getCity() {
        return City;
    }
}
