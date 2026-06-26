package com.foodapp.order.service.impl;

import com.foodapp.order.dto.PricedCart;
import com.foodapp.order.entity.CartItem;
import com.foodapp.order.entity.MenuItemRef;
import com.foodapp.order.exception.InvalidOrderException;
import com.foodapp.order.repository.CartItemRepository;
import com.foodapp.order.repository.MenuItemRefRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    private static final Long USER_ID = 1L;

    @Mock
    private MenuItemRefRepository menuItemRefRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void priceCart_computesLineTotalsAndOrderTotal() {
        when(cartItemRepository.findByUserId(USER_ID)).thenReturn(List.of(
                new CartItem(USER_ID, 5L, 2),
                new CartItem(USER_ID, 6L, 1)));
        MenuItemRef vegBurger = item(5L, "Veg Burger", "40.00", 5L, true);
        MenuItemRef chickenBurger = item(6L, "Chicken Burger", "60.00", 5L, true);
        when(menuItemRefRepository.findAllById(any())).thenReturn(List.of(vegBurger, chickenBurger));

        PricedCart pricedCart = cartService.priceCart(USER_ID);

        assertEquals(5L, pricedCart.restaurantId());
        assertEquals(2, pricedCart.lines().size());
        assertEquals(new BigDecimal("80.00"), pricedCart.lines().get(0).lineTotal());
        assertEquals(new BigDecimal("60.00"), pricedCart.lines().get(1).lineTotal());
        assertEquals(new BigDecimal("140.00"), pricedCart.total());
    }

    @Test
    void priceCart_emptyCart_returnsZeroTotalAndNoRestaurant() {
        when(cartItemRepository.findByUserId(USER_ID)).thenReturn(List.of());

        PricedCart pricedCart = cartService.priceCart(USER_ID);

        assertTrue(pricedCart.lines().isEmpty());
        assertEquals(null, pricedCart.restaurantId());
        assertEquals(new BigDecimal("0.00"), pricedCart.total());
    }

    @Test
    void priceCart_rejectsItemsFromDifferentRestaurants() {
        when(cartItemRepository.findByUserId(USER_ID)).thenReturn(List.of(
                new CartItem(USER_ID, 5L, 1),
                new CartItem(USER_ID, 13L, 1)));
        MenuItemRef vegBurger = item(5L, "Veg Burger", "40.00", 5L, true);
        MenuItemRef pizza = item(13L, "Onion & Capsicum Pizza", "60.00", 6L, true);
        when(menuItemRefRepository.findAllById(any())).thenReturn(List.of(vegBurger, pizza));

        InvalidOrderException ex = assertThrows(InvalidOrderException.class, () -> cartService.priceCart(USER_ID));
        assertTrue(ex.getMessage().contains("same restaurant"));
    }

    @Test
    void priceCart_rejectsInactiveItem() {
        when(cartItemRepository.findByUserId(USER_ID)).thenReturn(List.of(new CartItem(USER_ID, 9L, 1)));
        MenuItemRef samosa = item(9L, "Samosa", "10.00", 5L, false);
        when(menuItemRefRepository.findAllById(any())).thenReturn(List.of(samosa));

        InvalidOrderException ex = assertThrows(InvalidOrderException.class, () -> cartService.priceCart(USER_ID));
        assertTrue(ex.getMessage().contains("not available"));
    }

    private MenuItemRef item(Long menuItemId, String name, String price, Long restaurantId, boolean active) {
        MenuItemRef item = org.mockito.Mockito.mock(MenuItemRef.class);
        lenient().when(item.getMenuItemId()).thenReturn(menuItemId);
        lenient().when(item.getName()).thenReturn(name);
        lenient().when(item.getPrice()).thenReturn(new BigDecimal(price));
        lenient().when(item.getRestaurantId()).thenReturn(restaurantId);
        lenient().when(item.isActive()).thenReturn(active);
        return item;
    }
}
