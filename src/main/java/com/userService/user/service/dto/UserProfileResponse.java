package com.userService.user.service.dto;

import com.userService.user.service.entity.IdProofType;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class UserProfileResponse {
    UUID userId;
    String mobileNumber;
    String name;
    Integer age;
    IdProofType idProofType;
    String idProofNumber;
    Instant createdAt;
    Instant updatedAt;
}
