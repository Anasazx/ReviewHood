package tn.anasazx.tunirate.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.product.repository.ProductImageRepository;
import tn.anasazx.tunirate.product.repository.ProductRepository;

/**
 * Company portal access is based on company membership, not on the global role.
 * Used in @PreAuthorize, e.g. @PreAuthorize("@companyAccess.isMember()").
 */
@Component("companyAccess")
@RequiredArgsConstructor
public class CompanyAccess {

    private final CompanyMemberRepository companyMemberRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    // Current user belongs to a company (HEAD or WORKER)
    public boolean isMember() {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        return currentUserId != null && companyMemberRepository.findFirstByUserId(currentUserId).isPresent();
    }

    // Current user belongs to the company that owns this product
    public boolean ownsProduct(Long productId) {
        Long companyId = currentCompanyId();
        return companyId != null && productId != null && productRepository.existsByIdAndCompanyId(productId, companyId);
    }

    // Current user belongs to the company that owns the product of this image
    public boolean ownsImage(Long imageId) {
        Long companyId = currentCompanyId();
        return companyId != null && imageId != null && productImageRepository.existsByIdAndProductCompanyId(imageId, companyId);
    }

    private Long currentCompanyId() {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        if (currentUserId == null) return null;
        return companyMemberRepository.findFirstByUserId(currentUserId)
                .map(member -> member.getCompany().getId())
                .orElse(null);
    }

    // Current user is the HEAD of their company
    public boolean isHead() {
        Long currentUserId = SecurityUtils.getCurrentUserIdOrNull();
        return currentUserId != null && companyMemberRepository.findFirstByUserId(currentUserId)
                .map(member -> member.getCompanyRole() == CompanyRole.HEAD)
                .orElse(false);
    }

}
