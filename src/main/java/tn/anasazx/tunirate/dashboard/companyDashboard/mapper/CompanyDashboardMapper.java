package tn.anasazx.tunirate.dashboard.companyDashboard.mapper;

import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;

public class CompanyDashboardMapper {

    public static CompanyDashboardResponse toDto(
            Long totalProducts,
            Long totalReviews,
            Double averageRating,
            Long totalTeamMembers
    ) {
        return new CompanyDashboardResponse(
                totalProducts,
                totalReviews,
                averageRating,
                totalTeamMembers
        );
    }
}