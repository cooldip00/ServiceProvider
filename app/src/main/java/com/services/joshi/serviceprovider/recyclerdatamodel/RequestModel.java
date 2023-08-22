package com.services.joshi.serviceprovider.recyclerdatamodel;

public class RequestModel {

    private int id;
    private String service;
    private String name;
    private int image;
    private Double ratting;

    public RequestModel() {
    }



    public RequestModel(int id, String service, String name, int image, Double ratting) {
        this.id = id;
        this.service = service;
        this.name = name;
        this.image = image;
        this.ratting = ratting;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public Double getRatting() {
        return ratting;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setRatting(Double ratting) {
        this.ratting = ratting;
    }
}
