package com.foodapp.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item_ref")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItemRef {

    @Id
    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 9, scale = 2)
    private BigDecimal price;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private boolean active = true;
}
