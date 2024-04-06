package com.example.finalproject.models;
public class Game {

    private String name;
    private String type;
    private String launch_Date;
    private String develop_Comp;
    private String description;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLaunch_Date() {
        return launch_Date;
    }

    public void setLaunch_Date(String launch_Date) {
        this.launch_Date = launch_Date;
    }

    public String getDevelop_Comp() {
        return develop_Comp;
    }

    public void setDevelop_Comp(String develop_Comp) {
        this.develop_Comp = develop_Comp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game(String name, String type, String launch_Date, String develop_Comp, String description) {
        this.name = name;
        this.type = type;
        this.launch_Date = launch_Date;
        this.develop_Comp = develop_Comp;
        this.description = description;
    }

    public Game(){}
}
