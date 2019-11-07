package com.example.theftscanner;

public class Theft {

    private String NameOwner;
    private String Type;
    private String Brand;
    private String Model;
    private String Street;
    private String City;
    private String ImageURL;
    private String id;

    private double Latitude;
    private double Longitude;


    public Theft(String nameOwner, String type, String brand, String model, String street, String city, String imageURL, String ID, double latitude, double longitude) {
        NameOwner = nameOwner;
        Type = type;
        Brand = brand;
        Model = model;
        Street = street;
        City = city;
        ImageURL = imageURL;
        id = ID;
        Latitude = latitude;
        Longitude = longitude;
    }
    public Theft(){}

    public String getImageURL() {return ImageURL;}

    public void setNameOwner(String nameOwner) {
        NameOwner = nameOwner;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getId() {
        return id;
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

    public String getType() {
        return Type;
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
