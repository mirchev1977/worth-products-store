package com.w.prod.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.service.ProductResultServiceModel;
import com.w.prod.models.service.ProductServiceModel;
import com.w.prod.models.view.ProductBasicViewModel;
import com.w.prod.models.view.ProductDetailedViewModel;
import com.w.prod.models.view.ProductResultViewModel;
import com.w.prod.repositories.*;
import com.w.prod.services.PremiseService;
import com.w.prod.services.UserService;
import com.w.prod.services.impl.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    ProductServiceImpl service;
    ModelMapper modelMapper;
    UserService userService;
    PremiseService premiseService;
    @Mock
    ProductRepository mockProductRepository;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;
    @Mock
    PremiseRepository mockPremiseRepository;
    @Mock
    ActivityTypeRepository mockActivityTypeRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;


    @InjectMocks
    EquipmentServiceImpl equipmentService;
    @InjectMocks
    WorthProductUserService worthProductUserService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("classpath:init/premises.json")
    Resource premisesRes;
    @Autowired
    Gson gson;
    @Mock
    UserEntity mockUserEntity;
    @Mock
    Product mockProduct;

    Equipment equipment;
    Premise premise;
    Product firstProduct;
    Product secondProduct;
    UserEntity firstUser;
    UserEntity secondUser;
    ActivityType activityType;
    ProductServiceModel productServiceModel;
    ProductBasicViewModel productBasicViewModel;
    ProductDetailedViewModel productDetailedViewModel;
    ProductResultServiceModel productResultServiceModel;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        Product mockProduct = mock(Product.class);
        equipment = this.getEquipment();
        premise = this.getPremise();
        activityType = this.getActivityType();
        firstUser = this.getFirstUser();
        firstProduct = this.getFirstProduct();
        secondProduct = this.getSecondProduct();
        secondUser = this.getSecondUser();
        productServiceModel = new ProductServiceModel();
        productBasicViewModel = new ProductBasicViewModel();
        productDetailedViewModel = new ProductDetailedViewModel();
        productResultServiceModel = new ProductResultServiceModel();

        userService = new UserServiceImpl(mockUserRoleRepository, mockUserRepository, passwordEncoder, modelMapper, worthProductUserService, mockLogRepository);
        premiseService = new PremiseServiceImpl(premisesRes, gson, mockPremiseRepository, equipmentService, modelMapper);

        this.service = new ProductServiceImpl(mockProductRepository, mockLogRepository,
                modelMapper, userService, mockActivityTypeRepository, premiseService,
                mockEquipmentRepository);
    }


    @Test
    void testAddedProduct() {

        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(firstUser));
        when(mockPremiseRepository.findByName(any())).thenReturn(Optional.ofNullable(premise));
        productServiceModel
                .setName("000")
                .setActivityType("Lecture")
                .setDescription("1234567890")
                .setCategory(Category.IT)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho")
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        ProductBasicViewModel viewModel = service.createProduct(productServiceModel);

        Assertions.assertEquals("000", viewModel.getName());
        Assertions.assertEquals("2021-05-16", viewModel.getStartDate());

    }

    @Test
    void testGetActiveProductsOrderedByStartDate() {

        String product2StartDate = convertDate(secondProduct);
        String currentStartDate = convertDate(firstProduct);

        when(mockProductRepository.findAllByActiveTrueOrderByStartDateAsc()).thenReturn(List.of(secondProduct, firstProduct));

        List<ProductBasicViewModel> result = service.getActiveProductsOrderedbyStartDate();

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(product2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void testExtractProductModel() {
        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(firstUser));
        String duration = String.format("%02d %s %s - %02d %s %s <br />",
                firstProduct.getStartDate().getDayOfMonth(), firstProduct.getStartDate().getMonth(), firstProduct.getStartDate().getYear(),
                firstProduct.getEndDate().getDayOfMonth(), firstProduct.getEndDate().getMonth(), firstProduct.getEndDate().getYear());

        ProductDetailedViewModel model = service.extractProductModel("1");
        Assertions.assertEquals("123", model.getName());
        Assertions.assertEquals("1234567890", model.getDescription());
        Assertions.assertEquals("IT", model.getCategory().name());
        Assertions.assertEquals("Pesho Peshov", model.getPromoter());
        Assertions.assertEquals(duration, model.getDuration());
        Assertions.assertEquals("Carnegie1", model.getPremise());
    }


    @Test
    void testDeleteProduct() {
        firstProduct.setId("1");
        LogEntity log1 = new LogEntity();
        log1.setProduct(firstProduct);
        LogEntity log2 = new LogEntity();
        log2.setProduct(secondProduct);
        when(mockLogRepository.findByProduct_Id(any())).thenReturn(List.of(log1, log2));

        List<String> result = service.deleteProduct("1");

        Assertions.assertEquals("123", result.get(0));
        Assertions.assertEquals("000", result.get(1));


    }

    @Test
    void testGetUserProductsOrderedByStartDate() {

        String product2StartDate = convertDate(secondProduct);
        String currentStartDate = convertDate(firstProduct);

        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(firstUser));
        when(mockProductRepository.findAllByActiveAndPromoterOrderByStartDate(true, firstUser)).thenReturn(List.of(secondProduct, firstProduct));

        List<ProductBasicViewModel> result = service.getUserProductsOrderedByStartDate("1");

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(product2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void testGetUserCollaborationsOrderedByStartDate() {
        secondUser.setProducts(Set.of(firstProduct, secondProduct));

        String product2StartDate = convertDate(secondProduct);
        String currentStartDate = convertDate(firstProduct);

        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.of(secondUser));

        List<ProductBasicViewModel> result = service.getUserCollaborationsOrderedByStartDate(secondUser.getUsername());

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(product2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void verifyDeleteProductsOfUser() {

        ProductServiceImpl mockService = mock(ProductServiceImpl.class);

        mockService.deleteProductsOfUser(firstUser.getId());
        doNothing().when(mockService).deleteProductsOfUser(isA(String.class));

        mockService.deleteProductsOfUser("1");
        verify(mockService, times(1)).deleteProductsOfUser("1");

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(mockService).deleteProductsOfUser(valueCapture.capture());
        mockService.deleteProductsOfUser("1");

        Assertions.assertEquals("1", valueCapture.getValue());

    }

    @Test
    void testFindProductById() {
        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        ProductServiceModel result = service.findProductById("1");

        Assertions.assertEquals("123", result.getName());
        Assertions.assertEquals("pesho", result.getPromoter());
    }

    @Test
    void testArchiveProduct() {
        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        service.archiveProduct("1");

        Assertions.assertFalse(firstProduct.isActive());
    }

    @Test
    void testFindProductOwnerStr() {
        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        String owner = service.findProductOwnerStr("1");

        Assertions.assertEquals("pesho", owner);
    }

    @Test
    void testJoinProduct() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));

        boolean result = service.joinProduct("123", "pesho");
        Set<Product> userProducts = secondUser.getProducts();
        Set<UserEntity> productCollaborators = firstProduct.getCollaborators();

        Assertions.assertTrue(result);
        Assertions.assertTrue(userProducts.contains(firstProduct));
        Assertions.assertTrue(productCollaborators.contains(secondUser));
    }
    @Test
    void testJoinProductThrowsWhenProductNotExisting() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProductRepository.findById(any())).thenReturn(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.joinProduct("123", "pesho");
        });

        String expectedMessage = "User could not join product";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testLeaveProduct() {
        secondUser.addProduct(firstProduct);
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));

        service.leaveProduct("123", "pesho");
        Set<Product> userProducts = secondUser.getProducts();
        Set<UserEntity> productCollaborators = firstProduct.getCollaborators();

        Assertions.assertFalse(userProducts.contains(firstProduct));
        Assertions.assertFalse(productCollaborators.contains(secondUser));
    }

    @Test
    void testLeaveProductThrowsWhenProductNotExisting() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProductRepository.findById(any())).thenReturn(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.leaveProduct("123", "pesho");
        });

        String expectedMessage = "User could not leave product";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCheckIfCollaborating() {
        secondUser.addProduct(firstProduct);
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));
        boolean result = service.checkIfCollaborating("123", "pesho");
        Assertions.assertTrue(result);
    }

    @Test
    void testExtractProductServiceModel() {
        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));
        ProductServiceModel result = service.extractProductServiceModel("1");

        Assertions.assertEquals("123", result.getName());
        Assertions.assertEquals("1234567890", result.getDescription());
        Assertions.assertEquals("pesho", result.getPromoter());
        Assertions.assertEquals("Carnegie1", result.getPremise());
        Assertions.assertEquals(LocalDate.of(2021, 5, 16), result.getStartDate());
    }

    @Test
    void testGetProductPromoter() {
        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        String result = service.getProductPromoter("1");
        Assertions.assertEquals("pesho", result);
    }

    @Test
    void testUpdateProduct() {
        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(Optional.ofNullable(equipment));
        when(mockPremiseRepository.findByName(any())).thenReturn(Optional.ofNullable(premise));
        productServiceModel
                .setName("000")
                .setActivityType("Lecture")
                .setDescription("updated1234")
                .setCategory(Category.Arts)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho")
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        service.updateProduct("1", productServiceModel);

        Assertions.assertEquals("000", firstProduct.getName());
        Assertions.assertEquals("updated1234", firstProduct.getDescription());
        Assertions.assertEquals("Arts", firstProduct.getCategory().name());
        Assertions.assertEquals("Lecture", firstProduct.getActivityType().getActivityName());
        Assertions.assertEquals(LocalDate.of(2021, 5, 16), firstProduct.getStartDate());
        Assertions.assertEquals(LocalDate.of(2021, 5, 17), firstProduct.getEndDate());
    }


    @Test
    void testPublishProductResult() {
        productResultServiceModel
                .setName("123")
                .setDescription("12345678901234567890123456789012345678901234567890");

        when(mockProductRepository.getOne(any())).thenReturn(firstProduct);
        service.publishProductResult(productResultServiceModel);
        Assertions.assertEquals("12345678901234567890123456789012345678901234567890", firstProduct.getResult());
    }
    @Test
    void testExtractProductResultServiceModel() {

        when(mockProductRepository.findById(any())).thenReturn(Optional.ofNullable(firstProduct));

        ProductResultServiceModel resultServiceModel = service.extractProductResultServiceModel("1");
        Assertions.assertEquals("123", resultServiceModel.getName());
        Assertions.assertEquals("IT", resultServiceModel.getCategory().name());

    }

    @Test
    void testGetResults() {
        firstProduct.setResult("12345678901234567890123456789012345678901234567890");
        secondProduct.setResult("Another12345678901234567890123456789012345678901234567890");
        when(mockProductRepository.findAllResultsByCategory(any())).thenReturn(List.of(firstProduct, secondProduct));

        List<ProductResultViewModel> result = service.getResults("IT");
        Assertions.assertEquals("12345678901234567890123456789012345678901234567890", result.get(0).getResult());
        Assertions.assertEquals("123", result.get(0).getName());
        Assertions.assertEquals("Another12345678901234567890123456789012345678901234567890", result.get(1).getResult());
        Assertions.assertEquals("000", result.get(1).getName());
    }
    @Test
    void testGetDurationInDays() {
        productServiceModel
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));

        long result = service.getDurationInDays(productServiceModel);
        Assertions.assertEquals(1L, result);


    }

    //    Initialization methods
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

    private UserEntity getFirstUser() {
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

    private Product getFirstProduct() {

        Product product = new Product();
        product
                .setName("123")
                .setCategory(Category.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setActive(true)
                .setPromoter(firstUser)
                .setStartDate(LocalDate.of(2021, 5, 16))
                .setEndDate(LocalDate.of(2021, 5, 17));
        return product;
    }

    private Product getSecondProduct() {
        Product product2 = new Product();
        product2
                .setName("000")
                .setCategory(Category.IT)
                .setDescription("1234567890000")
                .setNeededEquipment(equipment)
                .setPremise(premise)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setStartDate(LocalDate.of(2021, 5, 10))
                .setEndDate(LocalDate.of(2021, 5, 12));
        return product2;
    }

    private UserEntity getSecondUser() {
        UserEntity user2 = new UserEntity();
        user2.setUsername("gosho")
                .setPassword("123456789")
                .setFirstName("Gosho")
                .setLastName("Goshov")
                .setEmail("gosho@gosho.bg")
                .setCategory(Category.Arts)
                .setUserType(UserType.Company);
        return user2;
    }

    private Premise getPremise() {
        Premise premise = new Premise();
        premise.setName("Carnegie1");
        premise.setEquipment(equipment);
        return premise;
    }

    private String convertDate(Product product) {
        return String.format("%02d %s %s",
                product.getStartDate().getDayOfMonth(), product.getStartDate().getMonth(),
                product.getStartDate().getYear());
    }

}
