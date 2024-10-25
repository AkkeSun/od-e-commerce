CREATE TABLE IF NOT EXISTS ACCOUNT
(
    TABLE_INDEX  int auto_increment primary key,
    EMAIL varchar(50)  not null,
    PASSWORD varchar(100) not null,
    USER_NAME varchar(50),
    USER_TEL varchar(50),
    ADDRESS varchar(100),
    REG_DATE varchar(8) NOT NULL,
    REG_DATE_TIME timestamp NOT NULL,
    ROLE varchar(50) not null
);
CREATE INDEX IF NOT EXISTS ACCOUNT_IDX_1 ON ACCOUNT (EMAIL);
CREATE INDEX IF NOT EXISTS ACCOUNT_IDX_2 ON ACCOUNT (EMAIL, PASSWORD);


CREATE TABLE IF NOT EXISTS TOKEN
(
    TABLE_INDEX  INT AUTO_INCREMENT PRIMARY KEY,
    EMAIL    VARCHAR(50) NOT NULL,
    USER_AGENT   VARCHAR(500) NOT NULL,
    REFRESH_TOKEN VARCHAR(200) NOT NULL,
    REG_DATE_TIME     VARCHAR(50) NOT NULL,
    ROLE VARCHAR(50) NOT NULL,
    UNIQUE KEY (EMAIL, USER_AGENT, REFRESH_TOKEN)
);
CREATE INDEX IF NOT EXISTS TOKEN_IDX_1 ON TOKEN (REG_DATE_TIME);

