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

-- DROP TABLE yearly_assets;

CREATE TABLE IF NOT EXISTS yearly_assets (
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

CREATE TABLE IF NOT EXISTS asset_goals (
    id INT PRIMARY KEY AUTO_INCREMENT,     -- 主キー
    goal_amount DECIMAL(15,2) NOT NULL,    -- 目標金額
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 更新日時
);

CREATE TABLE IF NOT EXISTS simulation_conditions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,     -- 主キー
    condition_name VARCHAR(100) NOT NULL,     -- シミュレーション条件名
    monthly_investment DECIMAL(15,2) NOT NULL, -- 毎月の追加投資金額
    annual_return_rate DECIMAL(5,4) NOT NULL,  -- 年間運用利回り（0.05 = 5%）
    investment_period_years INT NOT NULL,      -- 投資期間（年）
    initial_amount DECIMAL(15,2)              -- 初期投資額（現在の総資産額）
);
