package com.foodapp.order.controller;

import com.foodapp.order.dto.OrderResponse;
import com.foodapp.order.dto.PlaceOrderRequest;
import com.foodapp.order.entity.OrderStatus;
import com.foodapp.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Place and track orders")
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Place an order", description = "Creates an order from the user's current cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed"),
            @ApiResponse(responseCode = "400", description = "Validation failed or cart is empty")
    })
    @PostMapping("/users/{userId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Parameter(description = "User id") @PathVariable Long userId, @Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        return orderService.placeOrder(userId, placeOrderRequest);
    }

    @Operation(summary = "List a user's orders")
    @ApiResponse(responseCode = "200", description = "Orders returned")
    @GetMapping("/users/{userId}/orders")
    public List<OrderResponse> listOrders(@Parameter(description = "User id") @PathVariable Long userId) {
        return orderService.listOrders(userId);
    }

    @Operation(summary = "Get an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/orders/{orderId}")
    public OrderResponse getOrder(@Parameter(description = "Order id") @PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @Operation(summary = "Update order status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(@Parameter(description = "Order id") @PathVariable Long orderId, @Parameter(description = "New order status") @RequestParam OrderStatus orderStatus) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }
}
