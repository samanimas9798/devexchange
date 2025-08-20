package com.prototype.dev_exchange.dto;

import java.time.LocalDateTime;

public class ReplyDTO {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime repliedAt;

    public ReplyDTO(Long id, String content, String username, LocalDateTime repliedAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.repliedAt = repliedAt;
    }


    //Getters and Setters

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }
}
