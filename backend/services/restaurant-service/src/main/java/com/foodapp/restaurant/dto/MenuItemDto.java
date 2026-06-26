package com.foodapp.restaurant.dto;

import com.foodapp.restaurant.entity.MenuItem;

import java.math.BigDecimal;

public record MenuItemDto(
        Long menuItemId,
        Long restaurantId,
        String name,
        String type,
        BigDecimal price,
        String description,
        boolean active) {

    public static MenuItemDto from(MenuItem menuItem) {
        return new MenuItemDto(
                menuItem.getMenuItemId(),
                menuItem.getRestaurant().getRestaurantId(),
                menuItem.getName(),
                menuItem.getType(),
                menuItem.getPrice(),
                menuItem.getDescription(),
                menuItem.isActive());
    }
}
