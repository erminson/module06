SELECT s.id   AS student_id,
       s.name AS student_name
FROM record_book rb
         JOIN users s ON rb.student_id = s.id;