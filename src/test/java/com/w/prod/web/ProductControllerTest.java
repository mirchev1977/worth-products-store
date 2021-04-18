package com.w.prod.web;

import com.w.prod.models.entity.Equipment;
import com.w.prod.models.entity.Premise;
import com.w.prod.models.entity.Product;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.repositories.*;
import com.w.prod.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProductControllerTest {

    private static final String PROJECT_CONTROLLER_PREFIX = "/products";
    Equipment equipment;
    Premise premise;
    String productId;
    String userId;
    ProductTestData testData;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ActivityTypeRepository activityTypeRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    PremiseRepository premiseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LogRepository logRepository;

    @BeforeEach
    public void setup() {
        testData = new ProductTestData(
                activityTypeRepository,
                equipmentRepository,
                productRepository,
                premiseRepository,
                userRepository,
                logRepository

        );
        testData.init();
        productId = testData.getProductId();
        userId = testData.getUserId();
        equipment = testData.getEquipment();
        premise = testData.getPremise();
    }

    @AfterEach
    public void tearDown() {
        testData.cleanUp();
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void ProductsAllShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("products-all1"));
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void ProductsOwnShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/owned"))
                .andExpect(status().isOk())
                .andExpect(view().name("products-own"))
                .andExpect(model().attributeExists("fullName"))
                .andExpect(model().attributeExists("productsOfUser"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void ProductDetailsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/details/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product-details"))
                .andExpect(model().attributeExists("isCollaborating"))
                .andExpect(model().attributeExists("current"))
                .andExpect(model().attributeExists("isOwner"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void ArchiveProductShouldReturnValidStatusAndShowProductAsInactive() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/archive/{id}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You archived a product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/all"));
        Assertions.assertFalse(productRepository.getOne(productId).isActive());
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void DeleteProductReturnsValidStatus() throws Exception {
        String url = "http://localhost:8080/products/delete/{productId}";
        mockMvc.perform(MockMvcRequestBuilders.get(url, productId))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/all"));

        Assertions.assertEquals(0, productRepository.count());

    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void JoinProductAddsUserToCollaboratorsAndProductToUserCollabs() throws Exception {
        String url = "http://localhost:8080/products/join/{productId}";
        String redirectUrl = String.format("/products/details/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.get(url, productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You are now a collaborator in the product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        UserEntity user = userRepository.getOne(userId);
        Product product = productRepository.getOne(productId);
        Assertions.assertTrue(productRepository.getOne(productId).getCollaborators().contains(user));
        Assertions.assertTrue(userRepository.getOne(userId).getProducts().contains(product));
    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void LeaveProductRemovesUserFromCollaboratorsAndProductFromUserCollabs() throws Exception {
        String url = "http://localhost:8080/products/leave/{productId}";
        String redirectUrl = String.format("/products/details/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.get(url, productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You are no longer a collaborator in the product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        UserEntity user = userRepository.getOne(userId);
        Product product = productRepository.getOne(productId);
        Assertions.assertFalse(productRepository.getOne(productId).getCollaborators().contains(user));
        Assertions.assertFalse(userRepository.getOne(userId).getProducts().contains(product));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProductReturnsValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/update/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product-update"))
                .andExpect(model().attributeExists("current", "premisesInfo"))
                .andExpect(model().attribute("duration", 1L));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProductPostUpdatesProductSuccessfullyIfValidData() throws Exception {
        String redirectUrl = String.format("/products/details/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/update/{id}", productId)
                .param("name", "12345")
                .param("category", "IT")
                .param("description", "123456789012")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-05")
                .param("endDate", "2021-05-06")
                .param("premise", "Tesla1")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You updated the product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Product product = productRepository.getOne(productId);
        Assertions.assertEquals(1, productRepository.count());
        Assertions.assertEquals("12345", product.getName());
        Assertions.assertEquals("123456789012", product.getDescription());
        Assertions.assertEquals(LocalDate.of(2021, 5, 5), product.getStartDate());
        Assertions.assertEquals(LocalDate.of(2021, 5, 6), product.getEndDate());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProductRedirectsIfInvalidData() throws Exception {
        String redirectUrl = String.format("/products/update/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/update/{id}", productId)
                .param("name", "1")
                .param("category", "IT")
                .param("description", "123456789")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-05T10:00")
                .param("endDate", "2021-05-03T10:00")
                .param("premise", "Tesla1")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("productAddBindingModel"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.productAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product-publish"))
                .andExpect(model().attribute("id", productId))
                .andExpect(model().attributeExists("current"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsPerformsSuccessfullyWithValidInput() throws Exception {
        String redirectUrl = String.format("/products/details/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", productId)
                .param("description", "aD011960678529315557020119606785293155570201196067852931555702")
                .param("category", "IT")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You published the results from the product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
        Assertions.assertEquals("aD011960678529315557020119606785293155570201196067852931555702", productRepository.getOne(productId).getResult());
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsRedfirectsWithInvalidInput() throws Exception {
        String redirectUrl = String.format("/products/publish/%s", productId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", productId)
                .param("description", "aD0119606785293155570201196067852931555702011")
                .param("category", "IT")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("productResultBindingModel"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.productResultBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
        Assertions.assertNull(productRepository.getOne(productId).getResult());
    }


}
