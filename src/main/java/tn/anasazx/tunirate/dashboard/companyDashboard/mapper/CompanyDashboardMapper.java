package tn.anasazx.tunirate.dashboard.companyDashboard.mapper;

import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.mapper.ReviewMapper;

import java.util.List;

public class CompanyDashboardMapper {

    public static CompanyDashboardResponse toDto(
            Long totalProducts,
            Long totalReviews,
            Double averageRating,
            Long totalTeamMembers,
            List<Review> recentReviews
    ) {
        return new CompanyDashboardResponse(
                totalProducts,
                totalReviews,
                averageRating,
                totalTeamMembers,
                recentReviews.stream().map(ReviewMapper::toMinimizedResponse).toList()
        );
    }
}