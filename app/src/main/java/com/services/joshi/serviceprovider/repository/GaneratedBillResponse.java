package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Bill;

import java.util.List;

public class GaneratedBillResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("bill")
    @Expose
    private Bill bill;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
