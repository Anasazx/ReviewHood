package tn.anasazx.tunirate.dashboard.companyDashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;
import tn.anasazx.tunirate.dashboard.companyDashboard.service.CompanyDashboardService;
import tn.anasazx.tunirate.security.SecurityUtils;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class CompanyDashboardController {

    private final CompanyDashboardService companyDashboardService;

    @GetMapping
    public ResponseEntity<CompanyDashboardResponse> getMyCompanyDashboard() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(companyDashboardService.getMyCompanyDashboard(currentUserId));
    }

}