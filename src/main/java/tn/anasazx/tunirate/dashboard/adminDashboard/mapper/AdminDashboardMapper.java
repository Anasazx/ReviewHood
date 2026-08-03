package tn.anasazx.tunirate.dashboard.adminDashboard.mapper;

import tn.anasazx.tunirate.dashboard.adminDashboard.dto.AdminDashboardResponse;
import tn.anasazx.tunirate.dashboard.adminDashboard.dto.TimeSeriesPointDTO;

import java.util.List;


public class AdminDashboardMapper {

    public static AdminDashboardResponse toDto(
            Long totalUsers,
            Long totalCompanies,
            Long totalVerifiedCompanies,
            Long totalProducts,
            Long totalReviews,
            Long totalPendingApprovals,
            List<TimeSeriesPointDTO> usersOverTime,
            List<TimeSeriesPointDTO> companiesOverTime,
            List<TimeSeriesPointDTO> reviewsOverTime
    ) {
        return new AdminDashboardResponse(
                totalUsers,
                totalCompanies,
                totalVerifiedCompanies,
                totalProducts,
                totalReviews,
                totalPendingApprovals,
                usersOverTime,
                companiesOverTime,
                reviewsOverTime
        );
    }
}