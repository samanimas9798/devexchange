package com.prototype.dev_exchange.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "Users")


public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) //Auto-increment ID

    private Long id;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    public User() {}

    public User(Long id, String username, String email, String password, String techToLearn, String techToShare) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }



    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }



}
