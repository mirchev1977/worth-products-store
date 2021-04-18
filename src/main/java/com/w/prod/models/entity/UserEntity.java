package com.w.prod.models.entity;

import com.w.prod.models.entity.enums.Sector;
import com.w.prod.models.entity.enums.UserType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<UserRoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "promoter", targetEntity = Product.class, cascade = CascadeType.ALL)
    private Set<Product> ownProducts = new HashSet<>();


    @ManyToMany(mappedBy = "collaborators", targetEntity = Product.class)
    private Set<Product> products = new HashSet<>();

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserEntity setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public UserEntity setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public Set<Product> getOwnProducts() {
        return ownProducts;
    }

    public UserEntity setOwnProducts(Set<Product> ownProducts) {
        this.ownProducts = ownProducts;
        return this;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public UserEntity setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    public UserEntity addRole(UserRoleEntity roleEntity) {
        this.roles.add(roleEntity);
        return this;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}
