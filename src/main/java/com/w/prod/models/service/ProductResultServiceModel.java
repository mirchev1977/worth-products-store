package com.w.prod.models.service;

import com.w.prod.models.entity.enums.Sector;

public class ProductResultServiceModel {

    private String id;
    private String name;

    private Sector sector;

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

    public Sector getSector() {
        return sector;
    }

    public ProductResultServiceModel setSector(Sector sector) {
        this.sector = sector;
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
