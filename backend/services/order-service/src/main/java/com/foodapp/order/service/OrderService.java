package com.foodapp.order.service;

import com.foodapp.order.dto.OrderResponse;
import com.foodapp.order.dto.PlaceOrderRequest;
import com.foodapp.order.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Long userId, PlaceOrderRequest placeOrderRequest);

    List<OrderResponse> listOrders(Long userId);

    OrderResponse getOrder(Long orderId);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus);
}
