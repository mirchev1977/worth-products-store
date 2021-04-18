package com.w.prod.models.service;

import com.w.prod.models.entity.enums.Category;

import java.time.LocalDate;

public class ProductServiceModel {

    private String id;
    private String name;
    private Category category;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String activityType;
    private String neededEquipment;
    private String lab;
    private String promoter;

    public String getId() {
        return id;
    }

    public ProductServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public ProductServiceModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ProductServiceModel setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProductServiceModel setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProductServiceModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public ProductServiceModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getPremise() {
        return lab;
    }

    public ProductServiceModel setPremise(String lab) {
        this.lab = lab;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public ProductServiceModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }
}
