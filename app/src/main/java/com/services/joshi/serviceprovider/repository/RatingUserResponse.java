package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.RatingUser;

import java.util.List;

public class RatingUserResponse {

    @SerializedName("RatingUser")
    @Expose
    private List<RatingUser> ratingUser = null;
    @SerializedName("error")
    @Expose
    private boolean error;

    public List<RatingUser> getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(List<RatingUser> ratingUser) {
        this.ratingUser = ratingUser;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
