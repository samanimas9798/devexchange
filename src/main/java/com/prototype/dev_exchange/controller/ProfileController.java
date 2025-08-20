package com.prototype.dev_exchange.controller;
import com.prototype.dev_exchange.dto.ProfileDTO;
import com.prototype.dev_exchange.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Get All Profiles
    @GetMapping
    public List<ProfileDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    // Get Profile by ID
    @GetMapping("/{id}")
    public ProfileDTO getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }


    // Create New Profile
    @PostMapping
    public ProfileDTO createProfile(@RequestBody ProfileDTO profileDTO) {
        return profileService.createProfile(profileDTO);
    }

    // Update Profile
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable Long id, @RequestBody ProfileDTO profileDTO) {
        ProfileDTO updatedProfile = profileService.updateProfile(id, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    // Send a connection request
    @PostMapping("/{fromId}/request/{toId}")
    public ResponseEntity<?> sendRequest(@PathVariable Long fromId, @PathVariable Long toId) {
        profileService.sendConnectionRequest(fromId, toId);
        return ResponseEntity.ok().build();
    }

    // Accept a connection request
    @PostMapping("/{receiverId}/accept/{senderId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Long receiverId, @PathVariable Long senderId) {
        profileService.acceptConnectionRequest(receiverId, senderId);
        return ResponseEntity.ok().build();
    }

    // Decline a connection request
    @PostMapping("/{receiverId}/decline/{senderId}")
    public ResponseEntity<?> declineRequest(@PathVariable Long receiverId, @PathVariable Long senderId) {
        profileService.declineConnectionRequest(receiverId, senderId);
        return ResponseEntity.ok().build();
    }

    // Get pending requests for this user
    @GetMapping("/{id}/pending")
    public List<ProfileDTO> getPendingRequests(@PathVariable Long id) {
        return profileService.getPendingRequests(id);
    }

    // Get accepted connections
    @GetMapping("/{id}/connections")
    public List<ProfileDTO> getConnections(@PathVariable Long id) {
        return profileService.getConnections(id);
    }




}