package domain.System;

public class OrderDetail {
    private int id; // int identity
    private static int identityID;
    private Product product;
    private int quantity;
    private double totalPrice;

    public OrderDetail(int quantity, Product product) {
        identityID++;
        this.id = identityID;
        this.quantity = quantity;
        this.product = product;
        this.totalPrice = product.getPrice()*quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
