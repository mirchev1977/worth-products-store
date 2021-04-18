package com.w.prod.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.repositories.EquipmentRepository;
import com.w.prod.repositories.PremiseRepository;
import com.w.prod.services.EquipmentService;
import com.w.prod.services.PremiseService;
import com.w.prod.services.impl.EquipmentServiceImpl;
import com.w.prod.services.impl.PremiseServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PremiseServiceTest {


    PremiseService service;
    ModelMapper modelMapper;

    @Value("classpath:init/premises.json")
    Resource premisesRes;
    @Value("classpath:init/equipment.json")
    Resource equipmentRes;
    @Autowired
    Gson gson;
    @Mock
    PremiseRepository mockPremiseRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;

    EquipmentService equipmentService;
    Equipment firstEquipment;
    Equipment secondEquipment;
    Premise firstPremise;
    Premise secondPremise;
    Premise thirdPremise;
    Product firstProduct;
    Product secondProduct;
    Product thirdProduct;
    Product oldProduct;
    Product fourthProduct;
    @Mock
    Premise mockPremise;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        firstEquipment = this.getFirstEquipment();
        secondEquipment = this.getSecondEquipment();
        firstPremise = this.getFirstPremise();
        secondPremise = this.getSecondPremise();
        thirdPremise = this.getThirdPremise();
        firstProduct = this.getProducts().get(0);
        secondProduct = this.getProducts().get(1);
        thirdProduct = this.getProducts().get(3);
        fourthProduct = this.getProducts().get(4);
        oldProduct = this.getProducts().get(2);
        equipmentService = new EquipmentServiceImpl(equipmentRes, gson, mockEquipmentRepository, modelMapper);
        this.service = new PremiseServiceImpl(premisesRes, gson, mockPremiseRepository, equipmentService, modelMapper);
    }


    @Test
    void testGetAllPremises() {
        when(mockPremiseRepository.findAll()).thenReturn(List.of(firstPremise, secondPremise));
        List<String> result = service.getAllPremises();
        Assertions.assertEquals("Carnegie1", result.get(0));
        Assertions.assertEquals("Tesla", result.get(1));
    }

    @Test
    void testGetSuitablePremises() {
        when(mockPremiseRepository.findAllByEquipment_EquipmentName("Innovation Space")).thenReturn(List.of(secondPremise));
        List<String> result = service.findSuitablePremises("Innovation Space");
        Assertions.assertEquals("Tesla", result.get(0));
    }

    @Test
    void testFindPremise() {
        when(mockPremiseRepository.findByName("Tesla")).thenReturn(Optional.ofNullable(secondPremise));
        Premise result = service.findPremise("Tesla");
        Assertions.assertEquals("Tesla", result.getName());
    }

    @Test
    void testGetSuitablePremisesWithProducts() {
        secondPremise.setProducts(List.of(firstProduct, secondProduct, oldProduct));
        thirdPremise.setProducts(List.of(thirdProduct));
        when(mockPremiseRepository.findAllByEquipment_EquipmentName("Innovation Space")).thenReturn(List.of(secondPremise, thirdPremise));

        Map<String, String> result = service.getSuitablePremisesWithProducts("Innovation Space");

        Assertions.assertTrue(result.keySet().contains("Tesla2"));
        Assertions.assertTrue(result.keySet().contains("Tesla"));
        Assertions.assertEquals("16 MAY 2023 - 17 MAY 2023 <br />20 MAY 2023 - 22 MAY 2023 <br />", result.get("Tesla"));
        Assertions.assertEquals("24 APRIL 2023 - 26 APRIL 2023 <br />", result.get("Tesla2"));
    }

    @Test
    void testGetAllPremisesWithProducts() {
        firstPremise.setProducts(List.of(fourthProduct));
        secondPremise.setProducts(List.of(firstProduct, secondProduct, oldProduct));
        thirdPremise.setProducts(List.of(thirdProduct));
        when(mockPremiseRepository.findAll()).thenReturn(List.of(firstPremise, secondPremise, thirdPremise));

        Map<String, String> result = service.getAllPremisesWithProducts();

        Assertions.assertTrue(result.keySet().contains("Tesla2"));
        Assertions.assertTrue(result.keySet().contains("Tesla"));
        Assertions.assertTrue(result.keySet().contains("Carnegie1"));
        Assertions.assertEquals("16 MAY 2023 - 17 MAY 2023 <br />20 MAY 2023 - 22 MAY 2023 <br />", result.get("Tesla"));
        Assertions.assertEquals("24 APRIL 2023 - 26 APRIL 2023 <br />", result.get("Tesla2"));
        Assertions.assertEquals("25 APRIL 2023 - 29 APRIL 2023 <br />", result.get("Carnegie1"));
    }

    //    Initialization methods
    private Equipment getFirstEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        return equipment;
    }

    private Equipment getSecondEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Innovation space");
        return equipment;
    }

    private Premise getFirstPremise() {
        Premise firstPremise = new Premise();
        firstPremise.setName("Carnegie1");
        firstPremise.setEquipment(firstEquipment);
        return firstPremise;
    }

    private Premise getSecondPremise() {
        Premise secondPremise = new Premise();
        secondPremise.setName("Tesla");
        secondPremise.setEquipment(secondEquipment);
        return secondPremise;
    }

    private Premise getThirdPremise() {
        Premise thirdPremise = new Premise();
        thirdPremise.setName("Tesla2");
        thirdPremise.setEquipment(secondEquipment);
        return thirdPremise;
    }

    private List<Product> getProducts() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");

        Product first = new Product();
        first
                .setName("123")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setPremise(secondPremise)
                .setActivityType(activityType)
                .setActive(true)
                .setStartDate(LocalDate.of(2023, 5, 16))
                .setEndDate(LocalDate.of(2023, 5, 17));

        Product second = new Product();
        second
                .setName("456")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setPremise(secondPremise)
                .setActivityType(activityType)
                .setStartDate(LocalDate.of(2023, 5, 20))
                .setEndDate(LocalDate.of(2023, 5, 22));
        Product old = new Product();
        old
                .setName("789")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setPremise(secondPremise)
                .setActivityType(activityType)
                .setStartDate(LocalDate.of(2021, 3, 20))
                .setEndDate(LocalDate.of(2021, 3, 22));

        Product third = new Product();
        third
                .setName("012")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setPremise(thirdPremise)
                .setActivityType(activityType)
                .setStartDate(LocalDate.of(2023, 4, 24))
                .setEndDate(LocalDate.of(2023, 4, 26));

        Product fourth = new Product();
        fourth
                .setName("111")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(firstEquipment)
                .setPremise(thirdPremise)
                .setActivityType(activityType)
                .setStartDate(LocalDate.of(2023, 4, 25))
                .setEndDate(LocalDate.of(2023, 4, 29));
        return List.of(first, second, old, third, fourth);
    }

}

