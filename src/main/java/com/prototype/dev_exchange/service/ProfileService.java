package com.prototype.dev_exchange.service;

import com.prototype.dev_exchange.entity.Profile;
import com.prototype.dev_exchange.entity.User;
import com.prototype.dev_exchange.repository.ProfileRepository;
import com.prototype.dev_exchange.dto.ProfileDTO;
import com.prototype.dev_exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;



    private ProfileDTO convertProfileToDTO(Profile profile) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setUsername(profile.getUser().getUsername());
        dto.setTechToLearn(profile.getTechToLearn());
        dto.setTechToShare(profile.getTechToShare());
        dto.setBio(profile.getBio());
        return dto;
    }

    //Get All Profiles
    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream().map(this::convertProfileToDTO).collect(Collectors.toList());
    }
    // Converts DTO to Entity for saving
    private Profile mapToEntity(ProfileDTO profileDTO, User user) {
        Profile profile = new Profile();
        profile.setBio(profileDTO.getBio());
        profile.setTechToLearn(profileDTO.getTechToLearn());
        profile.setTechToShare(profileDTO.getTechToShare());
        profile.setUser(user);
        return profile;
    }

    // Converts Entity to DTO for returning to client
    private ProfileDTO mapToDTO(Profile profile) {
        return new ProfileDTO(
                profile.getId(),
                profile.getUser().getId(),
                profile.getTechToLearn(),
                profile.getTechToShare(),
                profile.getBio()
        );
    }

    //Create New Profile
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        Long userId = profileDTO.getUserId();

        //Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Check if user already has a profile
        Optional<Profile> existingProfile = profileRepository.findByUser_Id(userId);
        if (existingProfile.isPresent()) {
            throw new RuntimeException("This user already has a profile.");
        }

        // Create and save the new profile
        Profile profile = new Profile();
        profile.setBio(profileDTO.getBio());
        profile.setTechToLearn(profileDTO.getTechToLearn());
        profile.setTechToShare(profileDTO.getTechToShare());
        profile.setUser(user);

        Profile savedProfile = profileRepository.save(profile);

        return mapToDTO(savedProfile);
    }

    //Update Existing Profile
    public ProfileDTO updateProfile(Long profileId, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found for profile ID: " + profileId));

        existingProfile.setBio(profileDTO.getBio());
        existingProfile.setTechToLearn(profileDTO.getTechToLearn());
        existingProfile.setTechToShare(profileDTO.getTechToShare());

        profileRepository.save(existingProfile);

        return mapToDTO(existingProfile);
    }

    //Get Profile by ID
    public ProfileDTO getProfileById(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return convertProfileToDTO(profile);
    }

    //Delete User
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found with ID: " + id);
        }
        profileRepository.deleteById(id);
    }


    public void sendConnectionRequest(Long senderUserId, Long receiverProfileId) {
        Profile sender = profileRepository.findByUser_Id(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender profile not found"));
        Profile receiver = profileRepository.findById(receiverProfileId)
                .orElseThrow(() -> new RuntimeException("Receiver profile not found"));

        if (sender.getPendingRequestsSent().contains(receiver.getId())) {
            throw new RuntimeException("Request already sent");
        }

        sender.getPendingRequestsSent().add(receiver.getId());
        receiver.getPendingRequestsReceived().add(sender.getId());

        profileRepository.save(sender);
        profileRepository.save(receiver);
    }


    public void acceptConnectionRequest(Long receiverUserId, Long senderProfileId) {
        Profile receiver = profileRepository.findByUser_Id(receiverUserId)
                .orElseThrow(() -> new RuntimeException("Receiver profile not found"));
        Profile sender = profileRepository.findById(senderProfileId)
                .orElseThrow(() -> new RuntimeException("Sender profile not found"));

        if (!receiver.getPendingRequestsReceived().contains(sender.getId())) {
            throw new RuntimeException("No such request");
        }

        receiver.getPendingRequestsReceived().remove(sender.getId());
        sender.getPendingRequestsSent().remove(receiver.getId());

        receiver.getConnections().add(sender.getId());
        sender.getConnections().add(receiver.getId());

        profileRepository.save(receiver);
        profileRepository.save(sender);
    }

    public void declineConnectionRequest(Long receiverUserId, Long senderProfileId) {
        Profile receiver = profileRepository.findByUser_Id(receiverUserId)
                .orElseThrow(() -> new RuntimeException("Receiver profile not found"));
        Profile sender = profileRepository.findById(senderProfileId)
                .orElseThrow(() -> new RuntimeException("Sender profile not found"));

        // If the request exists, remove it
        receiver.getPendingRequestsReceived().remove(sender.getId());
        sender.getPendingRequestsSent().remove(receiver.getId());

        profileRepository.save(receiver);
        profileRepository.save(sender);
    }

    public List<ProfileDTO> getPendingRequests(Long receiverUserId) {
        Profile receiver = profileRepository.findByUser_Id(receiverUserId)
                .orElseThrow(() -> new RuntimeException("Receiver profile not found"));

        List<Profile> senders = receiver.getPendingRequestsReceived().stream()
                .map(senderId -> profileRepository.findById(senderId)
                        .orElseThrow(() -> new RuntimeException("Sender not found: " + senderId)))
                .collect(Collectors.toList());

        return senders.stream().map(this::convertProfileToDTO).collect(Collectors.toList());
    }

    public List<ProfileDTO> getConnections(Long userId) {
        Profile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<Profile> connectionProfiles = profile.getConnections().stream()
                .map(connId -> profileRepository.findById(connId)
                        .orElseThrow(() -> new RuntimeException("Connected profile not found: " + connId)))
                .collect(Collectors.toList());

        return connectionProfiles.stream().map(this::convertProfileToDTO).collect(Collectors.toList());
    }









}
