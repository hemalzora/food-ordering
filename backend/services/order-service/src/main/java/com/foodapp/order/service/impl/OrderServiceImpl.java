package com.foodapp.order.service.impl;

import com.foodapp.order.dto.LineView;
import com.foodapp.order.dto.OrderResponse;
import com.foodapp.order.dto.PlaceOrderRequest;
import com.foodapp.order.dto.PricedCart;
import com.foodapp.order.entity.Order;
import com.foodapp.order.entity.OrderItem;
import com.foodapp.order.entity.OrderStatus;
import com.foodapp.order.exception.InvalidOrderException;
import com.foodapp.order.exception.ResourceNotFoundException;
import com.foodapp.order.repository.OrderRepository;
import com.foodapp.order.service.CartService;
import com.foodapp.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest placeOrderRequest) {
        PricedCart pricedCart = cartService.priceCart(userId);
        if (pricedCart.lines().isEmpty()) throw new InvalidOrderException("Cart is empty");
        Order order = new Order(userId, pricedCart.restaurantId(), OrderStatus.PLACED, pricedCart.total(),
                placeOrderRequest.deliveryAddress(), placeOrderRequest.paymentMethod());
        for (LineView lineView : pricedCart.lines()) {
            order.addItem(new OrderItem(lineView.menuItemId(), lineView.name(), lineView.unitPrice(), lineView.quantity(), lineView.lineTotal()));
        }
        Order saved = orderRepository.save(order);
        cartService.clearCart(userId);
        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> listOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderId + " not found"));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order " + orderId + " not found"));
        order.setOrderStatus(orderStatus);
        // Managed entity: JPA dirty checking flushes these changes at commit - no save() needed.
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<LineView> items = order.getItems().stream()
                .map(orderItem -> new LineView(orderItem.getMenuItemId(), orderItem.getItemName(), orderItem.getUnitPrice(), orderItem.getQuantity(), orderItem.getLineTotal()))
                .toList();
        return new OrderResponse(order.getOrderId(), order.getUserId(), order.getRestaurantId(), order.getOrderStatus().name(),
                order.getTotalAmount(), order.getDeliveryAddress(), order.getPaymentMethod().name(), items, order.getCreatedAt());
    }
}
