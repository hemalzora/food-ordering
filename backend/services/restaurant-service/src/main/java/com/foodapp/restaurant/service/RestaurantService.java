package com.foodapp.restaurant.service;

import com.foodapp.restaurant.dto.CreateRestaurantRequest;
import com.foodapp.restaurant.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> listRestaurants();

    RestaurantDto getRestaurant(Long restaurantId);

    RestaurantDto createRestaurant(CreateRestaurantRequest createRestaurantRequest);

    RestaurantDto updateRestaurant(Long restaurantId, CreateRestaurantRequest createRestaurantRequest);

    void deactivateRestaurant(Long restaurantId);
}
