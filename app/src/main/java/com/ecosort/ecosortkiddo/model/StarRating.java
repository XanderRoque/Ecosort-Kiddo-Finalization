package com.ecosort.ecosortkiddo.model;

public class StarRating {
    private int starRatingId;
    private Double dateCreated;
    private int profileId;
    private int levelId;
    private int stars;

    public static final int TOTAL_STARS_HOME =18;
    public static final int TOTAL_STARS_BACKYARD =18;
    public static final int TOTAL_STARS_FOREST =18;
    public static final int TOTAL_STARS_BEACH =18;
    //TODO: add pa yung 3

    private int locationId;

    public StarRating(){
        super();
    }
    public StarRating(int starRatingId, Double dateCreated, int profileId, int levelId, int stars, int locationId) {
        this.starRatingId = starRatingId;
        this.dateCreated = dateCreated;
        this.profileId = profileId;
        this.levelId = levelId;
        this.stars = stars;
        this.locationId = locationId;
    }

    // Getters and setters for each field
    public int getStarRatingId() {
        return starRatingId;
    }

    public void setStarRatingId(int starRatingId) {
        this.starRatingId = starRatingId;
    }

    public Double getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Double dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}