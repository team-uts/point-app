CREATE TABLE IF NOT EXISTS `point_templates`
(
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT    PRIMARY  KEY,
    template_name           VARCHAR(50)         NOT NULL COMMENT 'external point template name',
    ext_template_id         VARCHAR(50)         NOT NULL COMMENT 'external point template ID',
    owner_type              VARCHAR(32)         NOT NULL COMMENT 'owner type (CUSTOMER, MERCHANT)',
    is_active               BOOLEAN             NOT NULL COMMENT 'is active (true, false)',
    issuance_starts_at      DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'issuance start date',
    issuance_ends_at        DATETIME(6)         NOT NULL COMMENT 'issuance end date',
    issuance_amount_type    VARCHAR(32)         NOT NULL COMMENT 'issuance amount type (FIXED, PERCENTAGE)',
    issuance_amount         DECIMAL(15, 2)      NOT NULL COMMENT 'issuance amount (when FIXED)',
    max_issuance_amount     DECIMAL(15, 2)      NOT NULL COMMENT 'maximum issuance amount',
    issuance_currency       CHAR(3)             NOT NULL COMMENT 'currency type (AUD)',
    expiration_type         VARCHAR(32)         NOT NULL COMMENT 'expiration type (FIXED, YEAR, MONTH, DAY, HOUR)',
    expiration_period       INT                 NULL     COMMENT 'expiration period (when YEAR, MONTH, DAY, HOUR)',
    expires_at              DATETIME(6)         NULL     COMMENT 'expiration date',
    created_at              DATETIME(6)         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              VARCHAR(30)         NOT NULL COMMENT 'creator',
    updated_at              DATETIME(6)         NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by              VARCHAR(30)         NULL COMMENT 'updater'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX uk_ptl_exttemplateid_ownertype ON point_templates (ext_template_id, owner_type);
