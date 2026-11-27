package com.clicKart.admin.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    private BigDecimal totalRevenue;

    private Long totalOrders;

    private Long totalProducts;

    private Long totalCustomers;

    private Long pendingOrders;

    private Long confirmedOrders;

    private Long shippedOrders;

    private Long deliveredOrders;

    private List<RecentOrderResponse> recentOrders;
}