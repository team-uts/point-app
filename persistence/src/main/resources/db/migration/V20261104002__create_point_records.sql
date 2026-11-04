CREATE TABLE IF NOT EXISTS `point_records`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    balance_id              BIGINT UNSIGNED     NOT NULL COMMENT 'point balance ID',
    owner_type              VARCHAR(32)         NOT NULL COMMENT 'owner type (CUSTOMER, MERCHANT)',
    point_transaction_id    BIGINT UNSIGNED     NOT NULL COMMENT 'point transaction ID',
    point_template_id       BIGINT UNSIGNED     NOT NULL COMMENT 'point template ID',
    total_amount            DECIMAL(15, 2)      NOT NULL COMMENT 'total point amount',
    remaining_amount        DECIMAL(15, 2)      NOT NULL COMMENT 'remaining point amount after usage',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    expires_at              DATETIME(6)         NOT NULL COMMENT 'point expiration date',
    expired_at              DATETIME(6)         NULL COMMENT 'point expired date',
    status                  VARCHAR(32)         NOT NULL COMMENT 'point record status',
    created_at              DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pr_balanceid ON point_records (balance_id);
