package com.w.prod.models.view;

import com.w.prod.models.entity.enums.Sector;

public class ProductDetailedViewModel {

    private String id;

    private String name;

    private Sector sector;

    private String description;

    private String duration;

    private String activityType;

    private String neededEquipment;

    private String lab;

    private String promoter;

   private String collaborators;

    public String getId() {
        return id;
    }

    public ProductDetailedViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDetailedViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public ProductDetailedViewModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailedViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public ProductDetailedViewModel setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProductDetailedViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public ProductDetailedViewModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getLab() {
        return lab;
    }

    public ProductDetailedViewModel setLab(String lab) {
        this.lab = lab;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public ProductDetailedViewModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getCollaborators() {
        return collaborators;
    }

    public ProductDetailedViewModel setCollaborators(String collaborators) {
        this.collaborators = collaborators;
        return this;
    }
}
