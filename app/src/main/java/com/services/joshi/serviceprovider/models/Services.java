package com.services.joshi.serviceprovider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Services {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("provider")
    @Expose
    private ProviderWithRating provider;
    @SerializedName("user")
    @Expose
    private User user;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProviderWithRating getProvider() {
        return provider;
    }

    public void setProvider(ProviderWithRating provider) {
        this.provider = provider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
