package com.example.fixmycar;

import java.io.Serializable;

/**
 * Created by tanzhongyi on 2015/4/3.
 */
public class DamageCarInfo implements Serializable{
    private String carMode;
    private String carImage;
    private String carDamageDesc;
    private double carLatitude;
    private double carLongitude;
    public DamageCarInfo(String mode, String image, String desc, double latitude,double longitude) {
        carMode = mode;
        carImage = image;
        carDamageDesc = desc;
        carLatitude= latitude;
        carLongitude= longitude;

    }

    public String getCarMode() {
        return carMode;
    }
    public String getCarImage() {
        return carImage;
    }
    public String getCarDamageDesc() {
        return carDamageDesc;
    }
    public double getCarLatitude() {
        return carLatitude;
    }
    public double getCarLongitude() {
        return carLongitude;
    }

}
