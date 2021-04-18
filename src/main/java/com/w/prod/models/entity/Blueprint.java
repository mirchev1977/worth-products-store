package com.w.prod.models.entity;

import com.w.prod.models.entity.enums.Category;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "blueprints")
public class Blueprint extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @Column(columnDefinition = "Text", nullable = false)
    private String description;

    @Column(nullable = false)
    private int duration;

    @Column
    private String status = "Pending";

    @ManyToOne
    @NotNull
    private Equipment neededEquipment;

    @ManyToOne
    @NotNull
    private ActivityType activityType;

    @ManyToOne
    @NotNull
    private UserEntity promoter;

    public String getName() {
        return name;
    }

    public Blueprint setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Blueprint setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Blueprint setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Blueprint setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public Equipment getNeededEquipment() {
        return neededEquipment;
    }

    public Blueprint setNeededEquipment(Equipment neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Blueprint setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public UserEntity getPromoter() {
        return promoter;
    }

    public Blueprint setPromoter(UserEntity promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Blueprint setStatus(String status) {
        this.status = status;
        return this;
    }
}
