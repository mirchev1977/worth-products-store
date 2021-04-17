package com.w.prod.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activity_types")
public class ActivityType extends BaseEntity {

    @Column(name = "activity_name", unique = true, nullable = false)
    private String activityName;


    public ActivityType() {
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityType setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }


}
