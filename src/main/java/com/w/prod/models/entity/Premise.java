package com.w.prod.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "premises")
public class Premise extends BaseEntity {

    @Column(name = "premise_name", unique=true, nullable = false)
    private String name;

    @ManyToOne
    @NotNull
    private Equipment equipment;

    @OneToMany(mappedBy = "premise", targetEntity = Product.class)
    private List<Product> products;

    public Premise() {
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Premise setName(String name) {
        this.name = name;
        return this;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Premise setEquipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Premise setProducts(List<Product> products) {
        this.products = products;
        return this;
    }
}
