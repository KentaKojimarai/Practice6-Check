CREATE TABLE summary_taxproduct (
    Dcount INTEGER,
    Scount NUMERIC
);
SELECT COUNT(*) AS Dcount FROM taxproduct;
SELECT SUM(price) AS Scount FROM taxproduct;
INSERT INTO summary_taxproduct (Dcount, Scount)
SELECT
    (SELECT COUNT(*) FROM taxproduct),
    (SELECT SUM(price) FROM taxproduct);