package com.userService.user.service.service;

import com.userService.user.service.dto.UserProfileCreateRequest;
import com.userService.user.service.dto.UserProfileResponse;
import com.userService.user.service.dto.UserProfileUpdateRequest;
import com.userService.user.service.dto.VaccinationIdsResponse;

import com.userService.user.service.dto.PagedResponse;
import java.util.List;
import java.util.UUID;

public interface UserProfileService {
    UserProfileResponse createProfile(UUID userId, UserProfileCreateRequest request);

    UserProfileResponse getProfile(UUID userId);

    UserProfileResponse updateProfile(UUID userId, UserProfileUpdateRequest request);

    PagedResponse<UserProfileResponse> getUsers(int page, int size);

    VaccinationIdsResponse getVaccinationIds(UUID userId);

    void addVaccinationIds(UUID userId, List<String> vaccinationIds);
}
