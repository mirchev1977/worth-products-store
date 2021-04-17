package com.w.prod.services;

import com.w.prod.models.service.ActivityTypeServiceModel;

import java.util.List;

public interface ActivityTypeService {

    void seedActivityTypes();

    void addNewActivity(ActivityTypeServiceModel activityTypeServiceModel);

    List<String> getAllActivities();
}
