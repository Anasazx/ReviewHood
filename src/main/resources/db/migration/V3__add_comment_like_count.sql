-- Create review likes table
CREATE TABLE review_likes (
                              id BIGSERIAL PRIMARY KEY,

                              review_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,

                              created_at TIMESTAMP NOT NULL,

                              CONSTRAINT fk_review_likes_review
                                  FOREIGN KEY (review_id)
                                      REFERENCES reviews(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_review_likes_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT uk_review_likes_user_review
                                  UNIQUE (user_id, review_id)
);


CREATE INDEX idx_review_likes_review_id
    ON review_likes(review_id);

CREATE INDEX idx_review_likes_user_id
    ON review_likes(user_id);



-- Create comment likes table
CREATE TABLE comment_likes (
                               id BIGSERIAL PRIMARY KEY,

                               comment_id BIGINT NOT NULL,
                               user_id BIGINT NOT NULL,

                               created_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_comment_likes_comment
                                   FOREIGN KEY (comment_id)
                                       REFERENCES comments(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_comment_likes_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uk_comment_likes_user_comment
                                   UNIQUE (user_id, comment_id)
);


CREATE INDEX idx_comment_likes_comment_id
    ON comment_likes(comment_id);

CREATE INDEX idx_comment_likes_user_id
    ON comment_likes(user_id);