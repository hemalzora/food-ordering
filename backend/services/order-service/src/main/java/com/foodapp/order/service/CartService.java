package com.foodapp.order.service;

import com.foodapp.order.dto.AddCartItemRequest;
import com.foodapp.order.dto.CartView;
import com.foodapp.order.dto.PricedCart;

public interface CartService {
    CartView getCart(Long userId);

    CartView addToCart(Long userId, AddCartItemRequest addCartItemRequest);

    void removeFromCart(Long userId, Long menuItemId);

    void clearCart(Long userId);

    PricedCart priceCart(Long userId);
}
