package com.clicKart.admin.service;



import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.admin.dto.AdminDashboardResponse;
import com.clicKart.admin.dto.RecentOrderResponse;
import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;
import com.clicKart.common.Role;
import com.clicKart.order.entity.Order;
import com.clicKart.order.repository.OrderRepository;
import com.clicKart.product.repository.ProductRepository;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public AdminDashboardResponse getDashboardData() {

        // Total revenue from successfully paid orders
        BigDecimal totalRevenue =
                orderRepository.getTotalRevenueByPaymentStatus(
                        PaymentStatus.PAID
                );

        // Total orders
        long totalOrders = orderRepository.count();

        // Total products
        long totalProducts = productRepository.count();

        // Total customers (excluding admin accounts)
        long totalCustomers = userRepository.countByRole(Role.USER);

        // Order status counts
        long pendingOrders =
                orderRepository.countByStatus(OrderStatus.PENDING);

        long confirmedOrders =
                orderRepository.countByStatus(OrderStatus.CONFIRMED);

        long shippedOrders =
                orderRepository.countByStatus(OrderStatus.SHIPPED);

        long deliveredOrders =
                orderRepository.countByStatus(OrderStatus.DELIVERED);

        // Five most recent orders
        List<RecentOrderResponse> recentOrders =
                orderRepository.findTop5ByOrderByCreatedAtDesc()
                        .stream()
                        .map(this::mapToRecentOrderResponse)
                        .toList();

        return new AdminDashboardResponse(
                totalRevenue,
                totalOrders,
                totalProducts,
                totalCustomers,
                pendingOrders,
                confirmedOrders,
                shippedOrders,
                deliveredOrders,
                recentOrders
        );
    }

    private RecentOrderResponse mapToRecentOrderResponse(Order order) {

        return new RecentOrderResponse(
                order.getId(),
                order.getUser().getName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}