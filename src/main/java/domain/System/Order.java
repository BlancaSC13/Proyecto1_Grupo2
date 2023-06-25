package domain.System;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;  //int identity
    private LocalDateTime orderDate;
    private String user;
    private List orders;
    private static int identityId;

    public Order(LocalDateTime orderDate, String user, List orders) {
        this.id = identityId++;
        this.orderDate = orderDate;
        this.user = user;
        this.orders = orders;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String  getUser() {
        return user;
    }

    public void setUser(String  user) {
        this.user = user;
    }

}