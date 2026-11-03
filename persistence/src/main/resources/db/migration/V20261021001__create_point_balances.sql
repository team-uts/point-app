CREATE TABLE IF NOT EXISTS `point_balances`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    owner_id                BIGINT UNSIGNED     NOT NULL COMMENT 'member ID',
    owner_type              VARCHAR(32)         NOT NULL COMMENT 'owner type (CUSTOMER, MERCHANT)',
    amount                  DECIMAL(15, 2)      NOT NULL COMMENT 'point balance amount',
    currency                CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    status                  VARCHAR(32)         NOT NULL COMMENT 'balance status',
    created_at              DATETIME(6)         NOT NULL,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX uk_pb_ownerid_ownertype_currency ON point_balances (owner_id, owner_type, currency);
