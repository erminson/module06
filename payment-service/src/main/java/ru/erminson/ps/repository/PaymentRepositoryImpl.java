package ru.erminson.ps.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.erminson.ps.entity.PaymentEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(PaymentEntity paymentEntity) {
        String sql = "INSERT INTO payment (order_id, price) VALUES (?, ?);";
        try {
            jdbcTemplate.update(sql, paymentEntity.getOrderId(), paymentEntity.getPrice());
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public Optional<PaymentEntity> findByOrderId(long orderId) {
        PaymentEntity paymentEntity = null;
        String sql = "SELECT id, order_id, price, payment_at FROM payment WHERE order_id = ?;";
        try {
            paymentEntity = jdbcTemplate.queryForObject(sql, new PaymentEntityRowMapper(), orderId);
        } catch (EmptyResultDataAccessException e) {
        }

        return Optional.ofNullable(paymentEntity);
    }

    private static class PaymentEntityRowMapper implements RowMapper<PaymentEntity> {
        @Override
        public PaymentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            PaymentEntity paymentEntity = new PaymentEntity();

            paymentEntity.setId(rs.getLong("id"));
            paymentEntity.setOrderId(rs.getLong("order_id"));
            paymentEntity.setPrice(rs.getBigDecimal("price"));

            Timestamp timestamp = rs.getTimestamp("payment_at");
            if (Objects.nonNull(timestamp)) {
                paymentEntity.setPaymentAt(timestamp.toLocalDateTime());
            }

            return paymentEntity;
        }
    }
}
