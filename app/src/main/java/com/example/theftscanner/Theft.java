package com.example.theftscanner;

public class Theft {

    private String NameOwner;
    private String Brand;
    private String Model;
    private String Street;
    private String City;


    public Theft(String nameOwner, String brand, String model, String street, String city) {
        NameOwner = nameOwner;
        Brand = brand;
        Model = model;
        Street = street;
        City = city;
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
