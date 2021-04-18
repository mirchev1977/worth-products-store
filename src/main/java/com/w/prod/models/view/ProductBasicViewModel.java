package com.w.prod.models.view;

public class ProductBasicViewModel {

    private String id;

    private String name;

    private String category;

    private String startDate;

    private String activityType;

    private String lab;

    public String getId() {
        return id;
    }

    public ProductBasicViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductBasicViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductBasicViewModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public ProductBasicViewModel setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProductBasicViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getPremise() {
        return lab;
    }

    public ProductBasicViewModel setPremise(String lab) {
        this.lab = lab;
        return this;
    }
}
