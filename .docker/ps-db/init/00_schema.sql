CREATE TABLE IF NOT EXISTS payment
(
    id         SERIAL PRIMARY KEY,
    order_id   BIGINT    NOT NULL UNIQUE,
    price      NUMERIC,
    payment_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);