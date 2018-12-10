package edu.umd.jchao.healthapp;

//Base class to hold information about the property
public class ListItem {

    //property basics
    private int streetNumber;
    private String streetName;
    private String name;
    private String suburb;
    private String state;
    private String description;
    private String image;
    private Double price;
    private int bedrooms;
    private int bathrooms;
    private int carspots;
    private int calories;
    private int amount;

    //constructor
    public ListItem(int streetNumber, String streetName, String suburb, String state, String description, Double price, String image, int bedrooms, int bathrooms, int carspots) {

        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.state = state;
        this.description = description;
        this.price = price;
        this.image = image;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.carspots = carspots;
    }

    public ListItem(String name, String description, int amount, int calories, String image) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.image = image;
        this.amount = amount;
    }

    //getters
    public int getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getCarspots() {
        return carspots;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}