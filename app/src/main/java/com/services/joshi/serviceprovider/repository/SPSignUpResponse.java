package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SPSignUpResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private boolean error;

    public SPSignUpResponse(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
