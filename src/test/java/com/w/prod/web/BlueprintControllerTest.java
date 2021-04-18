package com.w.prod.web;

import com.w.prod.models.entity.Equipment;
import com.w.prod.models.entity.Premise;
import com.w.prod.repositories.*;
import com.w.prod.services.CarouselService;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BlueprintControllerTest {

    private static final String IDEA_CONTROLLER_PREFIX = "/blueprints";
    private String testBlueprintId;
    private Equipment equipment;
    private BlueprintTestData blueprintTestData;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BlueprintRepository blueprintRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PremiseRepository premiseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private CarouselService carouselService;


    @BeforeEach
    public void setup() {
        blueprintTestData = new BlueprintTestData(
                blueprintRepository,
                activityTypeRepository,
                equipmentRepository,
                premiseRepository,
                userRepository,
                logRepository,
                productRepository
        );
        blueprintTestData.init();
        testBlueprintId = blueprintTestData.getTestBlueprintId();
        equipment = blueprintTestData.getEquipment();
    }

    @AfterEach
    public void tearDown() {
        blueprintTestData.cleanUp();
    }

    @Test
    void BlueprintsAllShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("blueprints-all"))
                .andExpect(model().attributeExists("blueprints"));
    }


    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void BlueprintDetailsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/details/{id}", testBlueprintId))
                .andExpect(status().isOk())
                .andExpect(view().name("blueprint-details"))
                .andExpect(model().attributeExists("current"));

    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AddShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("blueprint-add"))
                .andExpect(model().attributeExists("firstImg"))
                .andExpect(model().attributeExists("secondImg"))
                .andExpect(model().attributeExists("thirdImg"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AcceptBlueprintPostValidInput() throws Exception {
        Premise premise = new Premise();
        premise.setEquipment(equipment).setName("Monnet2");
        premiseRepository.saveAndFlush(premise);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testBlueprintId)
                .param("name", "123")
                .param("category", "IT")
                .param("description", "1234567890")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-03")
                .param("endDate", "2021-05-04")
                .param("premise", "Monnet2")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You created a product"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/blueprints/all"));

        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void addBlueprintInvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/add")
                .param("name", "12")
                .param("category", "")
                .param("description", "123456789")
                .param("duration", "0")
                .param("neededEquipment", "")
                .param("activityType", "")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.blueprintAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/blueprints/add"));
        Assertions.assertEquals(0, logRepository.count());
    }


    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AcceptBlueprintShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testBlueprintId))
                .andExpect(status().isOk())
                .andExpect(view().name("product-add"))
                .andExpect(model().attributeExists("blueprintServiceModel", "premises", "duration", "labsInfo"));
    }


    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void BlueprintAcceptPostInvalidInput() throws Exception {
        Premise premise = new Premise();
        premise.setName("Blueprinttion1");
        premise.setEquipment(equipment);
        premiseRepository.save(premise);

        String redirectUrl = String.format("/blueprints/accept/%s", testBlueprintId);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testBlueprintId)
                .param("name", "12")
                .param("category", "")
                .param("description", "123456789")
                .param("neededEquipment", "")
                .param("activityType", "")
                .param("startDate", "")
                .param("endDate", "")
                .param("premise", "")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.productAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void BlueprintAcceptPostInvalidDatesOnly() throws Exception {
        Premise premise = new Premise();
        premise.setName("Blueprinttion1");
        premise.setEquipment(equipment);
        premiseRepository.save(premise);

        String redirectUrl = String.format("/blueprints/accept/%s", testBlueprintId);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testBlueprintId)
                .param("name", "123")
                .param("category", "IT")
                .param("description", "1234567890")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Masterclass")
                .param("startDate", "2021-04-04")
                .param("endDate", "2021-04-03")
                .param("premise", "Blueprinttion")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.productAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Assertions.assertEquals(0, productRepository.count());
    }


    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})

    void addBlueprintValidInput() throws Exception {
        Premise premise = new Premise();
        premise.setName("Blueprinttion1");
        premise.setEquipment(equipment);
        premiseRepository.save(premise);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/add")
                .param("name", "123")
                .param("category", "Arts")
                .param("description", "1234567890")
                .param("duration", "1")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Your blueprint was added"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/blueprints/all"));

        Assertions.assertEquals(2, blueprintRepository.count());
        Assertions.assertEquals(1, logRepository.count());

    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void DeleteBlueprintReturnsValidStatus() throws Exception {
        String url = "http://localhost:8080/blueprints/delete/{testBlueprintId}";
        mockMvc.perform(MockMvcRequestBuilders.get(url, testBlueprintId))
                .andExpect(status().isFound());

        Assertions.assertEquals(0, blueprintRepository.count());
    }

}
