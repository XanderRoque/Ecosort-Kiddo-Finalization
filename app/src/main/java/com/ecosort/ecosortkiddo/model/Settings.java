package com.ecosort.ecosortkiddo.model;

public class Settings {
    private int music;
    private int sound;
    private String language;
    private int profileId;

    public Settings(int music, int sound, String language, int profileId) {
        this.music = music;
        this.sound = sound;
        this.language = language;
        this.profileId = profileId;
    }

    public static String LANGUAGE_ENGLISH = "en";
    public static String LANGUAGE_FILIPINO = "tl";

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profile_id) {
        this.profileId = profile_id;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "music=" + music +
                ", sound=" + sound +  // sound should not be enclosed in quotes
                ", language='" + language + '\'' +  // Single quote after language value
                ", profile_id=" + profileId +
                '}';
    }
}