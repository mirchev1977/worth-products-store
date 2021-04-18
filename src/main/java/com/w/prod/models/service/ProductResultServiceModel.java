package com.w.prod.models.service;

import com.w.prod.models.entity.enums.Category;

public class ProductResultServiceModel {

    private String id;
    private String name;

    private Category category;

    private String description;

    public String getId() {
        return id;
    }

    public ProductResultServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductResultServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public ProductResultServiceModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductResultServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }
}
