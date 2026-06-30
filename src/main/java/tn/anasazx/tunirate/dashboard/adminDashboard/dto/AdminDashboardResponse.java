package tn.anasazx.tunirate.dashboard.adminDashboard.dto;

public record AdminDashboardResponse (
        Long totalUsers,
        Long totalCompanies,
        Long totalActiveCompanies,
        Long totalProducts,
        Long totalReviews,
        Long totalPendingApprovals
){}
