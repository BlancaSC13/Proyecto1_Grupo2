package domain.System;

import java.util.ArrayList;
import java.util.List;

public class Reportes {
    String id;
    List<Order> orders;

    public Reportes(String id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }

    public Reportes(String id) {
        this.id = id;
        this.orders = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order){
        orders.add(order);
    }
}
