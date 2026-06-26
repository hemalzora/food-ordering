package com.foodapp.restaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateRestaurantRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        String phone,
        String address) {
}
