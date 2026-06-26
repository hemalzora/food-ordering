package com.foodapp.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartView(
        List<LineView> items,
        BigDecimal total) {
}
