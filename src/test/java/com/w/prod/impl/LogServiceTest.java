package com.w.prod.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.view.AddBlueprintLogViewModel;
import com.w.prod.models.view.JoinProductLogViewModel;
import com.w.prod.repositories.*;
import com.w.prod.services.BlueprintService;
import com.w.prod.services.PremiseService;
import com.w.prod.services.ProductService;
import com.w.prod.services.UserService;
import com.w.prod.services.impl.WorthProductUserService;
import com.w.prod.services.impl.LogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    LogServiceImpl service;

    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;
    @Mock
    ProductRepository mockProductRepository;
    @Mock
    BlueprintRepository mockBlueprintRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;
    @Mock
    PremiseRepository mockPremiseRepository;
    @Mock
    ActivityTypeRepository mockActivityTypeRepository;

    @Autowired
    WorthProductUserService worthProductUserService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    BlueprintService blueprintService;
    @Autowired
    PremiseService premiseService;
    LogEntity firstLog;
    LogEntity secondLog;
    LogEntity thirdLog;
    LogEntity fourthLog;


    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        this.service = new LogServiceImpl(mockLogRepository, userService, productService, blueprintService, modelMapper);
        firstLog = getLogs().get(0);
        secondLog = getLogs().get(1);
        thirdLog = getLogs().get(2);
        fourthLog = getLogs().get(3);
    }

    @Test
    void testFindAllBlueprintAddLogs() {
        when(mockLogRepository.findAllByBlueprintNotNullOrderByTimeDesc()).thenReturn(List.of(fourthLog, thirdLog));
        List<AddBlueprintLogViewModel> result = service.findAllBlueprintAddLogs();

        Assertions.assertEquals("pesho", result.get(0).getUser());
        Assertions.assertEquals("pesho", result.get(1).getUser());
        Assertions.assertEquals("012", result.get(0).getBlueprint());
        Assertions.assertEquals("789", result.get(1).getBlueprint());
        Assertions.assertEquals("18 MARCH 2021 (10:00)", result.get(0).getDateTime());
        Assertions.assertEquals("AddBlueprint", result.get(0).getAction());
    }

    @Test
    void testFindAllJoinProductLogs() {
        when(mockLogRepository.findAllByProductNotNullOrderByTimeDesc()).thenReturn(List.of(secondLog, firstLog));
        List<JoinProductLogViewModel> result = service.findAllJoinProductLogs();

        Assertions.assertEquals("pesho", result.get(0).getUser());
        Assertions.assertEquals("pesho", result.get(1).getUser());
        Assertions.assertEquals("456", result.get(0).getProduct());
        Assertions.assertEquals("123", result.get(1).getProduct());
        Assertions.assertEquals("19 MARCH 2021 (10:00)", result.get(0).getDateTime());
        Assertions.assertEquals("JoinProduct", result.get(0).getAction());
    }

    @Test
    void testGetStatsJoinProductActivity() {
        when(mockLogRepository.findAllByProductNotNullOrderByTimeDesc()).thenReturn(List.of(secondLog, firstLog));
        Map<Integer, Integer> result = service.getStatsJoinProductActivity();
        int dayOfWeek2 = secondLog.getTime().getDayOfWeek().getValue();
        int dayOfWeek1 = firstLog.getTime().getDayOfWeek().getValue();

        Assertions.assertEquals(1, result.get(dayOfWeek2));
        Assertions.assertEquals(1, result.get(dayOfWeek1));
    }

    @Test
    void testGetStatsBlueprintsCreated() {
        when(mockLogRepository.findAllByBlueprintNotNullOrderByTimeDesc()).thenReturn(List.of(fourthLog, thirdLog));
        Map<Integer, Integer> result = service.getStatsBlueprintsCreated();
        int dayOfWeek2 = fourthLog.getTime().getDayOfWeek().getValue();
        int dayOfWeek1 = thirdLog.getTime().getDayOfWeek().getValue();

        Assertions.assertEquals(1, result.get(dayOfWeek2));
        Assertions.assertEquals(1, result.get(dayOfWeek1));
    }

    //    Initialization methods

    private List<LogEntity> getLogs() {
        UserEntity firstUser = new UserEntity();
        firstUser.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Wood Workshop");
        Premise premise = new Premise();
        premise.setName("Blueprinttion");
        premise.setEquipment(equipment);
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");

        Product firstProduct = new Product();
        firstProduct
                .setName("123")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        Product secondProduct = new Product();
        secondProduct
                .setName("456")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        Blueprint firstBlueprint = new Blueprint();
        firstBlueprint
                .setName("789")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(firstUser);
        Blueprint secondBlueprint = new Blueprint();
        secondBlueprint
                .setName("012")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(firstUser);

        LogEntity firstLog = new LogEntity();
        firstLog
                .setProduct(firstProduct)
                .setUser(firstUser)
                .setAction("JoinProduct")
                .setTime(LocalDateTime.of(2021, 3, 17, 10, 0));

        LogEntity secondLog = new LogEntity();
        secondLog
                .setProduct(secondProduct)
                .setUser(firstUser)
                .setAction("JoinProduct")
                .setTime(LocalDateTime.of(2021, 3, 19, 10, 0));

        LogEntity thirdLog = new LogEntity();
        thirdLog
                .setBlueprint(firstBlueprint)
                .setUser(firstUser)
                .setAction("AddBlueprint")
                .setTime(LocalDateTime.of(2021, 3, 17, 10, 0));
        LogEntity fourthLog = new LogEntity();
        fourthLog
                .setBlueprint(secondBlueprint)
                .setUser(firstUser)
                .setAction("AddBlueprint")
                .setTime(LocalDateTime.of(2021, 3, 18, 10, 0));

        return List.of(firstLog, secondLog, thirdLog, fourthLog);
    }


}
