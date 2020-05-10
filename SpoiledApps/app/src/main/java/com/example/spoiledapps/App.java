package com.example.spoiledapps;

public class App {

    private String title;
    private String companyName;
    private String documentTag;

    public App(String title, String name, String tag) {
        this.title = title;
        companyName = name;
        documentTag = tag;
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

    public String getDocumentTag() { return documentTag; }
}
