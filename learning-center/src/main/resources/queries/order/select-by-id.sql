SELECT id,
       student_id,
       course_id,
       price,
       created_at,
       payment_at,
       payment_id
FROM orders
WHERE id = ?;