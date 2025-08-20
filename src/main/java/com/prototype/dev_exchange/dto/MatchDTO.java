package com.prototype.dev_exchange.dto;

import com.prototype.dev_exchange.entity.User;

public class MatchDTO {

    private Long id;
    private String username;
    private String canTeach;
    private String wantsToLearn;

    public MatchDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.canTeach = user.getProfile().getTechToShare();
        this.wantsToLearn = user.getProfile().getTechToLearn();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getCanTeach() {
        return canTeach;
    }
    public String getWantsToLearn() {
        return wantsToLearn;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCanTeach(String canTeach) {
        this.canTeach = canTeach;
    }
    public void setWantsToLearn(String wantsToLearn) {
        this.wantsToLearn = wantsToLearn;
    }
}
