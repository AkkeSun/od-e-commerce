CREATE TABLE IF NOT EXISTS PRODUCT
(
    TABLE_INDEX   BIGINT           primary key,
    SELLER_ID     int              not null,
    SELLER_EMAIL  varchar(50)      not null,
    PRODUCT_NAME  varchar(50)      not null,
    PRODUCT_IMG   varchar(50)      not null,
    PRODUCT_OPTION varchar(100),
    DESCRIPTION   TEXT             not null,
    PRICE         int    default 0 not null,
    QUANTITY      int    default 0 not null,
    SALES_COUNT   int    default 0 not null,
    HIT_COUNT     int    default 0 not null,
    REVIEW_COUNT  int    default 0 not null,
    REVIEW_SCORE  double default 0 not null,
    TOTAL_SCORE   double default 0 not null,
    CATEGORY      varchar(20)      NOT NULL,
    EMBEDDING_YN   varchar(1) default 'N' NOT NULL,
    REG_DATE      varchar(8)       NOT NULL,
    REG_DATE_TIME timestamp        NOT NULL
);
CREATE INDEX PRODUCT_IDX_1 ON PRODUCT (SELLER_ID);
CREATE INDEX PRODUCT_IDX_2 ON PRODUCT (CATEGORY, REVIEW_COUNT); -- 리뷰 많은 순
CREATE INDEX PRODUCT_IDX_3 ON PRODUCT (CATEGORY, SALES_COUNT); -- 판매량순
CREATE INDEX PRODUCT_IDX_4 ON PRODUCT (CATEGORY, REG_DATE); -- 최신순
CREATE INDEX PRODUCT_IDX_5 ON PRODUCT (CATEGORY, PRICE); -- 가격순
CREATE INDEX PRODUCT_IDX_6 ON PRODUCT (CATEGORY, TOTAL_SCORE);
-- 추천순 (REVIEW_COUNT * 0.4) + (REVIEW_SCORE * 0.3) + (HIT_COUNT * 0.2) + (SALES_COUNT * 0.1)
CREATE INDEX PRODUCT_IDX_7 ON PRODUCT (EMBEDDING_YN);

CREATE TABLE IF NOT EXISTS PRODUCT_HISTORY
(
    TABLE_INDEX   BIGINT primary key,
    PRODUCT_ID    BIGINT       not null,
    TYPE          varchar(15)  NOT NULL,
    DETAIL_INFO   varchar(100) NOT NULL,
    REG_DATE      varchar(8)   NOT NULL,
    REG_DATE_TIME timestamp    NOT NULL
);
CREATE INDEX PRODUCT_HISTORY_IDX_1 ON PRODUCT_HISTORY (PRODUCT_ID);
CREATE INDEX PRODUCT_HISTORY_IDX_1 ON PRODUCT_HISTORY (REG_DATE_TIME);


CREATE TABLE IF NOT EXISTS PRODUCT_RECOMMEND
(
    TABLE_INDEX   BIGINT primary key,
    PRODUCT_ID    BIGINT      not null,
    CUSTOMER_ID   BIGINT      not null,
    LEVEL         int         not null,
    TYPE          varchar(10) not null,
    REG_DATE      varchar(8)  NOT NULL,
    REG_DATE_TIME timestamp   NOT NULL,
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT (TABLE_INDEX)
);
CREATE INDEX PRODUCT_RECOMMEND_IDX_1 ON PRODUCT_RECOMMEND (CUSTOMER_ID);

CREATE TABLE IF NOT EXISTS REVIEW
(
    TABLE_INDEX   BIGINT     primary key,
    PRODUCT_ID    BIGINT     not null,
    CUSTOMER_ID   int        not null,
    SCORE         int        not null,
    COMMENT       varchar(500),
    REG_DATE      varchar(8) NOT NULL,
    REG_DATE_TIME timestamp  NOT NULL,
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT (TABLE_INDEX)
);
CREATE INDEX REVIEW_IDX_1 ON REVIEW (CUSTOMER_ID);

CREATE TABLE IF NOT EXISTS DLQ_LOG
(
    TABLE_INDEX   BIGINT primary key,
    TOPIC         varchar(100)     not null,
    PAYLOAD       TEXT             not null,
    REG_DATE      varchar(8) NOT NULL,
    REG_DATE_TIME timestamp  NOT NULL,
 );
CREATE INDEX DLQ_LOG_IDX_1 ON DLQ_LOG (TOPIC);