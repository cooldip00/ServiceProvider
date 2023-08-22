package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Provider;

import java.util.List;

public class SPLoginResponse {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("provider")
    @Expose
    private Provider provider;

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public SPLoginResponse(boolean error, String message, Integer statusCode, Provider provider) {
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
        this.provider = provider;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Provider getProvider() {
        return provider;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
