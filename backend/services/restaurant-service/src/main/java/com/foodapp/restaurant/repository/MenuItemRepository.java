package com.foodapp.restaurant.repository;

import com.foodapp.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_RestaurantId(Long restaurantId);
}
