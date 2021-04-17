package com.w.prod.services.impl;

import com.w.prod.models.entity.UserRoleEntity;
import com.w.prod.models.entity.enums.UserRole;
import com.w.prod.repositories.UserRoleRepository;
import com.w.prod.services.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;


    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void initUserRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);
            this.userRoleRepository.saveAll(List.of(adminRole, userRole));
        }

    }
}
