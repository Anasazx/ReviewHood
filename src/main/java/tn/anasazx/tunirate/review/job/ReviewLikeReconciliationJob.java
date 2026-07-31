package tn.anasazx.tunirate.review.job;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.review.repository.ReviewRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewLikeReconciliationJob {

    private final ReviewRepository reviewRepository;

    @Scheduled(cron = "0 0 3 * * *") // every night at 3am
    @Transactional
    public void reconcileLikeCounts() {
        int updated = reviewRepository.reconcileAllLikeCounts();
        log.info("Reconciled like counts for {} reviews", updated);
    }

}
