package com.example.dharam.police;

/**
 * Created by Dharam on 8/29/2018.
 */

public class CarLocation
{
    public String longitude;
    public String  latitude;
    public String registrationNo;

    public CarLocation(){}

    public CarLocation(String longitude, String latitude,String registrationNo) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.registrationNo=registrationNo;
    }

    public double getLongitude() {
        return Double.parseDouble(longitude);
    }

    public double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public String getRegistrationNo() {
        return registrationNo;
    }
}
