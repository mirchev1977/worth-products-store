package com.worth.prod.model.entity;

import com.worth.prod.model.entity.enums.ArtistName;

import javax.persistence.*;

@Entity
@Table(name = "artists")
public class ArtistEntity extends BaseEntity {
    private ArtistName name;
    private String careerInformation;

    public ArtistEntity() {
    }

    public ArtistEntity(ArtistName name, String careerInformation) {
        this.name = name;
        this.careerInformation = careerInformation;
    }

    @Enumerated(EnumType.STRING)
    public ArtistName getName() {
        return name;
    }

    public void setName(ArtistName name) {
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
