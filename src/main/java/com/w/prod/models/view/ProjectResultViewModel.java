package com.w.prod.models.view;

public class ProjectResultViewModel {


    private String name;

    private String result;

    public String getName() {
        return name;
    }

    public ProjectResultViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getResult() {
        return result;
    }

    public ProjectResultViewModel setResult(String result) {
        this.result = result;
        return this;
    }
}
