package com.w.prod.init;

import com.w.prod.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final ActivityTypeService activityTypeService;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final PremiseService premiseService;
    private final EquipmentService equipmentService;

    public DatabaseInitializer(ActivityTypeService activityTypeService, UserRoleService userRoleService, UserService userService, PremiseService premiseService, EquipmentService equipmentService) {
        this.activityTypeService = activityTypeService;
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.premiseService = premiseService;
        this.equipmentService = equipmentService;
    }

    @Override
    public void run(String... args) throws Exception {
        userRoleService.initUserRoles();
        userService.initUsers();
        activityTypeService.seedActivityTypes();
        equipmentService.seedEquipment();
        premiseService.seedPremises();
    }


}
