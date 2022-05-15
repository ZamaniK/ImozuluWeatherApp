package com.example.imozuluweatherapp;

public class ReadWriteUserDetails {
    public String fullName, doB, role ,gender, mobile;
    public  ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textFullName, String textDoB, String textGender, String textMobile) {
        this.doB = textDoB;
        this.gender = textGender;
        this.fullName= textFullName;
        this.mobile = textMobile;
    }



}
