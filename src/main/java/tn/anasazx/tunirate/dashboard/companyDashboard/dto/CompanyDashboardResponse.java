package tn.anasazx.tunirate.dashboard.companyDashboard.dto;

import tn.anasazx.tunirate.dashboard.adminDashboard.dto.TimeSeriesPointDTO;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;

import java.util.List;
import java.util.Map;

public record CompanyDashboardResponse(
        Long totalProducts,
        Long totalReviews,
        Double averageRating,
        Long totalTeamMembers,
        List<MinimizedReviewResponse> recentReviews,
        // Reviews the company hasn't replied to yet
        Long unansweredReviews,
        // star rating (1-5) -> number of reviews
        Map<Integer, Long> ratingDistribution,
        Map<ProductStatus, Long> productsByStatus,
        List<TimeSeriesPointDTO> reviewsOverTime
) {}
