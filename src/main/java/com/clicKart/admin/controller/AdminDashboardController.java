package com.clicKart.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.admin.dto.AdminDashboardResponse;
import com.clicKart.admin.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<AdminDashboardResponse> getDashboardData() {

        AdminDashboardResponse response =
                dashboardService.getDashboardData();

        return ResponseEntity.ok(response);
    }
}