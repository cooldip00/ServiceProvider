package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Provider;

public class SpProfileUpdateResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("provider")
    @Expose
    private Provider provider;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
