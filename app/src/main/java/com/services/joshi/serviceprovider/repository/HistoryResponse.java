package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Services;

import java.util.List;

public class HistoryResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("services")
    @Expose
    private List<Services> services = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

}
