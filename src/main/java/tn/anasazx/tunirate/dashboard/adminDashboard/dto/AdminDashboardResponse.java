package tn.anasazx.tunirate.dashboard.adminDashboard.dto;

import java.util.List;

public record AdminDashboardResponse (
        Long totalUsers,
        Long totalCompanies,
        Long totalActiveCompanies,
        Long totalProducts,
        Long totalReviews,
        Long totalPendingApprovals,
        List<TimeSeriesPointDTO> usersOverTime,
        List<TimeSeriesPointDTO> companiesOverTime,
        List<TimeSeriesPointDTO> reviewsOverTime
){}
