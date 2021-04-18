package com.w.prod.models.binding;

import com.w.prod.models.entity.enums.Category;
import com.w.prod.models.entity.enums.UserType;
import com.w.prod.models.validator.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@FieldMatch(
        first = "password",
        second = "confirmPassword"
)
public class UserRegistrationBindingModel {
    @NotEmpty
    @Size(min=3, max = 20)
    private String username;

    private String firstName;

    @NotEmpty
    @Size(min=3, max = 20)
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min=5, max = 20)
    private String password;

    @NotEmpty
    @Size(min=5, max = 20)
    private String confirmPassword;

    @NotNull
    private UserType userType;

    private Category category;



    public String getUsername() {
        return username;
    }

    public UserRegistrationBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegistrationBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegistrationBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegistrationBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegistrationBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserRegistrationBindingModel setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public UserRegistrationBindingModel setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegistrationBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
