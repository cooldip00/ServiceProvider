package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Services;

import java.util.List;

public class SPRequestServiceResponse {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("services")
    @Expose
    private List<Services> services = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }
}
