package com.w.prod.services;

import com.w.prod.models.entity.LogEntity;
import com.w.prod.models.entity.Project;
import com.w.prod.models.entity.UserEntity;
import com.w.prod.models.entity.UserRoleEntity;
import com.w.prod.models.service.UserRegistrationServiceModel;
import com.w.prod.models.view.UserViewModel;

import java.util.List;
import java.util.Set;

public interface UserService {

    void initUsers();

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    boolean usernameExists(String username);

    UserEntity findByUsername(String userName);

    void updateUser(UserEntity collaborator);

    List<UserViewModel> getAll();

    List<LogEntity> deleteUser(String id);

    Set<Project> getProjectsByUser(String username);

    Set<String> findAllUsernamesExceptCurrent();

    List<UserRoleEntity> changeRole(String username, List<String> roles);
}
