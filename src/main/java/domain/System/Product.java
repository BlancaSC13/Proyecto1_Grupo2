package domain.System;

public class Product {
    private int id;
    private String description;
    private double price;
    private int currentStock;
    private Integer minimumStock;
    private int suplierId;

    public Product(int id, String description, double price, int currentStock, Integer minimumStock, int suplierId) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.suplierId = suplierId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getSuplierId() {
        return suplierId;
    }

    public void setSuplierId(int suplierId) {
        this.suplierId = suplierId;
    }

    @Override
    public String toString() {
        return  "Product ID: " + id +
                "   Description: " + description
                +"   Price: " + price
                +"   Current Stock: " + currentStock
                +"   Minimum Stock: " + minimumStock
                +"   Suplier ID: " + suplierId;
    }
    public String datos() {
        return  "Product ID: " + id +
                "\nDescription: " + description
                +"\nPrice: " + price
                +"\nCurrent Stock: " + currentStock
                +"\nMinimum Stock: " + minimumStock
                +"\nSuplier ID: " + suplierId;
    }
}
