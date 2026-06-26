package com.foodapp.restaurant.controller;

import com.foodapp.restaurant.dto.CreateRestaurantRequest;
import com.foodapp.restaurant.dto.MenuItemDto;
import com.foodapp.restaurant.dto.RestaurantDto;
import com.foodapp.restaurant.service.MenuItemService;
import com.foodapp.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurants", description = "Manage restaurants and browse their menus")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public RestaurantController(RestaurantService restaurantService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "List restaurants", description = "Returns all restaurants.")
    @ApiResponse(responseCode = "200", description = "Restaurants returned")
    @GetMapping
    public List<RestaurantDto> listRestaurants() {
        return restaurantService.listRestaurants();
    }

    @Operation(summary = "Get a restaurant", description = "Returns a single restaurant by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{restaurantId}")
    public RestaurantDto getRestaurant(@Parameter(description = "Restaurant id") @PathVariable Long restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @Operation(summary = "Create a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto createRestaurant(@Valid @RequestBody CreateRestaurantRequest createRestaurantRequest) {
        return restaurantService.createRestaurant(createRestaurantRequest);
    }

    @Operation(summary = "Update a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PutMapping("/{restaurantId}")
    public RestaurantDto updateRestaurant(@Parameter(description = "Restaurant id") @PathVariable Long restaurantId, @Valid @RequestBody CreateRestaurantRequest createRestaurantRequest) {
        return restaurantService.updateRestaurant(restaurantId, createRestaurantRequest);
    }

    @Operation(summary = "Deactivate a restaurant", description = "Soft-deletes a restaurant by marking it inactive.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deactivated"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateRestaurant(@Parameter(description = "Restaurant id") @PathVariable Long restaurantId) {
        restaurantService.deactivateRestaurant(restaurantId);
    }

    @Operation(summary = "List a restaurant's menu", description = "Returns the menu items for a restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu returned"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{restaurantId}/menu")
    public List<MenuItemDto> listMenu(@Parameter(description = "Restaurant id") @PathVariable Long restaurantId) {
        return menuItemService.listMenu(restaurantId);
    }
}
