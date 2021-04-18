package com.w.prod.models.service;

import com.google.gson.annotations.Expose;

public class PremiseServiceModel {
    private String id;
    @Expose
    private String name;
    @Expose
    private String equipment;

    public String getId() {
        return id;
    }

    public PremiseServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PremiseServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getEquipment() {
        return equipment;
    }

    public PremiseServiceModel setEquipment(String equipment) {
        this.equipment = equipment;
        return this;
    }
}
