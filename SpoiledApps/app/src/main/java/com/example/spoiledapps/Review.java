package com.example.spoiledapps;

public class Review {
    private String headline;
    private String generalReview;
    private String pros;
    private String cons;
    private String favoriteFeature;
    private String leastFavoriteFeature;
    private String authorID;
    private float rating;
    private int authorScore;

    public Review(String author, String headline, String generalReview, String pros, String cons, String favoriteFeature, String leastFavoriteFeature, float rating, int authorScore) {
        this.headline = headline;
        this.generalReview = generalReview;
        this.pros = pros;
        this.cons = cons;
        this.authorID = author;
        this.favoriteFeature = favoriteFeature;
        this.leastFavoriteFeature = leastFavoriteFeature;
        this.rating = rating;
        this.authorScore = authorScore;
    }

    public String toString() {
        return headline + " " + generalReview + " " + pros + " " + cons + " " + favoriteFeature + " " + leastFavoriteFeature + " " + rating;
    }

    public int getAuthorScore() { return authorScore; }

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

    public String getAuthorID() {
        return authorID;
    }

    public float getRating() {
        return rating;
    }
}
