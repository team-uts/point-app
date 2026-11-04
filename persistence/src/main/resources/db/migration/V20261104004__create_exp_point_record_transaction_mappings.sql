CREATE TABLE IF NOT EXISTS `exp_point_record_mappings`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    point_transaction_id    BIGINT UNSIGNED     NOT NULL COMMENT 'point transaction ID',
    exp_point_record_id     BIGINT UNSIGNED     NOT NULL COMMENT 'exp point record ID',
    amount                  DECIMAL(15, 2)      NOT NULL COMMENT 'point amount',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    created_at              DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_eprm_pointtransactionid ON exp_point_record_mappings (point_transaction_id);
CREATE INDEX ix_eprm_exppointrecordid ON exp_point_record_mappings (exp_point_record_id);
