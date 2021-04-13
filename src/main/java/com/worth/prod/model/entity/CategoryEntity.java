package com.worth.prod.model.entity;

import com.worth.prod.model.entity.enums.CategoryName;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {
    private CategoryName name;
    private String careerInformation;

    public CategoryEntity() {
    }

    public CategoryEntity(CategoryName name, String careerInformation) {
        this.name = name;
        this.careerInformation = careerInformation;
    }

    @Enumerated(EnumType.STRING)
    public CategoryName getName() {
        return name;
    }

    public void setName(CategoryName name) {
        this.name = name;
    }

    @Column(name = "career_information", columnDefinition = "TEXT")
    public String getCareerInformation() {
        return careerInformation;
    }

    public void setCareerInformation(String description) {
        this.careerInformation = description;
    }
}
