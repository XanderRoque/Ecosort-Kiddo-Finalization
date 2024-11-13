package com.ecosort.ecosortkiddo.model;

public class Profile {

    //public static String TABLE_NAME = "profile";
    public static final int DEFAULT_PROFILE = 1;

    private Integer profileId;
    private String name;
    private Integer avatarId;
    private Integer achievement;
    private Integer tutorialDoneLocation1;
    private Integer tutorialDoneLocation2;
    private Integer tutorialDoneLocation3;
    private Integer tutorialDoneLocation4;
    private Integer isLocationCompletedHouse;
    private Integer isLocationCompletedBackyard;
    private Integer isLocationCompletedForest;
    private Integer isLocationCompletedBeach;

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public Integer getAchievement() {
        return achievement;
    }

    public void setAchievement(Integer achievement) {
        this.achievement = achievement;
    }

    public Integer getTutorialDoneLocation1() {
        return tutorialDoneLocation1;
    }

    public void setTutorialDoneLocation1(Integer tutorialDoneLocation1) {this.tutorialDoneLocation1 = tutorialDoneLocation1;}

    public Integer getTutorialDoneLocation2() {
        return tutorialDoneLocation2;
    }

    public void setTutorialDoneLocation2(Integer tutorialDoneLocation2) {this.tutorialDoneLocation2 = tutorialDoneLocation2;}

    public Integer getTutorialDoneLocation3() {return tutorialDoneLocation3;}

    public void setTutorialDoneLocation3(Integer tutorialDoneLocation3) {
        this.tutorialDoneLocation3 = tutorialDoneLocation3;
    }

    public Integer getTutorialDoneLocation4() {return tutorialDoneLocation4;}

    public void setTutorialDoneLocation4(Integer tutorialDoneLocation4) {
        this.tutorialDoneLocation4 = tutorialDoneLocation4;
    }

    public Integer getIsLocationCompletedHouse() {
        return isLocationCompletedHouse;
    }

    public void setIsLocationCompletedHouse(Integer isLocationCompletedHouse) {
        this.isLocationCompletedHouse = isLocationCompletedHouse;
    }

    public Integer getIsLocationCompletedBackyard() {
        return isLocationCompletedBackyard;
    }

    public void setIsLocationCompletedBackyard(Integer isLocationCompletedBackyard) {
        this.isLocationCompletedBackyard = isLocationCompletedBackyard;
    }

    public Integer getIsLocationCompletedForest() {
        return isLocationCompletedForest;
    }

    public void setIsLocationCompletedForest(Integer isLocationCompletedForest) {
        this.isLocationCompletedForest = isLocationCompletedForest;
    }

    public Integer getIsLocationCompletedBeach() {
        return isLocationCompletedBeach;
    }

    public void setIsLocationCompletedBeach(Integer isLocationCompletedBeach) {
        this.isLocationCompletedBeach = isLocationCompletedBeach;
    }

    public Profile(Integer id, String name, Integer avatarId, Integer achievement, Integer tutorialDoneLocation1, Integer tutorialDoneLocation2, Integer tutorialDoneLocation3, Integer tutorialDoneLocation4, Integer isLocationCompletedHouse, Integer isLocationCompletedBackyard, Integer isLocationCompletedForest, Integer isLocationCompletedBeach) {
        this.profileId = id;
        this.name = name;
        this.avatarId = avatarId;
        this.achievement = achievement;
        this.tutorialDoneLocation1 = tutorialDoneLocation1;
        this.tutorialDoneLocation2 = tutorialDoneLocation2;
        this.tutorialDoneLocation3 = tutorialDoneLocation3;
        this.tutorialDoneLocation4 = tutorialDoneLocation4;
        this.isLocationCompletedHouse = isLocationCompletedHouse;
        this.isLocationCompletedBackyard = isLocationCompletedBackyard;
        this.isLocationCompletedForest = isLocationCompletedForest;
        this.isLocationCompletedBeach = isLocationCompletedBeach;
    }

    public Profile(String name, Integer avatarId) {
        this.name = name;
        this.avatarId = avatarId;
    }



    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profileId +
                ", name='" + name + '\'' +
                ", avatar_id=" + avatarId +
                ", achievement=" + achievement +
                ", tutorial_done_location_1=" + tutorialDoneLocation1 +
                ", tutorial_done_location_2=" + tutorialDoneLocation2 +
                ", tutorial_done_location_3=" + tutorialDoneLocation3 +
                ", tutorial_done_location_4=" + tutorialDoneLocation4 +
                ", isLocationCompletedHouse=" + isLocationCompletedHouse +
                ", isLocationCompletedBackyard=" + isLocationCompletedBackyard +
                ", isLocationCompletedForest=" + isLocationCompletedForest +
                ", isLocationCompletedBeach=" + isLocationCompletedBeach +
                '}';
    }


}
