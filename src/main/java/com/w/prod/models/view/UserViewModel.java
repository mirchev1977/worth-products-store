package com.w.prod.models.view;

public class UserViewModel {

    private String id;
    private String username;
    private String fullNameAndEmail;
    private String activeProducts;
    private String archivesProducts;
    private String activeCollabs;
    private String categoryAndType;
    private String roles;

    public String getId() {
        return id;
    }

    public UserViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFullNameAndEmail() {
        return fullNameAndEmail;
    }

    public UserViewModel setFullNameAndEmail(String fullNameAndEmail) {
        this.fullNameAndEmail = fullNameAndEmail;
        return this;
    }

    public String getActiveProducts() {
        return activeProducts;
    }

    public UserViewModel setActiveProducts(String activeProducts) {
        this.activeProducts = activeProducts;
        return this;
    }

    public String getArchivesProducts() {
        return archivesProducts;
    }

    public UserViewModel setArchivesProducts(String archivesProducts) {
        this.archivesProducts = archivesProducts;
        return this;
    }

    public String getActiveCollabs() {
        return activeCollabs;
    }

    public UserViewModel setActiveCollabs(String activeCollabs) {
        this.activeCollabs = activeCollabs;
        return this;
    }

    public String getCategoryAndType() {
        return categoryAndType;
    }

    public UserViewModel setCategoryAndType(String categoryAndType) {
        this.categoryAndType = categoryAndType;
        return this;
    }

    public String getRoles() {
        return roles;
    }

    public UserViewModel setRoles(String roles) {
        this.roles = roles;
        return this;
    }
}
