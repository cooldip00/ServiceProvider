package com.services.joshi.serviceprovider.repository;


import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private boolean error;

    public RegisterResponse(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }
}
