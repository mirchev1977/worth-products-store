package com.w.prod.models.entity;

import com.w.prod.models.entity.enums.Category;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @Column(columnDefinition = "Text", nullable = false)
    private String description;

    @Column(name = "starting_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @ManyToOne
    @NotNull
    private ActivityType activityType;

    @ManyToOne
    @NotNull
    private Equipment neededEquipment;

    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    @NotNull
    private Premise premise;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "result", columnDefinition = "Text")
    private String result;

    @ManyToOne
    private UserEntity  promoter;

    @ManyToMany
    @JoinTable(name = "products_users",
            joinColumns = @JoinColumn(name="product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id"))
    private Set<UserEntity> collaborators = new HashSet<>();

    public Product() {
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Product setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Product setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Product setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public Equipment getNeededEquipment() {
        return neededEquipment;
    }

    public Product setNeededEquipment(Equipment neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public Premise getPremise() {
        return premise;
    }

    public Product setPremise(Premise premise) {
        this.premise = premise;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Product setActive(boolean active) {
        this.active = active;
        return this;
    }

    public UserEntity getPromoter() {
        return promoter;
    }

    public Product setPromoter(UserEntity promoter) {
        this.promoter = promoter;
        return this;
    }

    public Set<UserEntity> getCollaborators() {
        return collaborators;
    }

    public Product setCollaborators(Set<UserEntity> collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    public String getResult() {
        return result;
    }

    public Product setResult(String result) {
        this.result = result;
        return this;
    }
    public void addCollaborator(UserEntity user) {
        this.collaborators.add(user);
    }
    public void removeCollaborator(UserEntity user) {
        this.collaborators.remove(user);
    }

}
