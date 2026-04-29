package com.userService.user.service.exception;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class ApiErrorResponse {
    Instant timestamp;
    int status;
    String error;
    String message;
    List<String> details;
}
