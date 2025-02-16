CREATE TABLE IF NOT EXISTS `users`(
	`username` VARCHAR_IGNORECASE(50) NOT NULL PRIMARY KEY,
	`password` VARCHAR_IGNORECASE(500) NOT NULL,
	`enabled`  BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS `authorities` (
	`username`  VARCHAR_IGNORECASE(50) NOT NULL,
	`authority` VARCHAR_IGNORECASE(50) NOT NULL,
	CONSTRAINT `fk_authorities_users` FOREIGN KEY(`username`) REFERENCES `users`(`username`)
);
CREATE UNIQUE INDEX IF NOT EXISTS `ix_auth_username` ON `authorities` (`username`, `authority`);

-- DROP TABLE annual_assets;

CREATE TABLE IF NOT EXISTS annual_assets (
    target_year  INT PRIMARY KEY,         -- 記録する年
    cash DECIMAL(15,2) NOT NULL,  -- 現預金
    securities DECIMAL(15,2) NOT NULL,  -- 有価証券
    crypto DECIMAL(15,2) NOT NULL  -- 暗号資産
);

CREATE TABLE IF NOT EXISTS monthly_assets (
    target_year  INT NOT NULL,            -- 記録する年
    target_month INT NOT NULL,           -- 記録する月 (1～12)
    cash DECIMAL(15,2) NOT NULL,  -- 現預金
    securities DECIMAL(15,2) NOT NULL,  -- 有価証券
    crypto DECIMAL(15,2) NOT NULL,  -- 暗号資産
    PRIMARY KEY (target_year, target_month)     -- 年月の組み合わせをユニークに
);
