package com.w.prod.models.view;

public class AddBlueprintLogViewModel {

    private String id;

    private String user;

    private String action;

    private String blueprint;

    private String dateTime;

    public String getId() {
        return id;
    }

    public AddBlueprintLogViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public AddBlueprintLogViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public AddBlueprintLogViewModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getBlueprint() {
        return blueprint;
    }

    public AddBlueprintLogViewModel setBlueprint(String blueprint) {
        this.blueprint = blueprint;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public AddBlueprintLogViewModel setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
