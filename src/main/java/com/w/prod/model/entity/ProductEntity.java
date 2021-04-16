package com.w.prod.model.entity;

import com.w.prod.model.entity.enums.GenreName;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {
    private String name;
    private String imageUrl;
    private String description;
    private int quantity;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String producer;
    private GenreName genre;
    private CategoryEntity categoryEntity;
    private UserEntity addedFrom;

    public ProductEntity() {
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "image_url", nullable = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Column(name = "release_date", nullable = false)
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column(name = "producer")
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Enumerated(EnumType.ORDINAL)
    public GenreName getGenre() {
        return genre;
    }

    public void setGenre(GenreName genre) {
        this.genre = genre;
    }

    @OneToOne
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @ManyToOne
    public UserEntity getAddedFrom() {
        return addedFrom;
    }

    public void setAddedFrom(UserEntity addedFrom) {
        this.addedFrom = addedFrom;
    }
}
