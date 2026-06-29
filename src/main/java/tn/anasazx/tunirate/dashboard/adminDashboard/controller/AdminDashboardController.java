package tn.anasazx.tunirate.dashboard.adminDashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<AdminDashboardResponse> getMyAdminDashboard() {
        return ResponseEntity.ok(adminDashboardService.getMyAdminDashboard());
    }

}