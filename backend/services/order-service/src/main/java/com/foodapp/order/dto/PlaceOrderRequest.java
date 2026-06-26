package com.foodapp.order.dto;

import com.foodapp.order.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceOrderRequest(
        @NotBlank String deliveryAddress,
        @NotNull PaymentMethod paymentMethod) {
}
