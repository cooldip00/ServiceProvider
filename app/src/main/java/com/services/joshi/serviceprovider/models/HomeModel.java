package com.services.joshi.serviceprovider.models;

public class HomeModel {

    int id;
    int image;
    String service,name;
    double ratting;

    public HomeModel(int id, int image, String service, String name, double ratting) {
        this.id = id;
        this.image = image;
        this.service = service;
        this.name = name;
        this.ratting = ratting;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public double getRatting() {
        return ratting;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRatting(double ratting) {
        this.ratting = ratting;
    }
}
