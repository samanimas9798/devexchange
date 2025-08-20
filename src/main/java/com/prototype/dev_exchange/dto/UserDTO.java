package com.prototype.dev_exchange.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @NotBlank (message = "Username is required")
    private String username;

    @NotBlank (message = "Email is required")
    @Email (message = "Please enter a valid email address")
    private String email;

    @NotBlank (message = "Password is required")
    @Size (min = 8, max = 15, message = "Password must between 8 and 15 characters long")
    @Pattern(
            regexp = "^[a-zA-Z0-9@#$%^&+=]{8,}$",
            message = "Password must be at least 8 characters, with no spaces"
    )

    private String password;
    private String techToShare;
    private String techToLearn;

    public UserDTO() {
    }

    public UserDTO(long id, String username, String email, String password, String techToLearn, String techToShare) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.techToLearn = techToLearn;
        this.techToShare = techToShare;
    }

    //Getters and Setters

    public long getId() {
        return id;
    }
    public void setId(long id) {
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
}
