package com.clicKart.order.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.common.OrderStatus;
import com.clicKart.order.entity.Order;



@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);//newest order comes first.


    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdAndStatus(
            Long userId,
            OrderStatus status
    );
}