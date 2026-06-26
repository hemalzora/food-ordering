package com.foodapp.restaurant.controller;

import com.foodapp.restaurant.dto.CreateMenuItemRequest;
import com.foodapp.restaurant.dto.MenuItemDto;
import com.foodapp.restaurant.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Menu items", description = "Manage menu items belonging to restaurants")
@RestController
@RequestMapping("/api")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Add a menu item", description = "Adds a menu item to a restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Menu item created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping("/restaurants/{restaurantId}/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemDto addMenuItem(@Parameter(description = "Restaurant id") @PathVariable Long restaurantId, @Valid @RequestBody CreateMenuItemRequest createMenuItemRequest) {
        return menuItemService.addMenuItem(restaurantId, createMenuItemRequest);
    }

    @Operation(summary = "Update a menu item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu item updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @PutMapping("/menu-items/{menuItemId}")
    public MenuItemDto updateMenuItem(@Parameter(description = "Menu item id") @PathVariable Long menuItemId, @Valid @RequestBody CreateMenuItemRequest createMenuItemRequest) {
        return menuItemService.updateMenuItem(menuItemId, createMenuItemRequest);
    }

    @Operation(summary = "Deactivate a menu item", description = "Soft-deletes a menu item by marking it inactive.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Menu item deactivated"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @DeleteMapping("/menu-items/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMenuItem(@Parameter(description = "Menu item id") @PathVariable Long menuItemId) {
        menuItemService.deactivateMenuItem(menuItemId);
    }
}
