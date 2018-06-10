package com.mongodb.yaoxing.demo.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Person implements Serializable {
    private String name;
    private String address;
    private Date birthday;
    private List<String> favouriteColor;

    public Person() {

    }

    public Person(String name, String address, Date birthday) {
        this.name = name;
        this.address = address;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<String> getFavouriteColor() {
        return favouriteColor;
    }

    public void setFavouriteColor(List<String> favouriteColor) {
        this.favouriteColor = favouriteColor;
    }
}
