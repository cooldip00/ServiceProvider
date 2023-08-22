package com.services.joshi.serviceprovider.recyclerdatamodel;

public class SPServiceModel {

    private int id, image;
    private String serviceName, providerName;
    private double rating;

    public SPServiceModel() {
    }

    public SPServiceModel(int id, int image, String serviceName, String providerName, double rating) {
        this.id = id;
        this.image = image;
        this.serviceName = serviceName;
        this.providerName = providerName;
        this.rating = rating;
    }


    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getProviderName() {
        return providerName;
    }

    public double getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
