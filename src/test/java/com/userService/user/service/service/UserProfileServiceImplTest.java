package com.userService.user.service.service;

import com.userService.user.service.dto.UserProfileCreateRequest;
import com.userService.user.service.entity.IdProofType;
import com.userService.user.service.entity.UserProfile;
import com.userService.user.service.repository.UserProfileRepository;
import com.userService.user.service.repository.UserVaccinationMappingRepository;
import com.userService.user.service.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserVaccinationMappingRepository userVaccinationMappingRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void createProfile_shouldCreateSuccessfully() {
        UUID userId = UUID.randomUUID();
        UserProfileCreateRequest request = new UserProfileCreateRequest();
        request.setMobileNumber("9876543210");
        request.setName("Test User");
        request.setAge(25);
        request.setIdProofType(IdProofType.AADHAR);
        request.setIdProofNumber("111122223333");

        when(userProfileRepository.existsById(userId)).thenReturn(false);
        when(userProfileRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = userProfileService.createProfile(userId, request);

        assertEquals(userId, response.getUserId());
        assertEquals("Test User", response.getName());
    }
}
