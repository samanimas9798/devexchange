package com.prototype.dev_exchange.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime postedAt;
    private String username;
    private List<String> tags;

    public QuestionDTO(Long id, String title, String description, LocalDateTime postedAt, String username, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.username = username;
        this.tags = tags;
    }


    //Getters and Setters


    public Long getId() {

        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {

        return description;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public String getUsername() {
        return username;
    }
    public List<String> getTags() {
        return tags;
    }



}
