package com.mohamed.theftscanner;

public class Theft {

    private String owner;
    private String type;
    private String brand;
    private String model;
    private String street;
    private String city;
    private String imageURL;

    private double latitude;
    private double longitude;


    public Theft(String owner, String type, String brand, String model, String street, String city, String imageURL, double latitude, double longitude) {
        this.owner = owner;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.street = street;
        this.city = city;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Theft(){}

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
