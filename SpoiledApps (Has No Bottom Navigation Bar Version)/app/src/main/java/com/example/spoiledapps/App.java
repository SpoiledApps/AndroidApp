package com.example.spoiledapps;

public class App {

    private String title;
    private String companyName;

    public App(String title, String name) {
        this.title = title;
        companyName = name;
    }

    public String toString() {
        return title + " " + companyName;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }
}
