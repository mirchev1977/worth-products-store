package com.w.prod.service;

import com.w.prod.model.service.UserServiceModel;

public interface UserService {
    void register(UserServiceModel userServiceModel);

    UserServiceModel findByUsernameAndPassword(String username, String password);
}
