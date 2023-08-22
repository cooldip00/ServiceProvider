package com.services.joshi.serviceprovider.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("id")
    private Integer id;

    @SerializedName("fname")
    private String fname;

    @SerializedName("mname")
    private String mname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("category_id")
    private Integer categoryId;

    @SerializedName("gender")
    private String gender;

    @SerializedName("aadhar_image")
    private String aadharImage;

    @SerializedName("user_image")
    private String userImage;

    @SerializedName("status")
    private Integer status;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longtitude")
    private String longtitude;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("category")
    private Category category;

    public Provider(Integer id, String fname, String mname, String lname, String email, String phone, String address, Integer categoryId, String gender, String aadharImage, String userImage, Integer status, String latitude, String longtitude, String createdAt, String updatedAt) {
        this.id = id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.categoryId = categoryId;
        this.gender = gender;
        this.aadharImage = aadharImage;
        this.userImage = userImage;
        this.status = status;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharImage() {
        return aadharImage;
    }

    public void setAadharImage(String aadharImage) {
        this.aadharImage = aadharImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
