package com.foodapp.restaurant.dto;

import com.foodapp.restaurant.entity.Restaurant;

public record RestaurantDto(
        Long restaurantId,
        String name,
        String email,
        String phone,
        String address,
        boolean active) {

    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getRestaurantId(),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getPhone(),
                restaurant.getAddress(),
                restaurant.isActive());
    }
}
