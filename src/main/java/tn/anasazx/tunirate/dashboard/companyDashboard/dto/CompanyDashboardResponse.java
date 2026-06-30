package tn.anasazx.tunirate.dashboard.companyDashboard.dto;

import tn.anasazx.tunirate.review.dto.MinimizedReviewResponse;

import java.util.List;

public record CompanyDashboardResponse(
        Long totalProducts,
        Long totalReviews,
        Double averageRating,
        Long totalTeamMembers,
        List<MinimizedReviewResponse> recentReviews //get last 3 reviews
) {}