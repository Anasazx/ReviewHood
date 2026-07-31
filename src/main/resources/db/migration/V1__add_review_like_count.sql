ALTER TABLE reviews ADD COLUMN like_count BIGINT NOT NULL DEFAULT 0;

UPDATE reviews r
SET like_count = (
    SELECT COUNT(*) FROM review_likes rl WHERE rl.review_id = r.id
);