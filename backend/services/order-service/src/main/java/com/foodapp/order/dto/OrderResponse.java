package com.foodapp.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long userId,
        Long restaurantId,
        String orderStatus,
        BigDecimal totalAmount,
        String deliveryAddress,
        String paymentMethod,
        List<LineView> items,
        Instant createdAt) {
}
