package com.foodapp.restaurant.service;

import com.foodapp.restaurant.dto.CreateMenuItemRequest;
import com.foodapp.restaurant.dto.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    List<MenuItemDto> listMenu(Long restaurantId);

    MenuItemDto addMenuItem(Long restaurantId, CreateMenuItemRequest createMenuItemRequest);

    MenuItemDto updateMenuItem(Long menuItemId, CreateMenuItemRequest createMenuItemRequest);

    void deactivateMenuItem(Long menuItemId);
}
