package com.mongodb.yaoxing.demo.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * POJO对象Person
 */
public class Person implements Serializable {
    private String name;
    private String address;
    private Date birthday;
    private int age;
    private BigDecimal balance;
    private List<String> favouriteColor;
    private List<Phone> phones;

    public Person() {

    }

    public Person(String name, String address, Date birthday, List<String> favouriteColor,
                  int age, BigDecimal balance, List<Phone> phones) {
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.favouriteColor = favouriteColor;
        this.age = age;
        this.balance = balance;
        this.phones = phones;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
