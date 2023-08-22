package com.services.joshi.serviceprovider.recyclerdatamodel;

public class SPHistoryModel {

    private int id, image;
    private String providerName,serviceName;
    private double rating;

    public SPHistoryModel() {
    }

    public SPHistoryModel(int id, int image, String providerName, String serviceName, double rating) {
        this.id = id;
        this.image = image;
        this.providerName = providerName;
        this.serviceName = serviceName;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getServiceName() {
        return serviceName;
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

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
