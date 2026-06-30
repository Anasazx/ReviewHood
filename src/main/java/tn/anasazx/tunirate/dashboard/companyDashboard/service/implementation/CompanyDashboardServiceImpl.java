package tn.anasazx.tunirate.dashboard.companyDashboard.service.implementation;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;
import tn.anasazx.tunirate.dashboard.companyDashboard.mapper.CompanyDashboardMapper;
import tn.anasazx.tunirate.dashboard.companyDashboard.service.CompanyDashboardService;

import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.security.SecurityUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyDashboardServiceImpl implements CompanyDashboardService {

    private final ProductRepository productRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public CompanyDashboardResponse getMyCompanyDashboard() {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        CompanyMember membership = companyMemberRepository.findFirstByUserId(currentUserId).orElseThrow(
                () -> new RuntimeException("User doesnt belong to any company")
        );

        Long companyId = membership.getCompany().getId();

        Long totalProducts = productRepository.countByCompanyId(companyId);

        Long totalReviews = reviewRepository.countByProductCompanyId(companyId);

        Long totalTeamMembers = companyMemberRepository.countByCompanyId(companyId);

        Double averageRating = reviewRepository.findAverageRatingByCompanyId(companyId);

        List<Review> recentReviews = reviewRepository.findTop3ByProductCompanyIdOrderByCreatedAtDesc(companyId); //get last 3 reviews

        double averageRatingResponse = Optional.ofNullable(averageRating).orElse(0.0);

        return CompanyDashboardMapper.toDto(totalProducts, totalReviews, averageRatingResponse, totalTeamMembers, recentReviews);
    }

}
