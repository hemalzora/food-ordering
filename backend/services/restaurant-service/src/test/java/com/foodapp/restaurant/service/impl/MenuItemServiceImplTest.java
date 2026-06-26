package com.foodapp.restaurant.service.impl;

import com.foodapp.restaurant.dto.CreateMenuItemRequest;
import com.foodapp.restaurant.dto.MenuItemDto;
import com.foodapp.restaurant.entity.MenuItem;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.exception.ResourceNotFoundException;
import com.foodapp.restaurant.repository.MenuItemRepository;
import com.foodapp.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    private static final Long RESTAURANT_ID = 5L;
    private static final Long MENU_ITEM_ID = 9L;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    @Test
    void listMenu_returnsMappedItems_whenRestaurantExists() {
        Restaurant restaurant = restaurant();
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurant_RestaurantId(RESTAURANT_ID)).thenReturn(List.of(
                new MenuItem(restaurant, "Samosa", "VEG", new BigDecimal("10.00"), "Crispy")));

        List<MenuItemDto> result = menuItemService.listMenu(RESTAURANT_ID);

        assertEquals(1, result.size());
        assertEquals("Samosa", result.get(0).name());
        assertEquals(RESTAURANT_ID, result.get(0).restaurantId());
    }

    @Test
    void listMenu_throwsNotFound_whenRestaurantMissing() {
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.listMenu(RESTAURANT_ID));
        verify(menuItemRepository, never()).findByRestaurant_RestaurantId(any());
    }

    @Test
    void addMenuItem_savesUnderRestaurantAndReturnsDto() {
        Restaurant restaurant = restaurant();
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));
        CreateMenuItemRequest request =
                new CreateMenuItemRequest("Pasta", "VEG", new BigDecimal("110.00"), "Creamy");

        MenuItemDto dto = menuItemService.addMenuItem(RESTAURANT_ID, request);

        assertEquals("Pasta", dto.name());
        assertEquals(new BigDecimal("110.00"), dto.price());
        assertEquals(RESTAURANT_ID, dto.restaurantId());
        assertTrue(dto.active());
    }

    @Test
    void addMenuItem_throwsNotFound_whenRestaurantMissing() {
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.addMenuItem(RESTAURANT_ID,
                new CreateMenuItemRequest("Pasta", "VEG", new BigDecimal("110.00"), "Creamy")));
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void updateMenuItem_appliesChangesViaDirtyChecking() {
        MenuItem existing = new MenuItem(restaurant(), "Old", "VEG", new BigDecimal("10.00"), "old");
        when(menuItemRepository.findById(MENU_ITEM_ID)).thenReturn(Optional.of(existing));
        CreateMenuItemRequest request =
                new CreateMenuItemRequest("New", "NON_VEG", new BigDecimal("60.00"), "new");

        MenuItemDto dto = menuItemService.updateMenuItem(MENU_ITEM_ID, request);

        assertEquals("New", dto.name());
        assertEquals("NON_VEG", dto.type());
        assertEquals(new BigDecimal("60.00"), dto.price());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void updateMenuItem_throwsNotFound_whenMissing() {
        when(menuItemRepository.findById(MENU_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.updateMenuItem(MENU_ITEM_ID,
                new CreateMenuItemRequest("New", "VEG", new BigDecimal("60.00"), null)));
    }

    @Test
    void deactivateMenuItem_setsActiveFalse() {
        MenuItem existing = new MenuItem(restaurant(), "Samosa", "VEG", new BigDecimal("10.00"), "Crispy");
        when(menuItemRepository.findById(MENU_ITEM_ID)).thenReturn(Optional.of(existing));

        menuItemService.deactivateMenuItem(MENU_ITEM_ID);

        assertFalse(existing.isActive());
    }

    @Test
    void deactivateMenuItem_throwsNotFound_whenMissing() {
        when(menuItemRepository.findById(MENU_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deactivateMenuItem(MENU_ITEM_ID));
    }

    private Restaurant restaurant() {
        Restaurant restaurant = mock(Restaurant.class);
        lenient().when(restaurant.getRestaurantId()).thenReturn(RESTAURANT_ID);
        return restaurant;
    }
}
