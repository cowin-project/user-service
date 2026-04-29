package com.userService.user.service.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class VaccinationIdsResponse {
    UUID userId;
    List<String> vaccinationIds;
}
