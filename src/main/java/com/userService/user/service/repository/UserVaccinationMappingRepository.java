package com.userService.user.service.repository;

import com.userService.user.service.entity.UserVaccinationMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserVaccinationMappingRepository extends JpaRepository<UserVaccinationMapping, Long> {
    List<UserVaccinationMapping> findByUserId(UUID userId);
}
