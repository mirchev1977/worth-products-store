package com.w.prod.models.view;

public class BlueprintViewModel {


    private String id;

    private String name;

    private String category;

    private String description;

    private int duration;

    private String neededEquipment;

    private String activityType;

    private String promoter;
    private String status;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public BlueprintViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public BlueprintViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public BlueprintViewModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BlueprintViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public BlueprintViewModel setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public BlueprintViewModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public BlueprintViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public BlueprintViewModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BlueprintViewModel setStatus(String status) {
        this.status = status;
        return this;
    }
}
