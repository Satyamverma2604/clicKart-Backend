package com.clicKart.order.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;
import com.clicKart.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdAndStatus(
            Long userId,
            OrderStatus status
    );

    // Dashboard queries

    long countByStatus(OrderStatus status);

    List<Order> findTop5ByOrderByCreatedAtDesc();

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
            WHERE o.paymentStatus = :paymentStatus
            """)
    BigDecimal getTotalRevenueByPaymentStatus(
            @Param("paymentStatus") PaymentStatus paymentStatus
    );
}