package com.foodapp.restaurant.service.impl;

import com.foodapp.restaurant.dto.CreateRestaurantRequest;
import com.foodapp.restaurant.dto.RestaurantDto;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.exception.ResourceNotFoundException;
import com.foodapp.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    private static final Long RESTAURANT_ID = 1L;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void listRestaurants_mapsEveryRowToDto() {
        when(restaurantRepository.findAll()).thenReturn(List.of(
                new Restaurant("Pizza Place", "pizza@example.com", "111", "Patiala"),
                new Restaurant("Burger Barn", "burger@example.com", "222", "Mohali")));

        List<RestaurantDto> result = restaurantService.listRestaurants();

        assertEquals(2, result.size());
        assertEquals("Pizza Place", result.get(0).name());
        assertEquals("Burger Barn", result.get(1).name());
    }

    @Test
    void getRestaurant_returnsDto_whenFound() {
        when(restaurantRepository.findById(RESTAURANT_ID))
                .thenReturn(Optional.of(new Restaurant("Pizza Place", "pizza@example.com", "111", "Patiala")));

        RestaurantDto dto = restaurantService.getRestaurant(RESTAURANT_ID);

        assertEquals("Pizza Place", dto.name());
        assertEquals("pizza@example.com", dto.email());
        assertTrue(dto.active());
    }

    @Test
    void getRestaurant_throwsNotFound_whenMissing() {
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> restaurantService.getRestaurant(RESTAURANT_ID));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void createRestaurant_persistsRequestAndReturnsDto() {
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));
        CreateRestaurantRequest request =
                new CreateRestaurantRequest("New Spot", "new@example.com", "999", "Zirakpur");

        RestaurantDto dto = restaurantService.createRestaurant(request);

        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        assertEquals("New Spot", captor.getValue().getName());
        assertTrue(captor.getValue().isActive());
        assertEquals("new@example.com", dto.email());
    }

    @Test
    void updateRestaurant_appliesChangesViaDirtyChecking() {
        Restaurant existing = new Restaurant("Old Name", "old@example.com", "111", "Old Addr");
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(existing));
        CreateRestaurantRequest request =
                new CreateRestaurantRequest("New Name", "new@example.com", "222", "New Addr");

        RestaurantDto dto = restaurantService.updateRestaurant(RESTAURANT_ID, request);

        assertEquals("New Name", dto.name());
        assertEquals("new@example.com", dto.email());
        assertEquals("New Addr", dto.address());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void updateRestaurant_throwsNotFound_whenMissing() {
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> restaurantService.updateRestaurant(RESTAURANT_ID,
                        new CreateRestaurantRequest("X", "x@example.com", null, null)));
    }

    @Test
    void deactivateRestaurant_setsActiveFalse() {
        Restaurant existing = new Restaurant("Pizza Place", "pizza@example.com", "111", "Patiala");
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.of(existing));

        restaurantService.deactivateRestaurant(RESTAURANT_ID);

        assertFalse(existing.isActive());
    }

    @Test
    void deactivateRestaurant_throwsNotFound_whenMissing() {
        when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> restaurantService.deactivateRestaurant(RESTAURANT_ID));
    }
}
