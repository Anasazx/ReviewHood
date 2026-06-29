package tn.anasazx.tunirate.dashboard.adminDashboard.mapper;

import tn.anasazx.tunirate.dashboard.adminDashboard.dto.AdminDashboardResponse;


public class AdminDashboardMapper {

    public static AdminDashboardResponse toDto(
            Long totalUsers,
            Long totalCompanies,
            Long totalVerifiedCompanies,
            Long totalProducts,
            Long totalReviews,
            Long totalPendingApprovals
    ) {
        return new AdminDashboardResponse(
                totalUsers,
                totalCompanies,
                totalVerifiedCompanies,
                totalProducts,
                totalReviews,
                totalPendingApprovals
        );
    }
}