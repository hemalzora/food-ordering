package com.foodapp.order.service.impl;

import com.foodapp.order.dto.AddCartItemRequest;
import com.foodapp.order.dto.CartView;
import com.foodapp.order.dto.LineView;
import com.foodapp.order.dto.PricedCart;
import com.foodapp.order.entity.CartItem;
import com.foodapp.order.entity.MenuItemRef;
import com.foodapp.order.exception.InvalidOrderException;
import com.foodapp.order.exception.ResourceNotFoundException;
import com.foodapp.order.repository.CartItemRepository;
import com.foodapp.order.repository.MenuItemRefRepository;
import com.foodapp.order.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final MenuItemRefRepository menuItemRefRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(MenuItemRefRepository menuItemRefRepository, CartItemRepository cartItemRepository) {
        this.menuItemRefRepository = menuItemRefRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartView getCart(Long userId) {
        PricedCart pricedCart = priceCart(userId);
        return new CartView(pricedCart.lines(), pricedCart.total());
    }

    @Override
    @Transactional
    public CartView addToCart(Long userId, AddCartItemRequest addCartItemRequest) {
        MenuItemRef item = menuItemRefRepository.findById(addCartItemRequest.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item " + addCartItemRequest.menuItemId() + " not found"));
        if (!item.isActive()) throw new InvalidOrderException("Item not available: " + item.getName());
        cartItemRepository.findByUserIdAndMenuItemId(userId, addCartItemRequest.menuItemId()).ifPresentOrElse(
                existing -> existing.setQuantity(existing.getQuantity() + addCartItemRequest.quantity()),
                () -> cartItemRepository.save(new CartItem(userId, addCartItemRequest.menuItemId(), addCartItemRequest.quantity())));
        return getCart(userId);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long menuItemId) {
        cartItemRepository.deleteByUserIdAndMenuItemId(userId, menuItemId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    @Override
    public PricedCart priceCart(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        // Batch-load every referenced menu item in a single IN-query and index by id.
        // Avoids issuing one findById per cart item (the N+1 problem); lookups below are O(1).
        Map<Long, MenuItemRef> refsById = menuItemRefRepository
                .findAllById(cartItems.stream().map(CartItem::getMenuItemId).toList())
                .stream().collect(Collectors.toMap(MenuItemRef::getMenuItemId, item -> item));
        List<LineView> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        Long restaurantId = null;
        for (CartItem cartItem : cartItems) {
            MenuItemRef item = refsById.get(cartItem.getMenuItemId());
            if (item == null) throw new ResourceNotFoundException("Menu item " + cartItem.getMenuItemId() + " not found");
            if (!item.isActive()) throw new InvalidOrderException("Item not available: " + item.getName());
            if (restaurantId == null) restaurantId = item.getRestaurantId();
            else if (!restaurantId.equals(item.getRestaurantId()))
                throw new InvalidOrderException("All items in an order must be from the same restaurant");
            BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())).setScale(2, RoundingMode.HALF_UP);
            lines.add(new LineView(item.getMenuItemId(), item.getName(), item.getPrice(), cartItem.getQuantity(), lineTotal));
            total = total.add(lineTotal);
        }
        return new PricedCart(restaurantId, lines, total.setScale(2, RoundingMode.HALF_UP));
    }
}
