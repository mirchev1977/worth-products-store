package com.w.prod.services.impl;

import com.w.prod.models.entity.*;
import com.w.prod.models.entity.enums.UserRole;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.service.UserRegistrationServiceModel;
import com.w.prod.models.view.UserViewModel;
import com.w.prod.repositories.LogRepository;
import com.w.prod.repositories.UserRepository;
import com.w.prod.repositories.UserRoleRepository;
import com.w.prod.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final WorthProductUserService worthProductUserService;
    private final LogRepository logRepository;

    public UserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, WorthProductUserService worthProductUserService, LogRepository logRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.worthProductUserService = worthProductUserService;
        this.logRepository = logRepository;
    }

    @Override
    public void initUsers() {
        if (userRepository.count() == 0) {
            UserRoleEntity adminRole = this.userRoleRepository.findByRole(UserRole.ADMIN).orElse(null);
            UserRoleEntity userRole = this.userRoleRepository.findByRole(UserRole.USER).orElse(null);

            UserEntity admin = new UserEntity()
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin_pass"))
                    .setEmail("admin@admin.com")
                    .setFirstName("Admin")
                    .setLastName("AdminLastName")
                    .setUserType(UserType.University);
            admin.setRoles(List.of(adminRole, userRole));
            UserEntity user = new UserEntity()
                    .setUsername("user")
                    .setPassword(passwordEncoder.encode("user_pass"))
                    .setFirstName("User")
                    .setLastName("UserLastName")
                    .setEmail("user@user.bg")
                    .setUserType(UserType.University);
            user.setRoles(List.of(userRole));
            userRepository.saveAll(List.of(user, admin));
        }
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {
        UserEntity newUser = modelMapper.map(userRegistrationServiceModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.USER).orElseThrow(() -> new IllegalStateException("User role not found. Please seed the roles."));
        newUser.addRole(userRole);
        newUser = userRepository.save(newUser);
        UserDetails principal = worthProductUserService.loadUserByUsername(newUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                newUser.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public void updateUser(UserEntity collaborator) {
        userRepository.save(collaborator);
    }

    @Override
    @PostFilter("!filterObject.username.equalsIgnoreCase(authentication.name)")
    public List<UserViewModel> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> {
                    UserViewModel userViewModel = modelMapper.map(u, UserViewModel.class);
                    userViewModel.setFullNameAndEmail(
                            String.format("%s %s <br /> %s", u.getFirstName(), u.getLastName(), u.getEmail()));
                    userViewModel.setCategoryAndType(
                            String.format("%s <br /> %s", u.getCategory(), u.getUserType()));

                    StringBuilder sb1 = new StringBuilder();
                    u.getOwnProducts()
                            .stream()
                            .filter(p -> p.isActive())
                            .forEach(p -> sb1.append(String.format("%s <br />", p.getName())));

                    userViewModel.setActiveProducts(sb1.toString());

                    StringBuilder sb2 = new StringBuilder();
                    u.getOwnProducts()
                            .stream()
                            .filter(p -> !p.isActive())
                            .forEach(p -> sb2.append(String.format("%s <br />", p.getName())));

                    userViewModel.setArchivesProducts(sb2.toString());

                    StringBuilder sb3 = new StringBuilder();
                    u.getProducts()
                            .stream()
                            .filter(p -> p.isActive())
                            .forEach(p -> sb3.append(String.format("%s <br />", p.getName())));

                    userViewModel.setActiveCollabs(sb3.toString());

                    StringBuilder sb4 = new StringBuilder();
                    u.getRoles()
                            .forEach(r -> sb4.append(String.format("%s <br />", r.getRole().name())));

                    userViewModel.setRoles(sb4.toString());

                    return userViewModel;
                }).collect(Collectors.toList());

    }

    @Override
    public List<LogEntity> deleteUser(String id) {

        UserEntity user = userRepository.getOne(id);
        user.getProducts().stream().forEach(p -> p.getCollaborators().remove(user));
        List<LogEntity> logs = logRepository.findByUser_Id(id);
        if (!logs.isEmpty()) {
            logs.forEach(l -> logRepository.delete(l));
        }
        userRepository.deleteById(id);
        return logs;
}

    @Override
    public Set<Product> getProductsByUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            UserEntity user = userRepository.findByUsername(username).get();
            Set<Product> products = user.getProducts();
            return user.getProducts();
        } else {
            throw new IllegalArgumentException("No user with such username");
        }
    }

    @Override
    @PostFilter("!filterObject.equalsIgnoreCase(authentication.name)")
    public Set<String> findAllUsernamesExceptCurrent() {
        return userRepository
                .findAll()
                .stream()
                .map(u -> u.getUsername())
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserRoleEntity> changeRole(String username, List<String> roles) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        List<UserRoleEntity> newRoleList = new ArrayList<>();
        roles.forEach(r -> {
            UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.valueOf(r.toUpperCase())).orElseThrow(() -> new IllegalStateException("User role not found. Please seed the roles."));
            newRoleList.add(userRole);
        });
        user.setRoles(newRoleList);
        userRepository.save(user);
        return newRoleList;
    }

}



