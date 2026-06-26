package com.foodapp.restaurant.service.impl;

import com.foodapp.restaurant.dto.CreateMenuItemRequest;
import com.foodapp.restaurant.dto.MenuItemDto;
import com.foodapp.restaurant.entity.MenuItem;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.exception.ResourceNotFoundException;
import com.foodapp.restaurant.repository.MenuItemRepository;
import com.foodapp.restaurant.repository.RestaurantRepository;
import com.foodapp.restaurant.service.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<MenuItemDto> listMenu(Long restaurantId) {
        requireRestaurant(restaurantId);
        return menuItemRepository.findByRestaurant_RestaurantId(restaurantId).stream().map(MenuItemDto::from).toList();
    }

    @Override
    @Transactional
    public MenuItemDto addMenuItem(Long restaurantId, CreateMenuItemRequest createMenuItemRequest) {
        Restaurant restaurant = requireRestaurant(restaurantId);
        return MenuItemDto.from(menuItemRepository.save(new MenuItem(restaurant, createMenuItemRequest.name(), createMenuItemRequest.type(), createMenuItemRequest.price(), createMenuItemRequest.description())));
    }

    @Override
    @Transactional
    public MenuItemDto updateMenuItem(Long menuItemId, CreateMenuItemRequest createMenuItemRequest) {
        MenuItem menuItem = requireMenuItem(menuItemId);
        // Managed entity: JPA dirty checking flushes these changes at commit - no save() needed.
        menuItem.setName(createMenuItemRequest.name());
        menuItem.setType(createMenuItemRequest.type());
        menuItem.setPrice(createMenuItemRequest.price());
        menuItem.setDescription(createMenuItemRequest.description());
        return MenuItemDto.from(menuItem);
    }

    @Override
    @Transactional
    public void deactivateMenuItem(Long menuItemId) {
        requireMenuItem(menuItemId).setActive(false);
    }

    private Restaurant requireRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant " + restaurantId + " not found"));
    }

    private MenuItem requireMenuItem(Long menuItemId) {
        return menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("Menu item " + menuItemId + " not found"));
    }
}
