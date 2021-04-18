package com.w.prod.models.service;

import com.w.prod.models.entity.enums.Category;

public class BlueprintServiceModel {
    private String id;

    private String name;

    private Category category;

    private String description;

    private int duration;

    private String neededEquipment;

    private String activityType;

    private String promoter;

    public String getId() {
        return id;
    }

    public BlueprintServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BlueprintServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public BlueprintServiceModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BlueprintServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public BlueprintServiceModel setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public BlueprintServiceModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public BlueprintServiceModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public BlueprintServiceModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }
}
