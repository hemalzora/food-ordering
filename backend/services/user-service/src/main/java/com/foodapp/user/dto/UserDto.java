package com.foodapp.user.dto;

import com.foodapp.user.entity.User;

public record UserDto(
        Long userId,
        String name,
        String email,
        String role,
        String phone,
        String gender,
        String address,
        boolean active) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhone(),
                user.getGender(),
                user.getAddress(),
                user.isActive());
    }
}
