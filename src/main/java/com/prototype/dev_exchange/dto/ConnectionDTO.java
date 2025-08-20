package com.prototype.dev_exchange.dto;

import com.prototype.dev_exchange.status.ConnectionStatus;

import java.time.LocalDateTime;

public class ConnectionDTO {
    private Long id;
    private Long requesterId;
    private String requesterUsername;
    private Long receiverId;
    private String receiverUsername;
    private ConnectionStatus status;
    private String googleMeetLink;
    private LocalDateTime createdAt;


    //Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getRequesterId() {
        return requesterId;
    }
    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }
    public String getRequesterUsername() {
        return requesterUsername;
    }
    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }
    public Long getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
    public String getReceiverUsername() {
        return receiverUsername;
    }
    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
    public ConnectionStatus getStatus() {
        return status;
    }
    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }
    public String getGoogleMeetLink() {
        return googleMeetLink;
    }
    public void setGoogleMeetLink(String googleMeetLink) {
        this.googleMeetLink = googleMeetLink;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

