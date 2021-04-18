package com.w.prod.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserRole;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.view.UserViewModel;
import com.w.prod.repositories.LogRepository;
import com.w.prod.repositories.UserRepository;
import com.w.prod.repositories.UserRoleRepository;
import com.w.prod.services.impl.WorthProductUserService;
import com.w.prod.services.impl.UserServiceImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    UserServiceImpl service;
    ModelMapper modelMapper;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;
    @Mock
    UserEntity mockUserEntity;


    WorthProductUserService worthProductUserService;
    @Autowired
    PasswordEncoder passwordEncoder;

    UserEntity firstUser;
    UserEntity secondUser;
    Equipment equipment;
    Premise premise;
    ActivityType activityType;
    Product firstProduct;
    Product secondProduct;
    Product thirdProduct;
    Product fourthProduct;
    Product fifthProduct;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        firstUser = this.getFirstUser();
        secondUser = this.getSecondUser();
        worthProductUserService = new WorthProductUserService(mockUserRepository);
        this.service = new UserServiceImpl(mockUserRoleRepository, mockUserRepository, passwordEncoder, modelMapper, worthProductUserService, mockLogRepository);
        equipment = getEquipment();
        premise = getPremise();
        activityType = getActivityType();
        firstProduct = getProducts().get(0);
        secondProduct = getProducts().get(1);
        thirdProduct = getProducts().get(2);
        fourthProduct = getProducts().get(3);
        fifthProduct = getProducts().get(4);
    }


    @Test
    void testUsernameExists() {

        when(mockUserRepository.findByUsername("pesho")).thenReturn(java.util.Optional.ofNullable(firstUser));
        boolean result = service.usernameExists("pesho");
        Assertions.assertTrue(result);
    }

    @Test
    void testFindByUsername() {

        when(mockUserRepository.findByUsername("pesho")).thenReturn(java.util.Optional.ofNullable(firstUser));
        UserEntity result = service.findByUsername("pesho");
        Assertions.assertEquals("pesho", result.getUsername());
        Assertions.assertEquals("pesho@pesho.bg", result.getEmail());
        Assertions.assertEquals("123456789", result.getPassword());
    }

    @Test
    void testGetAll() {
        firstUser.setOwnProducts(Set.of(firstProduct, secondProduct)).setProducts(Set.of(thirdProduct, fourthProduct, fifthProduct));
        secondUser.setOwnProducts(Set.of(thirdProduct, fourthProduct, fifthProduct)).setProducts(Set.of(firstProduct, secondProduct));
        when(mockUserRepository.findAll()).thenReturn(List.of(firstUser, secondUser));

        List<UserViewModel> result = service.getAll();

        Assertions.assertEquals("pesho", result.get(0).getUsername());
        Assertions.assertEquals("gosho", result.get(1).getUsername());
        Assertions.assertEquals("Pesho Peshov <br /> pesho@pesho.bg", result.get(0).getFullNameAndEmail());
        Assertions.assertTrue(result.get(0).getActiveProducts().contains("123"));
        Assertions.assertTrue(result.get(0).getActiveProducts().contains("456"));
        Assertions.assertTrue(result.get(0).getActiveCollabs().contains("789"));
        Assertions.assertTrue(result.get(0).getActiveCollabs().contains("012"));
        Assertions.assertEquals("Arts <br /> Company", result.get(0).getCategoryAndType());
        Assertions.assertTrue(result.get(1).getActiveCollabs().contains("123"));
        Assertions.assertTrue(result.get(1).getActiveCollabs().contains("456"));
        Assertions.assertTrue(result.get(1).getActiveProducts().contains("789"));
        Assertions.assertTrue(result.get(1).getActiveProducts().contains("012"));
        Assertions.assertEquals("Archived <br />", result.get(1).getArchivesProducts());
    }

    @Test
    void testDeleteUser() {
        LogEntity log1 = new LogEntity();
        log1.setUser(firstUser);
        log1.setProduct(thirdProduct);
        LogEntity log2 = new LogEntity();
        log2.setUser(firstUser);
        log2.setProduct(fourthProduct);

        when(mockUserRepository.getOne(any())).thenReturn(firstUser);
        when(mockLogRepository.findByUser_Id(any())).thenReturn(List.of(log1, log2));

        List<LogEntity> result = service.deleteUser("1");

        Assertions.assertEquals("789", result.get(0).getProduct().getName());
        Assertions.assertEquals("012", result.get(1).getProduct().getName());
    }

    @Test
    void testGetProductsByUser() {

        firstUser.setProducts(Set.of(firstProduct, secondProduct));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));

        Set<Product> result = service.getProductsByUser("pesho");

        Assertions.assertTrue(result.contains(firstProduct));
        Assertions.assertTrue(result.contains(secondProduct));
    }

    @Test
    void testFindAllUsersExceptCurrent (){
        when(mockUserRepository.findAll()).thenReturn(List.of(firstUser, secondUser));
        Set<String> result = service.findAllUsernamesExceptCurrent();
        Assertions.assertTrue(result.contains(firstUser.getUsername()));
        Assertions.assertTrue(result.contains(secondUser.getUsername()));
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testChangeRole (){
        UserRoleEntity admin = new UserRoleEntity().setRole(UserRole.ADMIN);
        UserRoleEntity user = new UserRoleEntity().setRole(UserRole.USER);
        List<String> newRoles = List.of("ADMIN", "USER");
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));
        when(mockUserRoleRepository.findByRole(UserRole.ADMIN)).thenReturn(java.util.Optional.ofNullable(admin));
        when(mockUserRoleRepository.findByRole(UserRole.USER)).thenReturn(java.util.Optional.ofNullable(user));

        List<UserRoleEntity> result =  service.changeRole("1", newRoles);
        Assertions.assertTrue(result.contains(admin));
        Assertions.assertTrue(result.contains(user));
    }

    //    Initialization methods

    private UserEntity getFirstUser() {
        UserEntity firstUser = new UserEntity();
        firstUser.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        return firstUser;
    }


    private UserEntity getSecondUser() {
        UserEntity secondUser = new UserEntity();
        secondUser.setUsername("gosho")
                .setPassword("123456789")
                .setFirstName("Gosho")
                .setLastName("Goshov")
                .setEmail("gosho@gosho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        return secondUser;
    }

    private Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Wood Workshop");
        return equipment;
    }

    private Premise getPremise() {
        Premise premise = new Premise();
        premise.setName("Blueprinttion");
        premise.setEquipment(equipment);
        return premise;
    }

    private ActivityType getActivityType() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        return activityType;
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Product firstProduct = new Product();
        firstProduct
                .setName("123")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setCollaborators(Set.of(secondUser))
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
                .setCollaborators(Set.of(secondUser))
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        Product thirdProduct = new Product();
        thirdProduct
                .setName("789")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));
        Product fourthProduct = new Product();
        fourthProduct
                .setName("012")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        Product fifthProduct = new Product();
        fifthProduct
                .setName("Archived")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setActive(false)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);
        products.add(fourthProduct);
        products.add(fifthProduct);
        return products;
    }
}



