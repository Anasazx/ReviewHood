DO $$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM information_schema.tables
            WHERE table_name='comment_likes'
        ) THEN

            UPDATE comments c
            SET like_count = (
                SELECT COUNT(*)
                FROM comment_likes cl
                WHERE cl.comment_id = c.id
            );

        END IF;
    END $$;