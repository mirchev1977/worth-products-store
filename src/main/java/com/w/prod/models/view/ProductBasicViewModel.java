package com.w.prod.models.view;

public class ProductBasicViewModel {

    private String id;

    private String name;

    private String sector;

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

    public String getSector() {
        return sector;
    }

    public ProductBasicViewModel setSector(String sector) {
        this.sector = sector;
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

    public String getLab() {
        return lab;
    }

    public ProductBasicViewModel setLab(String lab) {
        this.lab = lab;
        return this;
    }
}
