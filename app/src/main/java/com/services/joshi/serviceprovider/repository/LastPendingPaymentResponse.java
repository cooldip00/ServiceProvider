package com.services.joshi.serviceprovider.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.services.joshi.serviceprovider.models.Bill;

public class LastPendingPaymentResponse {

    @SerializedName("bill")
    @Expose
    private Bill bill;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
