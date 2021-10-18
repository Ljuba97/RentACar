package com.rentACar.model.request;

import java.util.UUID;

public class CarAddRequestModel {
    private UUID carId;
    private String licencePlate, make, model, color, size, fuel, image;
    private int year, engineCapacity, doors, power;
    private double price;
    private boolean automatic;

    public CarAddRequestModel(String licencePlate, String make, String model, int year, int engineCapacity, String color, double price, int doors, String size, int power, boolean automatic, String fuel, String image) {
        this.carId = UUID.randomUUID();
        this.licencePlate = licencePlate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.engineCapacity = engineCapacity;
        this.color = color;
        this.price = price;
        this.doors = doors;
        this.size = size;
        this.power = power;
        this.automatic = automatic;
        this.fuel = fuel;
        this.image = image;
    }

    public UUID getCarId() {
        return carId;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getFuel() {
        return fuel;
    }

    public String getImage() {
        return image;
    }

    public int getYear() {
        return year;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public int getDoors() {
        return doors;
    }

    public int getPower() {
        return power;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public boolean isAutomatic() {
        return automatic;
    }
}
