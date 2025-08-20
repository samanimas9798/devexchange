package com.prototype.dev_exchange.dto;

public class ProfileDTO {

    private Long id;
    private Long userId;
    private String username;
    private String techToLearn;
    private String techToShare;
    private String bio;

    public ProfileDTO() {}

    public ProfileDTO(Long id, Long userId, String techToLearn, String techToShare, String bio) {
        this.id = id;
        this.userId = userId;
        this.techToLearn = techToLearn;
        this.techToShare = techToShare;
        this.bio = bio;
    }


    //Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTechToLearn() {
        return techToLearn;
    }
    public void setTechToLearn(String techToLearn) {
        this.techToLearn = techToLearn;
    }
    public String getTechToShare() {
        return techToShare;
    }
    public void setTechToShare(String techToShare) {
        this.techToShare = techToShare;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
}
