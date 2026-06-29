package tn.anasazx.tunirate.dashboard.adminDashboard.dto;

import tn.anasazx.tunirate.enums.ProductStatus;

public record AdminDashboardResponse (
        Long totalUsers,
        Long totalCompanies,
        Long totalVerifiedCompanies,
        Long totalProducts,
        Long totalReviews,
        Long totalPendingApprovals
){}
