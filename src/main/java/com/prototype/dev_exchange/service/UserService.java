package com.prototype.dev_exchange.service;

import com.prototype.dev_exchange.entity.Profile;
import com.prototype.dev_exchange.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.prototype.dev_exchange.dto.UserDTO;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;


    //Convert Entity to DTO
    private UserDTO convertUserToDTO(User user) {
        Profile profile = profileRepository.findByUser_Id(user.getId()).orElse(null);

        String techToLearn = profile != null ? profile.getTechToLearn() : "";
        String techToShare = profile != null ? profile.getTechToShare() : "";

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                techToLearn,
                techToShare
        );
    }

    //Get User by ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return convertUserToDTO(user);
    }

    //Get ALL Users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertUserToDTO).collect(Collectors.toList());
    }

    //Create New User
    public UserDTO createUser(UserDTO userDTO) {


        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user = userRepository.save(user);


        Profile profile = new Profile();
        profile.setUser(user);
        profile.setTechToLearn(userDTO.getTechToLearn() != null ? userDTO.getTechToLearn() : "");
        profile.setTechToShare(userDTO.getTechToShare() != null ? userDTO.getTechToShare() : "");
        profileRepository.save(profile);


        UserDTO dto = convertUserToDTO(user);
        dto.setTechToShare(profile.getTechToShare());
        dto.setTechToLearn(profile.getTechToLearn());

        return dto;
    }

    //Update Existing User
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(userDTO.getPassword());
        userRepository.save(existingUser);

        // Update the associated profile
        Profile profile = profileRepository.findByUser_Id(id)
                .orElseThrow(() -> new RuntimeException("Profile not found for user"));

        profile.setTechToLearn(userDTO.getTechToLearn());
        profile.setTechToShare(userDTO.getTechToShare());
        profileRepository.save(profile);

        return convertUserToDTO(existingUser);
    }


    //Delete User
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }



}
