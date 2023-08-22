package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Category;
import com.services.joshi.serviceprovider.models.Provider;

import java.util.List;

public class    CategoryResponse {

    @SerializedName("provider")
    @Expose
    private List<Provider> provider = null;

    @SerializedName("error")
    @Expose
    private Boolean error;


    public List<Provider> getProvider() {
        return provider;
    }

    public void setProvider(List<Provider> provider) {
        this.provider = provider;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
