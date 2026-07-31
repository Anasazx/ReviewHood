package tn.anasazx.tunirate.product.job;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.product.repository.ProductRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductReviewStatsReconciliationJob {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 30 3 * * *") // 3:30am, staggered after the two like jobs
    @Transactional
    public void reconcileReviewStats() {
        int updated = productRepository.reconcileReviewStats();
        log.info("Reconciled review stats for {} products", updated);
    }
}
