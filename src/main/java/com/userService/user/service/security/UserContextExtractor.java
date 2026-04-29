package com.userService.user.service.security;

import com.userService.user.service.exception.ConflictException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserContextExtractor {

    private static final String USER_ID_HEADER = "X-User-Id";

    public UUID extractUserId(HttpServletRequest request) {
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null || userId.isBlank()) {
            throw new ConflictException("Missing X-User-Id header from gateway");
        }
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            throw new ConflictException("Invalid X-User-Id header format");
        }
    }
}
