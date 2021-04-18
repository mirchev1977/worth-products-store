package com.w.prod.models.view;

public class JoinProductLogViewModel {

    private String id;

    private String user;

    private String action;

    private String product;

    private String dateTime;

    public String getId() {
        return id;
    }

    public JoinProductLogViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public JoinProductLogViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public JoinProductLogViewModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public JoinProductLogViewModel setProduct(String product) {
        this.product = product;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public JoinProductLogViewModel setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
