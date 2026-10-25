package tn.anasazx.tunirate.dashboard.companyDashboard.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.dashboard.TimeSeries;
import tn.anasazx.tunirate.dashboard.companyDashboard.dto.CompanyDashboardResponse;
import tn.anasazx.tunirate.dashboard.companyDashboard.service.CompanyDashboardService;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.mapper.ReviewMapper;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.review.repository.ReviewSpecifications;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyDashboardServiceImpl implements CompanyDashboardService {

    private static final int DAYS = 30;

    private final ProductRepository productRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public CompanyDashboardResponse getMyCompanyDashboard(Long currentUserId) {

        CompanyMember membership = companyMemberRepository.findFirstByUserId(currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't belong to any company")
        );

        Long companyId = membership.getCompany().getId();

        Specification<Review> unanswered = Specification
                .where(ReviewSpecifications.forCompany(companyId))
                .and(ReviewSpecifications.notAnsweredBy(companyId, true));

        Map<Integer, Long> ratingDistribution = new LinkedHashMap<>();
        for (int stars = 5; stars >= 1; stars--) {
            ratingDistribution.put(stars, 0L);
        }
        for (Object[] row : reviewRepository.countByRatingForCompany(companyId)) {
            ratingDistribution.put((Integer) row[0], (Long) row[1]);
        }

        Map<ProductStatus, Long> productsByStatus = new EnumMap<>(ProductStatus.class);
        for (ProductStatus status : ProductStatus.values()) {
            productsByStatus.put(status, 0L);
        }
        for (Object[] row : productRepository.countByStatusGrouped(companyId)) {
            productsByStatus.put((ProductStatus) row[0], (Long) row[1]);
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(DAYS);

        return new CompanyDashboardResponse(
                productRepository.countByCompanyId(companyId),
                reviewRepository.countByProductCompanyId(companyId),
                Optional.ofNullable(reviewRepository.findAverageRatingByCompanyId(companyId)).orElse(0.0),
                companyMemberRepository.countByCompanyId(companyId),
                reviewRepository.findTop5ByProductCompanyIdOrderByCreatedAtDesc(companyId)
                        .stream().map(ReviewMapper::toMinimizedResponse).toList(),
                reviewRepository.count(unanswered),
                ratingDistribution,
                productsByStatus,
                TimeSeries.daily(reviewRepository.countCompanyReviewsByDayRaw(companyId, startDate), startDate.toLocalDate(), DAYS)
        );
    }
}
