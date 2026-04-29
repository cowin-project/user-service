package com.userService.user.service.controller;

import com.userService.user.service.dto.PagedResponse;
import com.userService.user.service.dto.UserProfileCreateRequest;
import com.userService.user.service.dto.UserProfileResponse;
import com.userService.user.service.dto.UserProfileUpdateRequest;
import com.userService.user.service.dto.VaccinationIdsResponse;
import com.userService.user.service.security.UserContextExtractor;
import com.userService.user.service.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;
    private final UserContextExtractor userContextExtractor;

    @PostMapping
    public ResponseEntity<UserProfileResponse> create(@Valid @RequestBody UserProfileCreateRequest request,
                                                      HttpServletRequest httpServletRequest) {
        UUID userId = userContextExtractor.extractUserId(httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createProfile(userId, request));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<UserProfileResponse>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userProfileService.getUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userProfileService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(id, request));
    }

    @GetMapping("/{id}/vaccinations")
    public ResponseEntity<VaccinationIdsResponse> getVaccinations(@PathVariable UUID id) {
        return ResponseEntity.ok(userProfileService.getVaccinationIds(id));
    }

    @PostMapping("/{id}/vaccinations")
    public ResponseEntity<Void> addVaccinations(@PathVariable UUID id, @RequestBody List<String> vaccinationIds) {
        userProfileService.addVaccinationIds(id, vaccinationIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
