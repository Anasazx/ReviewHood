package tn.anasazx.tunirate.dashboard.adminDashboard.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.dashboard.adminDashboard.dto.AdminDashboardResponse;
import tn.anasazx.tunirate.dashboard.adminDashboard.mapper.AdminDashboardMapper;
import tn.anasazx.tunirate.dashboard.adminDashboard.service.AdminDashboardService;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.user.repository.UserRepository;


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
        Long totalVerifiedCompanies = companyRepository.countByVerified(true);
        Long totalProducts = productRepository.count();
        Long totalReviews = reviewRepository.count();
        Long totalPendingApprovals = productRepository.countByStatus(ProductStatus.PENDING_REVIEW);

        return AdminDashboardMapper.toDto(totalUsers, totalCompanies, totalVerifiedCompanies, totalProducts, totalReviews, totalPendingApprovals);

    }

}