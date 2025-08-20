package com.prototype.dev_exchange.service;

import com.prototype.dev_exchange.dto.ProfileDTO;
import com.prototype.dev_exchange.entity.Profile;
import com.prototype.dev_exchange.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<ProfileDTO> findMatches(Long userId) {
        try {
            Profile userProfile = profileRepository.findByUser_Id(userId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            String techToLearn = userProfile.getTechToLearn();
            String techToShare = userProfile.getTechToShare();

            System.out.println("Current user wants to learn: " + techToLearn);
            System.out.println("Current user can share: " + techToShare);

            List<ProfileDTO> results = profileRepository.findAll().stream()
                    .filter(profile -> !profile.getUser().getId().equals(userId))
                    .filter(profile ->
                            (profile.getTechToShare() != null && techToLearn != null && profile.getTechToShare().equalsIgnoreCase(techToLearn)) ||
                                    (profile.getTechToLearn() != null && techToShare != null && profile.getTechToLearn().equalsIgnoreCase(techToShare))
                    )
                    .map(profile -> {
                        ProfileDTO dto = new ProfileDTO();
                        dto.setId(profile.getId());
                        dto.setBio(profile.getBio());
                        dto.setUsername(profile.getUser().getUsername());
                        dto.setTechToLearn(profile.getTechToLearn());
                        dto.setTechToShare(profile.getTechToShare());
                        dto.setUserId(profile.getUser().getId());
                        return dto;
                    })
                    .collect(Collectors.toList());

            System.out.println("Found " + results.size() + " matches.");
            return results;

        } catch (Exception e) {
            System.err.println("Error during match finding: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}