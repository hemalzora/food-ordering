package com.foodapp.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "menu_item_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "menu_item_id", nullable = false)
    private Long menuItemId;

    @Setter
    @Column(nullable = false)
    private int quantity;

    public CartItem(Long userId, Long menuItemId, int quantity) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }
}
