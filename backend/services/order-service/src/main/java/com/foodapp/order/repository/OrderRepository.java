package com.foodapp.order.repository;

import com.foodapp.order.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*
     * @EntityGraph eagerly join-fetches each order's "items" in the SAME query.
     * Without it, "items" is LAZY, so mapping N orders to responses would fire N
     * extra "SELECT ... FROM order_items" queries (the N+1 problem). The graph
     * collapses that into a single join. Safe here because there is no pagination:
     * a collection fetch combined with Pageable would force in-memory paging.
     */
    @EntityGraph(attributePaths = "items")
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /*
     * Same idea for the single-order read: fetch the order and its items in one
     * query instead of a lazy follow-up when toResponse() walks the line items.
     */
    @EntityGraph(attributePaths = "items")
    Optional<Order> findById(Long orderId);
}
