package com.w.prod.model.service;

import com.w.prod.model.entity.enums.CategoryName;
import com.w.prod.model.entity.enums.GenreName;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductServiceModel {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private int quantity;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String producer;
    private GenreName genre;
    private CategoryName category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CategoryName getCategory() {
        return category;
    }

    public void setCategory(CategoryName category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public GenreName getGenre() {
        return genre;
    }

    public void setGenre(GenreName genre) {
        this.genre = genre;
    }
}