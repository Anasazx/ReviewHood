DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_name='review_likes'
    ) THEN
        UPDATE reviews r
        SET like_count = (
            SELECT COUNT(*)
            FROM review_likes rl
            WHERE rl.review_id = r.id
        );
    END IF;
END $$;