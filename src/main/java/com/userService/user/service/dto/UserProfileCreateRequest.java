package com.userService.user.service.dto;

import com.userService.user.service.entity.IdProofType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserProfileCreateRequest {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than 0")
    private Integer age;

    @NotNull(message = "ID proof type is required")
    private IdProofType idProofType;

    @NotBlank(message = "ID proof number is required")
    private String idProofNumber;
}
