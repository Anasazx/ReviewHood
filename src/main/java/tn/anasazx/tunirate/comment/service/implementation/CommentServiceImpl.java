package tn.anasazx.tunirate.comment.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.comment.dto.CommentResponse;
import tn.anasazx.tunirate.comment.entity.Comment;
import tn.anasazx.tunirate.comment.mapper.CommentMapper;
import tn.anasazx.tunirate.comment.repository.CommentRepository;
import tn.anasazx.tunirate.comment.service.CommentService;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.membership.repository.CompanyMemberRepository;
import tn.anasazx.tunirate.review.entity.Review;
import tn.anasazx.tunirate.review.repository.ReviewRepository;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CompanyMemberRepository companyMemberRepository;
    private final CommentMapper commentMapper;


    @Override
    public List<CommentResponse> getCommentsByReviewId(Long reviewId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Comment> commentList = commentRepository.findByReview_Id(reviewId);
        return commentMapper.toResponseList(commentList, currentUserId);
    }


    @Override
    public CommentResponse createComment(Long reviewId, String content) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setActor(user);
        comment.setReview(review);

        return commentMapper.toResponse(commentRepository.save(comment), currentUserId);
    }

    @Override
    public CommentResponse createCommentAsCompany(Long reviewId, String content) {

        Long currentUserId = SecurityUtils.getCurrentUserId();


        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        CompanyMember membership = companyMemberRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not belong to any company"));

        Company company = membership.getCompany( );
        User user = membership.getUser( );

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setActor(company);
        comment.setPostedBy(user);
        comment.setReview(review);

        return commentMapper.toResponse(commentRepository.save(comment), currentUserId);
    }


    @Override
    public CommentResponse replyToComment(Long parentCommentId, String content) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));

        Comment replyComment = new Comment();
        replyComment.setContent(content);
        replyComment.setActor(user);
        replyComment.setReview(parentComment.getReview());
        replyComment.setRepliedTo(parentComment);

        return commentMapper.toResponse(commentRepository.save(replyComment), currentUserId);
    }

    @Override
    public CommentResponse replyToCommentAsCompany(Long parentCommentId, String content) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));

        CompanyMember membership = companyMemberRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not belong to any company"));

        Company company = membership.getCompany();

        User user = membership.getUser();

        Comment replyComment = new Comment();
        replyComment.setContent(content);
        replyComment.setActor(company);
        replyComment.setPostedBy(user);
        replyComment.setReview(parentComment.getReview());
        replyComment.setRepliedTo(parentComment);

        return commentMapper.toResponse(commentRepository.save(replyComment), currentUserId);
    }


    @Override
    public void deleteComment(Long commentId) {

        Long currentUserId = SecurityUtils.getCurrentUserId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        Actor author = comment.getActor();

        switch (author.getType()) {

            case USER -> {
                if (!author.getId().equals(currentUserId)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your comment");
                }
            }

            case COMPANY -> {
                Long companyId = author.getId();

                CompanyMember membership = companyMemberRepository
                        .findByUserIdAndCompanyId(currentUserId, companyId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.FORBIDDEN, "You don't belong to this company"));

                if (!membership.getCompanyRole().canDeleteComments()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient role");
                }
            }

            // any future ActorType that hasn't been handled yet is denied
            default -> throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Delete not supported for this actor type");
        }

        commentRepository.delete(comment);
    }

}