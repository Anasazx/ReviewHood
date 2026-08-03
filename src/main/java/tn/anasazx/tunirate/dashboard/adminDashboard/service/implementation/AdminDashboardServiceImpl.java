package tn.anasazx.tunirate.dashboard.adminDashboard.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.dashboard.adminDashboard.dto.AdminDashboardResponse;
import tn.anasazx.tunirate.dashboard.adminDashboard.dto.TimeSeriesPointDTO;
import tn.anasazx.tunirate.dashboard.adminDashboard.mapper.AdminDashboardMapper;
import tn.anasazx.tunirate.dashboard.adminDashboard.service.AdminDashboardService;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public AdminDashboardResponse getMyAdminDashboard() {

        Long totalUsers = userRepository.count();
        Long totalCompanies = companyRepository.count();
        Long totalActiveCompanies = companyRepository.countByStatus(CompanyStatus.ACTIVE);
        Long totalProducts = productRepository.count();
        Long totalReviews = reviewRepository.count();
        Long totalPendingApprovals = productRepository.countByStatus(ProductStatus.PENDING_REVIEW);

        int days = 30;
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        List<TimeSeriesPointDTO> usersOverTime = fillGaps(
                mapToTimeSeries(userRepository.countUsersByDayRaw(startDate)),
                startDate.toLocalDate(), days
        );
        List<TimeSeriesPointDTO> companiesOverTime = fillGaps(
                mapToTimeSeries(companyRepository.countCompaniesByDayRaw(startDate)),
                startDate.toLocalDate(), days
        );
        List<TimeSeriesPointDTO> reviewsOverTime = fillGaps(
                mapToTimeSeries(reviewRepository.countReviewsByDayRaw(startDate)),
                startDate.toLocalDate(), days
        );

        return AdminDashboardMapper.toDto(totalUsers, totalCompanies, totalActiveCompanies, totalProducts, totalReviews, totalPendingApprovals, usersOverTime, companiesOverTime, reviewsOverTime);

    }


    private List<TimeSeriesPointDTO> mapToTimeSeries(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new TimeSeriesPointDTO(
                        toLocalDate(row[0]),
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        throw new IllegalStateException("Unexpected date type: " + value.getClass());
    }

    private List<TimeSeriesPointDTO> fillGaps(List<TimeSeriesPointDTO> raw, LocalDate startDate, int days) {
        Map<LocalDate, Long> countsByDate = raw.stream()
                .collect(Collectors.toMap(TimeSeriesPointDTO::date, TimeSeriesPointDTO::count));

        List<TimeSeriesPointDTO> filled = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            LocalDate date = startDate.plusDays(i);
            filled.add(new TimeSeriesPointDTO(date, countsByDate.getOrDefault(date, 0L)));
        }
        return filled;
    }



}