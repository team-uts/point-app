CREATE TABLE IF NOT EXISTS `point_transactions`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    balance_id              BIGINT UNSIGNED     NOT NULL COMMENT 'point balance ID',
    owner_type              VARCHAR(32)         NOT NULL COMMENT 'owner type (CUSTOMER, MERCHANT)',
    transaction_type        VARCHAR(32)         NOT NULL COMMENT 'transaction type (EARN, REDEEM, ADJUSTMENT)',
    operation_type          VARCHAR(32)         NOT NULL COMMENT 'operation type (CREDIT, DEBIT)',
    cash_amount             DECIMAL(15, 2)      NOT NULL COMMENT 'cashable point amount',
    exp_amount              DECIMAL(15, 2)      NOT NULL COMMENT 'expirable point amount',
    after_balance_amount    DECIMAL(15, 2)      NOT NULL COMMENT 'point balance amount after transaction',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    pay_transaction_id      VARCHAR(50)         NOT NULL COMMENT 'payment transaction ID',
    payment_type            VARCHAR(32)         NOT NULL COMMENT 'payment type...',
    created_at              DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_pt_balanceid_createdat ON point_transactions (balance_id, created_at);
CREATE INDEX ix_pt_balanceid_transactiontype_createdat ON point_transactions (balance_id, transaction_type, created_at);
