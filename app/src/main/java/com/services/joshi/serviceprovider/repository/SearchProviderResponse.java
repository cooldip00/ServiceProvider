package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Category;
import com.services.joshi.serviceprovider.models.SearchedProvider;

import java.util.List;

public class SearchProviderResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("provider")
    @Expose
    private List<SearchedProvider> provider = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<SearchedProvider> getProvider() {
        return provider;
    }

    public void setProvider(List<SearchedProvider> provider) {
        this.provider = provider;
    }
}
