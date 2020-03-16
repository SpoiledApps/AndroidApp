package com.example.spoiledapps.data;

public class Person {
    private String name;
    private String userName;
    private String password;
    private String emailAddress;
    private int numReviews;
    private double reputationRating;

    public Person (String a, String b, String c, String d, int e, double f){
        this.name=a;
        this.userName=b;
        this.password=c;
        this.emailAddress=d;
        this.numReviews=e;
        this.reputationRating=f;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String a){
        this.name=a;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String a){
        this.userName=a;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String a){
        this.password=a;
    }
    public String getEmailAddress(){
        return this.emailAddress;
    }
    public void setEmailAddress(String a){
        this.emailAddress=a;
    }
    public int getNumReview(){
        return this.numReviews;
    }
    public void setNumReview(int a){
        this.numReviews=a;
    }
    public double getReputationRating(){
        return this.reputationRating;
    }
    public void setReputationRating(double a){
        this.reputationRating=a;
    }
}