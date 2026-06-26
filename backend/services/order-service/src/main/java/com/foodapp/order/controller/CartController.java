package com.foodapp.order.controller;

import com.foodapp.order.dto.AddCartItemRequest;
import com.foodapp.order.dto.CartView;
import com.foodapp.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Manage a user's shopping cart")
@RestController
@RequestMapping("/api/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get cart", description = "Returns the current cart for a user.")
    @ApiResponse(responseCode = "200", description = "Cart returned")
    @GetMapping
    public CartView getCart(@Parameter(description = "User id") @PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @Operation(summary = "Add item to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to cart"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartView addToCart(@Parameter(description = "User id") @PathVariable Long userId, @Valid @RequestBody AddCartItemRequest addCartItemRequest) {
        return cartService.addToCart(userId, addCartItemRequest);
    }

    @Operation(summary = "Remove item from cart", description = "Idempotent: succeeds whether or not the item was in the cart.")
    @ApiResponse(responseCode = "204", description = "Item removed")
    @DeleteMapping("/items/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromCart(@Parameter(description = "User id") @PathVariable Long userId, @Parameter(description = "Menu item id") @PathVariable Long menuItemId) {
        cartService.removeFromCart(userId, menuItemId);
    }

    @Operation(summary = "Clear cart", description = "Removes all items from the user's cart.")
    @ApiResponse(responseCode = "204", description = "Cart cleared")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@Parameter(description = "User id") @PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
