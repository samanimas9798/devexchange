package com.prototype.dev_exchange.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table (name = "Profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id") //foreign key to User
    private User user;

    @Column (columnDefinition = "TEXT")
    private String techToLearn;

    @Column (columnDefinition = "TEXT")
    private String techToShare;

    @Column(length = 500)
    private String bio;

    @ElementCollection
    private List<Long> pendingRequestsSent = new ArrayList<>();

    @ElementCollection
    private List<Long> pendingRequestsReceived = new ArrayList<>();

    @ElementCollection
    private List<Long> connections = new ArrayList<>();


    public  Profile() {}

    public Profile(User user, String techToLearn, String techToShare, String bio) {
        this.user = user;
        this.techToLearn = techToLearn;
        this.techToShare = techToShare;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
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

    public List<Long> getPendingRequestsSent() {
        return pendingRequestsSent;
    }

    public void setPendingRequestsSent(List<Long> pendingRequestsSent) {
        this.pendingRequestsSent = pendingRequestsSent;
    }

    public List<Long> getPendingRequestsReceived() {
        return pendingRequestsReceived;
    }

    public void setPendingRequestsReceived(List<Long> pendingRequestsReceived) {
        this.pendingRequestsReceived = pendingRequestsReceived;
    }

    public List<Long> getConnections() {
        return connections;
    }

    public void setConnections(List<Long> connections) {
        this.connections = connections;
    }
}
