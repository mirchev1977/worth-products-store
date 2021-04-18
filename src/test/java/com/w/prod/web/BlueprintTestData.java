package com.w.prod.web;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.repositories.*;

import javax.transaction.Transactional;

public class BlueprintTestData {

    private String testBlueprintId;
    private Equipment equipment;

    private BlueprintRepository blueprintRepository;
    private ActivityTypeRepository activityTypeRepository;
    private EquipmentRepository equipmentRepository;
    private PremiseRepository premiseRepository;
    private UserRepository userRepository;
    private LogRepository logRepository;
    private ProductRepository productRepository;

    public BlueprintTestData(BlueprintRepository blueprintRepository, ActivityTypeRepository activityTypeRepository, EquipmentRepository equipmentRepository, PremiseRepository premiseRepository, UserRepository userRepository, LogRepository logRepository, ProductRepository productRepository) {
        this.blueprintRepository = blueprintRepository;
        this.activityTypeRepository = activityTypeRepository;
        this.equipmentRepository = equipmentRepository;
        this.premiseRepository = premiseRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void init() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        activityTypeRepository.save(activityType);
        equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        equipmentRepository.saveAndFlush(equipment);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("pesho")
                .setPassword("123456789")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        userRepository.save(userEntity);

        Blueprint blueprint = new Blueprint();
        blueprint.setName("Blueprint test")
                .setPromoter(userEntity)
                .setActivityType(activityType)
                .setDescription("description of the test blueprint")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setCategory(Category.Arts)
                .setStatus("pending");
        blueprintRepository.save(blueprint);
        testBlueprintId = blueprint.getId();

    }

    void cleanUp() {
        logRepository.deleteAll();
        blueprintRepository.deleteAll();
        productRepository.deleteAll();
        activityTypeRepository.deleteAll();
        userRepository.deleteAll();
        premiseRepository.deleteAll();
        equipmentRepository.deleteAll();
    }

    public String getTestBlueprintId() {
        return testBlueprintId;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}

