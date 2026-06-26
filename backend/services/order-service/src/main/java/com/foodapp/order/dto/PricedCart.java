package com.foodapp.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record PricedCart(
        Long restaurantId,
        List<LineView> lines,
        BigDecimal total) {
}
