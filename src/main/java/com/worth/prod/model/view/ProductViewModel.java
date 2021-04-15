package com.worth.prod.model.view;

import com.worth.prod.model.entity.CategoryEntity;
import com.worth.prod.model.entity.UserEntity;
import com.worth.prod.model.entity.enums.GenreName;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductViewModel {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private int copies;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String producer;
    private GenreName genre;
    private CategoryEntity categoryEntity;
    private UserEntity addedFrom;

    public ProductViewModel() {
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public UserEntity getAddedFrom() {
        return addedFrom;
    }

    public void setAddedFrom(UserEntity addedFrom) {
        this.addedFrom = addedFrom;
    }
}
