package com.w.prod.models.view;

public class ProductResultViewModel {


    private String name;

    private String result;

    public String getName() {
        return name;
    }

    public ProductResultViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getResult() {
        return result;
    }

    public ProductResultViewModel setResult(String result) {
        this.result = result;
        return this;
    }
}
