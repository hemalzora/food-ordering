package com.foodapp.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
        @NotNull Long menuItemId,
        @Min(1) int quantity) {
}
