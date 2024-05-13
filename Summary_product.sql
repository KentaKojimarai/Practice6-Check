-- 新しいテーブルを作成
CREATE TABLE summary_product (
    Dcount INTEGER,
    Scount NUMERIC
);
-- データの行数を取得
SELECT COUNT(*) AS Dcount FROM product;
-- 数値の合計を取得
SELECT SUM(price) AS Scount FROM product;
-- データを挿入
INSERT INTO summary_product (Dcount, Scount)
SELECT
    (SELECT COUNT(*) FROM product),
    (SELECT SUM(price) FROM product);