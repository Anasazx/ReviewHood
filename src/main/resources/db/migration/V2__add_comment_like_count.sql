ALTER TABLE comments ADD COLUMN like_count BIGINT NOT NULL DEFAULT 0;

UPDATE comments c
SET like_count = (
    SELECT COUNT(*) FROM comment_likes cl WHERE cl.comment_id = c.id
);