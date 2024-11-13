package com.ecosort.ecosortkiddo.model;

public class Garbage {
    private int id;
    private String dateCreated;
    private Double layoutConstraintHorizontalBias;
    private Double layoutConstraintVerticalBias;
    private boolean active;
    private int levelId;
    private int garbagecategoryId;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private int locationId;
    public Garbage(){
        super();
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Double getLayoutConstraintHorizontalBias() {
        return layoutConstraintHorizontalBias;
    }

    public void setLayoutConstraintHorizontalBias(Double layoutConstraintHorizontalBias) {
        this.layoutConstraintHorizontalBias = layoutConstraintHorizontalBias;
    }

    public Double getLayoutConstraintVerticalBias() {
        return layoutConstraintVerticalBias;
    }

    public void setLayoutConstraintVerticalBias(Double layoutConstraintVerticalBias) {
        this.layoutConstraintVerticalBias = layoutConstraintVerticalBias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getGarbagecategoryId() {
        return garbagecategoryId;
    }

    public void setGarbagecategoryId(int garbagecategoryId) {
        this.garbagecategoryId = garbagecategoryId;
    }

    @Override
    public String toString() {
        return "Garbage{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", layoutConstraintHorizontalBias=" + layoutConstraintHorizontalBias +
                ", layoutConstraintVerticalBias=" + layoutConstraintVerticalBias +
                ", active=" + active +
                ", levelId=" + levelId +
                ", locationId=" + locationId +
                ", garbagecategoryId=" + garbagecategoryId +
                '}';
    }
}