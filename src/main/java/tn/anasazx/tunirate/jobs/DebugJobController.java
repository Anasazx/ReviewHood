package tn.anasazx.tunirate.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.comment.job.CommentLikeReconciliationJob;
import tn.anasazx.tunirate.product.job.ProductReviewStatsReconciliationJob;
import tn.anasazx.tunirate.review.job.ReviewLikeReconciliationJob;

@RestController
@RequestMapping("/admin/jobs")
@RequiredArgsConstructor
public class DebugJobController {

    private final ReviewLikeReconciliationJob reviewJob;
    private final CommentLikeReconciliationJob commentJob;
    private final ProductReviewStatsReconciliationJob productJob;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reconcile-likes")
    public ResponseEntity<Void> runReconciliation() {
        reviewJob.reconcileLikeCounts();
        commentJob.reconcileLikeCounts();
        productJob.reconcileReviewStats();
        return ResponseEntity.ok().build();
    }

}