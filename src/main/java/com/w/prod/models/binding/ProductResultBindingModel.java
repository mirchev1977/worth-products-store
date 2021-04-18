package com.w.prod.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ProductResultBindingModel {

    @NotEmpty
    private String sector;

    @NotEmpty
    @Size(min = 50, max = 5000)
    private String description;

    public String getSector() {
        return sector;
    }

    public ProductResultBindingModel setSector(String sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductResultBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }
}
