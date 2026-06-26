package com.foodapp.order.repository;

import com.foodapp.order.entity.MenuItemRef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRefRepository extends JpaRepository<MenuItemRef, Long> {
}
