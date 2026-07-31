package tn.anasazx.tunirate.comment.job;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.comment.repository.CommentRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentLikeReconciliationJob {

    private final CommentRepository commentRepository;

    @Scheduled(cron = "0 15 3 * * *") // 3:15am, offset from the review job
    @Transactional
    public void reconcileLikeCounts() {
        int updated = commentRepository.reconcileAllLikeCounts();
        log.info("Reconciled like counts for {} comments", updated);
    }

}