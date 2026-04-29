package com.userService.user.service.service.impl;

import com.userService.user.service.dto.PagedResponse;
import com.userService.user.service.dto.UserProfileCreateRequest;
import com.userService.user.service.dto.UserProfileResponse;
import com.userService.user.service.dto.UserProfileUpdateRequest;
import com.userService.user.service.dto.VaccinationIdsResponse;
import com.userService.user.service.entity.UserProfile;
import com.userService.user.service.entity.UserVaccinationMapping;
import com.userService.user.service.exception.ConflictException;
import com.userService.user.service.exception.ResourceNotFoundException;
import com.userService.user.service.repository.UserProfileRepository;
import com.userService.user.service.repository.UserVaccinationMappingRepository;
import com.userService.user.service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserVaccinationMappingRepository userVaccinationMappingRepository;

    @Override
    @Transactional
    public UserProfileResponse createProfile(UUID userId, UserProfileCreateRequest request) {
        if (userProfileRepository.existsById(userId)) {
            throw new ConflictException("Profile already exists for userId: " + userId);
        }
        userProfileRepository.findByMobileNumber(request.getMobileNumber())
                .ifPresent(profile -> { throw new ConflictException("Mobile number is already mapped to another user"); });

        UserProfile saved = userProfileRepository.save(UserProfile.builder()
                .userId(userId)
                .mobileNumber(request.getMobileNumber())
                .name(request.getName())
                .age(request.getAge())
                .idProofType(request.getIdProofType())
                .idProofNumber(request.getIdProofNumber())
                .build());
        log.info("Created profile for userId={} with mobile={}", userId, request.getMobileNumber());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId) {
        return toResponse(fetchUser(userId));
    }


    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserProfileResponse> getUsers(int page, int size) {
        var userPage = userProfileRepository.findAll(PageRequest.of(page, size)).map(this::toResponse);
        return PagedResponse.<UserProfileResponse>builder()
                .content(userPage.getContent())
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(UUID userId, UserProfileUpdateRequest request) {
        UserProfile user = fetchUser(userId);
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setIdProofType(request.getIdProofType());
        user.setIdProofNumber(request.getIdProofNumber());
        UserProfile updated = userProfileRepository.save(user);
        log.info("Updated profile for userId={}", userId);
        return toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationIdsResponse getVaccinationIds(UUID userId) {
        fetchUser(userId);
        List<String> vaccinationIds = userVaccinationMappingRepository.findByUserId(userId).stream()
                .map(UserVaccinationMapping::getVaccinationId)
                .toList();
        return VaccinationIdsResponse.builder().userId(userId).vaccinationIds(vaccinationIds).build();
    }

    @Override
    @Transactional
    public void addVaccinationIds(UUID userId, List<String> vaccinationIds) {
        fetchUser(userId);
        Set<String> existing = userVaccinationMappingRepository.findByUserId(userId).stream()
                .map(UserVaccinationMapping::getVaccinationId)
                .collect(java.util.stream.Collectors.toSet());

        List<UserVaccinationMapping> newMappings = vaccinationIds.stream()
                .filter(id -> !existing.contains(id))
                .map(id -> UserVaccinationMapping.builder().userId(userId).vaccinationId(id).build())
                .toList();

        if (!newMappings.isEmpty()) {
            userVaccinationMappingRepository.saveAll(newMappings);
            log.info("Added {} vaccination mappings for userId={}", newMappings.size(), userId);
        }
    }

    private UserProfile fetchUser(UUID userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for userId: " + userId));
    }

    private UserProfileResponse toResponse(UserProfile user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .mobileNumber(user.getMobileNumber())
                .name(user.getName())
                .age(user.getAge())
                .idProofType(user.getIdProofType())
                .idProofNumber(user.getIdProofNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
