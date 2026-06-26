package com.foodapp.order.dto;

import java.math.BigDecimal;

public record LineView(
        Long menuItemId,
        String name,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal) {
}
