package domain.System;

import java.time.LocalDate;

public class Sale {
    private int id;  // int identity
    private java.time.LocalDate saleData;
    private int customerId;
    private String annotations;

    public Sale(int id, LocalDate saleData, int customerId, String annotations) {
        this.id = id;
        this.saleData = saleData;
        this.customerId = customerId;
        this.annotations = annotations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getSaleData() {
        return saleData;
    }

    public void setSaleData(LocalDate saleData) {
        this.saleData = saleData;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }
}
