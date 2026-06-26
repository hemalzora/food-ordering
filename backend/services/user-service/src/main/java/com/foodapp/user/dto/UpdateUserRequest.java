package com.foodapp.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String name,
        String phone,
        String gender,
        String address) {
}
