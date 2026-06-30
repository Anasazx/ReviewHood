package tn.anasazx.tunirate.dashboard.adminDashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.dashboard.adminDashboard.dto.AdminDashboardResponse;
import tn.anasazx.tunirate.dashboard.adminDashboard.service.AdminDashboardService;



@RestController
@RequestMapping("/admindashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<AdminDashboardResponse> getMyAdminDashboard() {
        return ResponseEntity.ok(adminDashboardService.getMyAdminDashboard());
    }

}