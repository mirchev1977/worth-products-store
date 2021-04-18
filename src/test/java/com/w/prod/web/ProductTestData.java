package com.w.prod.web;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.repositories.*;

import javax.transaction.Transactional;
import java.time.LocalDate;

public class ProductTestData {

    private String productId;
    private Equipment equipment;
    private ActivityType activityType;
    private Premise premise;
    private String userId;

    private ActivityTypeRepository activityTypeRepository;
    private EquipmentRepository equipmentRepository;
    private ProductRepository productRepository;
    private PremiseRepository premiseRepository;
    private UserRepository userRepository;
    private LogRepository logRepository;

    public ProductTestData(ActivityTypeRepository activityTypeRepository, EquipmentRepository equipmentRepository, ProductRepository productRepository, PremiseRepository premiseRepository, UserRepository userRepository, LogRepository logRepository) {
        this.activityTypeRepository = activityTypeRepository;
        this.equipmentRepository = equipmentRepository;
        this.productRepository = productRepository;
        this.premiseRepository = premiseRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public void init() {
        activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        activityTypeRepository.save(activityType);
        equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        equipmentRepository.saveAndFlush(equipment);
        Premise premise = new Premise();
        premise
                .setName("Tesla1")
                .setEquipment(equipment);
        premiseRepository.saveAndFlush(premise);

        UserEntity user = new UserEntity();
        user.setUsername("pesho")
                .setPassword("123456789")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        userRepository.save(user);
        userId = user.getId();

        Product product = new Product();
        product
                .setName("123")
                .setPromoter(user)
                .setActivityType(activityType)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setCategory(Category.Arts)
                .setPremise(premise)
                .setStartDate(LocalDate.of(2021, 4, 16))
                .setEndDate(LocalDate.of(2021, 4, 17));

        productRepository.save(product);
        productId = product.getId();

    }

    void cleanUp() {
        logRepository.deleteAll();
        productRepository.deleteAll();
        activityTypeRepository.deleteAll();
        userRepository.deleteAll();
        premiseRepository.deleteAll();
        equipmentRepository.deleteAll();
    }

    public String getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Premise getPremise() {
        return premise;
    }

}

