package com.example.sqliteassignment;

public class Product {
    private String name;
    private String desc;
    private double weight;
    private double price;
    private int availability;

    public Product(String name, String desc, double weight, double price, int availability) {
        this.name = name;
        this.desc = desc;
        this.weight = weight;
        this.price = price;
        this.availability = availability;
    }


    // Object Getters and Setters and toString
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return (" name: " + name + ", desc: " + desc + ", weight: " + weight + ", price: " + price + ", availability:" + availability);

    }
}