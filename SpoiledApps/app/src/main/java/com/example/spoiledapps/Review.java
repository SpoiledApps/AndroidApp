package com.example.spoiledapps;

public class Review {
    private String headline;
    private String generalReview;
    private String pros;
    private String cons;
    private String favoriteFeature;
    private String leastFavoriteFeature;
    private float rating;

    public Review(String headline, String generalReview, String pros, String cons, String favoriteFeature, String leastFavoriteFeature, float rating) {
        this.headline = headline;
        this.generalReview = generalReview;
        this.pros = pros;
        this.cons = cons;
        this.favoriteFeature = favoriteFeature;
        this.leastFavoriteFeature = leastFavoriteFeature;
        this.rating = rating;
    }

    public String toString() {
        return headline + " " + generalReview + " " + pros + " " + cons + " " + favoriteFeature + " " + leastFavoriteFeature + " " + rating;
    }

    public String getHeadline() {
        return headline;
    }

    public String getGeneralReview() {
        return generalReview;
    }

    public String getPros() {
        return pros;
    }

    public String getCons() {
        return cons;
    }

    public String getFavoriteFeature() {
        return favoriteFeature;
    }

    public String getLeastFavoriteFeature() {
        return leastFavoriteFeature;
    }

    public float getRating() {
        return rating;
    }
}
