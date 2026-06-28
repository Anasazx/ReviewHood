package tn.anasazx.tunirate.dashboard.companyDashboard.dto;

public record CompanyDashboardResponse(
        Long totalProducts,
        Long totalReviews,
        Double averageRating,
        Long totalTeamMembers
) {}