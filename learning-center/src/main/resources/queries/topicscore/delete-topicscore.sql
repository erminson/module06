DELETE
FROM topic_score
WHERE id IN (SELECT ts.id
             FROM topic_score AS ts
                      JOIN record_book_topic_score rbts ON rbts.topic_score_id = ts.id
             WHERE rbts.record_book_id = ?);