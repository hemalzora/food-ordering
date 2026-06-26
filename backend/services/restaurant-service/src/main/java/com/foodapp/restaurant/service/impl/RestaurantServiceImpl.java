package com.foodapp.restaurant.service.impl;

import com.foodapp.restaurant.dto.CreateRestaurantRequest;
import com.foodapp.restaurant.dto.RestaurantDto;
import com.foodapp.restaurant.entity.Restaurant;
import com.foodapp.restaurant.exception.ResourceNotFoundException;
import com.foodapp.restaurant.repository.RestaurantRepository;
import com.foodapp.restaurant.service.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantDto> listRestaurants() {
        return restaurantRepository.findAll().stream().map(RestaurantDto::from).toList();
    }

    @Override
    public RestaurantDto getRestaurant(Long restaurantId) {
        return RestaurantDto.from(requireRestaurant(restaurantId));
    }

    @Override
    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        return RestaurantDto.from(restaurantRepository.save(new Restaurant(createRestaurantRequest.name(), createRestaurantRequest.email(), createRestaurantRequest.phone(), createRestaurantRequest.address())));
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(Long restaurantId, CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = requireRestaurant(restaurantId);
        // Managed entity: JPA dirty checking flushes these changes at commit - no save() needed.
        restaurant.setName(createRestaurantRequest.name());
        restaurant.setEmail(createRestaurantRequest.email());
        restaurant.setPhone(createRestaurantRequest.phone());
        restaurant.setAddress(createRestaurantRequest.address());
        return RestaurantDto.from(restaurant);
    }

    @Override
    @Transactional
    public void deactivateRestaurant(Long restaurantId) {
        requireRestaurant(restaurantId).setActive(false);
    }

    private Restaurant requireRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant " + restaurantId + " not found"));
    }
}
