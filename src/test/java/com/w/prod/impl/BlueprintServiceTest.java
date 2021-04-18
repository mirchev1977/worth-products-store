package com.w.prod.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.service.BlueprintServiceModel;
import com.w.prod.models.view.BlueprintViewModel;
import com.w.prod.repositories.*;
import com.w.prod.services.impl.BlueprintServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BlueprintServiceTest {

    ModelMapper modelMapper;

    @Mock
    BlueprintRepository mockBlueprintRepository;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;

    @Mock
    ActivityTypeRepository mockActivityTypeRepository;
    @Mock
    UserRepository mockUserRepository;


    @InjectMocks
    BlueprintServiceImpl service;


    Equipment equipment;
    Blueprint current;
    UserEntity user;
    ActivityType activityType;
    BlueprintServiceModel blueprintServiceModel;
    BlueprintViewModel blueprintViewModel;
    Premise premise;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        equipment = this.getEquipment();
        premise = this.getPremise();
        activityType = this.getActivityType();
        user = this.getUser();
        current = this.getBlueprint();
        blueprintServiceModel = new BlueprintServiceModel();
        blueprintViewModel = new BlueprintViewModel();

        this.service = new BlueprintServiceImpl(mockBlueprintRepository,
                modelMapper, mockUserRepository, mockActivityTypeRepository,
                mockLogRepository, mockEquipmentRepository);
    }


    @Test
    void testAddedBlueprint() {

        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        blueprintServiceModel
                .setName("123")
                .setActivityType("Lecture")
                .setDescription("1234567890")
                .setCategory(Category.IT)
                .setDuration(2)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho");

        BlueprintViewModel blueprintViewModel = service.createBlueprint(blueprintServiceModel);

        Assertions.assertEquals("123", blueprintViewModel.getName());
        Assertions.assertEquals(2, blueprintViewModel.getDuration());

    }

    @Test
    void testGetAll() {
        Blueprint blueprint2 = new Blueprint();
        blueprint2
                .setName("12345")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setStatus("Accepted")
                .setActivityType(activityType)
                .setPromoter(user);
        when(mockBlueprintRepository.findAllByOrderByStatusDesc()).thenReturn(List.of(current, blueprint2));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        String fullNameCurrent = String.format("%s %s", current.getPromoter().getFirstName(), current.getPromoter().getLastName());
        String fullNameBlueprint2 = String.format("%s %s", current.getPromoter().getFirstName(), current.getPromoter().getLastName());

        List<BlueprintViewModel> result = service.getAll();

        Assertions.assertEquals(current.getName(), result.get(0).getName());
        Assertions.assertEquals(blueprint2.getName(), result.get(1).getName());
        Assertions.assertEquals("Pending", result.get(0).getStatus());
        Assertions.assertEquals("Accepted", result.get(1).getStatus());
        Assertions.assertEquals(fullNameCurrent, result.get(0).getPromoter());
        Assertions.assertEquals(fullNameBlueprint2, result.get(1).getPromoter());

    }

    @Test
    void markBlueprintAsAcceptedTestWithValidBlueprint() {
        when(mockBlueprintRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(current));

        boolean isAccepted = service.markBlueprintAsAccepted("1");

        Assertions.assertTrue(isAccepted);
    }

    @Test
    void markBlueprintAsAcceptedTestWithNullBlueprint() {
        when(mockBlueprintRepository.findById("1")).thenReturn(java.util.Optional.empty());

        boolean isAccepted = service.markBlueprintAsAccepted("1");

        Assertions.assertFalse(isAccepted);
    }


    @Test
    void DeleteBlueprintTest() {
        LogEntity log = new LogEntity();
        log.setBlueprint(current).setAction("action").setTime(LocalDateTime.now()).setUser(user);
        when(mockLogRepository.findByBlueprint_Id(any())).thenReturn(List.of(log));

        List<String> deletedLogs = service.deleteBlueprint("1");

        Assertions.assertEquals(current.getName(), deletedLogs.get(0));

    }

    @Test
    void extractBlueprintModelTest() {
        when(mockBlueprintRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(current));

        BlueprintServiceModel result = service.extractBlueprintModel("1");
        Assertions.assertEquals(current.getName(), result.getName());
        Assertions.assertEquals(current.getPromoter().getUsername(), result.getPromoter());
        Assertions.assertEquals(current.getActivityType().getActivityName(), result.getActivityType());
        Assertions.assertEquals(current.getNeededEquipment().getEquipmentName(), result.getNeededEquipment());
    }


    @Test
    void getDurationOfBlueprintTest() {
        when(mockBlueprintRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(current));

        int duration = service.getDurationOfBlueprint("1");
        Assertions.assertEquals(2, duration);

    }

    @Test
    void deleteBlueprintsOfUserTest() {

        BlueprintServiceImpl mockService = mock(BlueprintServiceImpl.class);
        mockService.deleteBlueprintsOfUser(user.getId());
        verify(mockService, times(1)).deleteBlueprintsOfUser(user.getId());

        when(mockBlueprintRepository.findAllByPromoterId(any())).thenReturn(List.of(current));
        List<String> result = service.deleteBlueprintsOfUser(user.getId());
        Assertions.assertEquals(current.getName(), result.get(0));
    }


    private Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        return equipment;
    }

    private ActivityType getActivityType() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        return activityType;
    }

    private UserEntity getUser() {
        UserEntity user = new UserEntity();
        user.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        return user;
    }


    private Blueprint getBlueprint() {

        Blueprint blueprint = new Blueprint();
        blueprint
                .setName("123")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(user);
        return blueprint;
    }

    private Premise getPremise() {
        Premise premise = new Premise();
        premise.setName("Monnet");
        premise.setEquipment(equipment);
        return premise;
    }

}
