CREATE TABLE IF NOT EXISTS `point_balances`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    member_id               BIGINT UNSIGNED     NOT NULL COMMENT 'owner member ID',
    owner_type              VARCHAR(32)         NOT NULL COMMENT 'owner type (CUSTOMER, MERCHANT)',
    cash_amount             DECIMAL(15, 2)      NOT NULL COMMENT 'cashable point amount',
    exp_amount              DECIMAL(15, 2)      NOT NULL COMMENT 'expirable point amount',
    total_amount            DECIMAL(15, 2)      NOT NULL COMMENT 'total point amount',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    status                  VARCHAR(32)         NOT NULL COMMENT 'balance status',
    last_transaction_id     BIGINT UNSIGNED     NULL COMMENT 'last transaction ID',
    created_at              DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX uk_pb_memberid_ownertype_currency ON point_balances (member_id, owner_type, currency);
