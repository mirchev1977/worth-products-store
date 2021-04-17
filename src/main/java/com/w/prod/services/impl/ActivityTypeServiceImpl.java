package com.w.prod.services.impl;

import com.w.prod.models.entity.ActivityType;
import com.w.prod.models.service.ActivityTypeServiceModel;
import com.w.prod.repositories.ActivityTypeRepository;
import com.w.prod.services.ActivityTypeService;
import com.google.gson.Gson;
import org.hibernate.property.access.internal.PropertyAccessStrategyIndexBackRefImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    private final Gson gson;
    private final ActivityTypeRepository activityTypeRepository;
    private final ModelMapper modelMapper;

    public ActivityTypeServiceImpl(
            Gson gson,
            ActivityTypeRepository activityTypeRepository,
            ModelMapper modelMapper
    ) {
        this.gson = gson;
        this.activityTypeRepository = activityTypeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedActivityTypes() {
        if (activityTypeRepository.count() == 0) {
            ArrayList<String> activityTypes = new ArrayList<>();
            activityTypes.add("Business Support");
            activityTypes.add("Masterclass");
            activityTypes.add("Networking Session");
            activityTypes.add("Product Innovation Session");
            activityTypes.add("Tech Demonstration");
            activityTypes.add("Training");

            for (String activity : activityTypes) {
                ActivityType act = new ActivityType();
                act.setActivityName(activity);
                activityTypeRepository.save(act);
            }
        }
    }

    @Override
    public void addNewActivity(ActivityTypeServiceModel activityTypeServiceModel) {
        activityTypeRepository.save(modelMapper.map(activityTypeServiceModel, ActivityType.class));
    }


    @Override
    public List<String> getAllActivities() {
        return activityTypeRepository.findAll().stream().map(ActivityType::getActivityName).collect(Collectors.toList());
    }
}
