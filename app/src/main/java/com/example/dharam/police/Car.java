package com.example.dharam.police;

/**
 * Created by Dharam on 7/3/2018.
 */

public class Car
{
    public String registration_no;
    public String brand;
    public String model;
    public String color;
    public String status;

    public Car()
    {
        //Empty Constructor.
    }

    public Car(String registration_no, String brand, String model, String color,String status)
    {
        this.registration_no = registration_no;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

}
