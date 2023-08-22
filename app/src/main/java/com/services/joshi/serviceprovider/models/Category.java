package com.services.joshi.serviceprovider.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    private int id;
    private String category_name;

    @SerializedName("category_image")
    @Nullable
    @Expose
    private String category_image;

    @SerializedName("created_at")
    @Nullable
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Nullable
    @Expose
    private String updated_at;

    public Category(int id, String category_name) {
        this.id = id;
        this.category_name = category_name;
    }

    public void setCategory_image(@Nullable String category_image) {
        this.category_image = category_image;
    }

    public void setCreated_at(@Nullable String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(@Nullable String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
