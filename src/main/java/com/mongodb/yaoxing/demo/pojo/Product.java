package com.mongodb.yaoxing.demo.pojo;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String sku;
    private String name;
    private Map variables;

    public Product() {

    }

    public Product(String sku, String name, Map variables) {
        this.sku = sku;
        this.name = name;
        this.variables = variables;
    }

    public Map getVariables() {
        return variables;
    }

    public void setVariables(Map variables) {
        this.variables = variables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
