package com.w.prod.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class LogEntity extends BaseEntity {
    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    private String action;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Blueprint blueprint;


    @Column(nullable = false)
    private LocalDateTime time;

    public LogEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public LogEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public LogEntity setAction(String action) {
        this.action = action;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public LogEntity setProduct(Product product) {
        this.product = product;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LogEntity setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public LogEntity setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
        return this;
    }
}
