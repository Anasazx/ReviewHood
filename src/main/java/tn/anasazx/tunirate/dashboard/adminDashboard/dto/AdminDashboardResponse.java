package tn.anasazx.tunirate.dashboard.adminDashboard.dto;

import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.dto.AdminProductResponse;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;

import java.util.List;
import java.util.Map;

public record AdminDashboardResponse (
        Long totalUsers,
        Long totalCompanies,
        Long totalActiveCompanies,
        Long totalProducts,
        Long totalReviews,
        Long totalPendingApprovals,
        // Work waiting for an admin
        Long totalPendingCompanies,
        Long totalPendingSuggestions,
        Map<ProductStatus, Long> productsByStatus,
        Map<CompanyStatus, Long> companiesByStatus,
        List<TimeSeriesPointDTO> usersOverTime,
        List<TimeSeriesPointDTO> companiesOverTime,
        List<TimeSeriesPointDTO> reviewsOverTime,
        List<MinimizedReviewResponse> recentReviews,
        List<AdminProductResponse> pendingProducts
){}
