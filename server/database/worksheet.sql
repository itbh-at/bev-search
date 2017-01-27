/*
 * Arbitrary SQL queries for testing purposes
 */

SELECT * FROM BEV.GEMEINDE WHERE GEMEINDENAME = 'Heidenreichstein';
SELECT * FROM BEV.STRASSE WHERE STRASSENNAME LIKE '%Kautzener%' AND GKZ = '30916';
SELECT * FROM BEV.ADRESSE WHERE GKZ = '30916' AND HAUSNRZAHL1 = 7 AND SKZ = '013199';

SELECT ADRCD, count(*) FROM BEV.GEBAEUDE GROUP BY ADRCD HAVING count(*) > 1 ORDER BY count(*) DESC;

SELECT a.plz, o.ortsname, g.gemeindename, s.strassenname, a.*
FROM 
	BEV.ADRESSE a
	JOIN BEV.STRASSE s ON a.skz = s.skz
	JOIN BEV.ORTSCHAFT o ON a.okz = o.okz
	JOIN BEV.GEMEINDE g ON a.gkz = g.gkz
WHERE 
	a.ADRCD = '6992076';
	
SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE a.ADRCD = '6992076';

CREATE OR REPLACE VIEW BEV.ADRESSE_DENORMALIZED AS
SELECT a.*, o.ORTSNAME, s.STRASSENNAME, s.STRASSENNAMENZUSATZ, s.SZUSADRBEST, g.GEMEINDENAME, b.SUBCD, b.OBJEKTNUMMER, b.HAUPTADRESSE, b.HAUSNRVERBINDUNG2, b.HAUSNRZAHL3, b.HAUSNRBUCHSTABE3, b.HAUSNRVERBINDUNG3, b.HAUSNRZAHL4, b.HAUSNRBUCHSTABE4, b.HAUSNRGEBAEUDEBEZ, b.RW RW_GEBAEUDE, b.HW HW_GEBAEUDE, b.EPSG EPSG_GEBAEUDE, b.EIGENSCHAFT, b.QUELLADRESSE QUELLADRESSE_GEBAEUDE, b.BESTIMMUNGSART BESTIMMUNGSART_GEBAEUDE
FROM 
	BEV.ADRESSE a
	JOIN BEV.STRASSE s ON a.skz = s.skz
	JOIN BEV.ORTSCHAFT o ON a.okz = o.okz
	JOIN BEV.GEMEINDE g ON a.gkz = g.gkz
	LEFT JOIN BEV.GEBAEUDE b ON a.adrcd = b.adrcd
;	

SELECT ADRCD FROM BEV.ADRESSE WHERE ADRCD NOT IN (SELECT ADRCD FROM BEV.GEBAEUDE);
SELECT ADRCD FROM BEV.GEBAEUDE WHERE ADRCD NOT IN (SELECT ADRCD FROM BEV.ADRESSE);
	
SELECT DISTINCT HAUSNRZAHL2 FROM BEV.ADRESSE WHERE HAUSNRZAHL2 IS NULL;

-- address of the Austrian Parliament
SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE ADRCD = '6791990';

DROP VIEW BEV.ADRESSE_DENORMALIZED;
CREATE TABLE BEV.ADRESSE_DENORMALIZED AS SELECT * FROM BEV.V_ADRESSE_DENORMALIZED;

SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE lower(STRASSENNAME) LIKE lower('%renner%ring%') AND HAUSNRZAHL1 = '3';
SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE subcd IS NULL;

SELECT count(*) FROM BEV.ADRESSE_DENORMALIZED;

SELECT * FROM BEV.V_ADRESSE_DENORMALIZED LIMIT 1;
DROP VIEW IF EXISTS BEV.V_ADRESSE_DENORMALIZED;

-- addresses in Eisenstadt without buildings
SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE id like '%-' and gkz = '10101' and okz = '00001';

-- karl marx hof
SELECT * FROM BEV.ADRESSE_DENORMALIZED WHERE lower(strassenname) like lower('%heiligen%') and plz = '1190' and hausnrzahl1 = '82';
SELECT count(*) FROM BEV.ADRESSE_DENORMALIZED WHERE adrcd = '6904968'; -- 39

SELECT DISTINCT EPSG FROM BEV.ADRESSE;

SELECT HAUSNRVERBINDUNG1 FROM BEV.ADRESSE_DENORMALIZED
UNION
SELECT HAUSNRVERBINDUNG2 FROM BEV.ADRESSE_DENORMALIZED
UNION
SELECT HAUSNRVERBINDUNG3 FROM BEV.ADRESSE_DENORMALIZED;

SELECT strassenname, hofname FROM BEV.ADRESSE_DENORMALIZED WHERE hofname is not null and hofname != '';
SELECT strassenname, hofname, hausnrzahl1 FROM BEV.ADRESSE_DENORMALIZED WHERE hofname is not null and hofname != '' and hausnrzahl1 is not null and hausnrzahl1 != '';
SELECT strassenname, hofname, hausnrgebaeudebez FROM BEV.ADRESSE_DENORMALIZED WHERE hofname is not null and hofname != '' and hausnrgebaeudebez is not null and hausnrgebaeudebez != '';

SELECT min(length(strassenname)) FROM BEV.ADRESSE_DENORMALIZED WHERE strassenname is not null and strassenname != '';

SELECT STRASSENNAME, STRASSENNAMENZUSATZ, ORTSNAME FROM BEV.ADRESSE_DENORMALIZED WHERE SZUSADRBEST = 1 LIMIT 10;

DELETE FROM BEV.ADRESSE_DENORMALIZED WHERE gkz NOT LIKE '3%';
COMMIT;

select adresseden0_.id as id1_0_0_, adresseden0_.adrcd as adrcd2_0_0_, adresseden0_.bestimmungsart as bestimmu3_0_0_, adresseden0_.BESTIMMUNGSART_GEBAEUDE as BESTIMMU4_0_0_, adresseden0_.eigenschaft as eigensch5_0_0_, adresseden0_.epsg as epsg6_0_0_, adresseden0_.EPSG_GEBAEUDE as EPSG_GEB7_0_0_, adresseden0_.gemeindename as gemeinde8_0_0_, adresseden0_.gkz as gkz9_0_0_, adresseden0_.gnradresse as gnradre10_0_0_, adresseden0_.hauptadresse as hauptad11_0_0_, adresseden0_.hausnrbereich as hausnrb12_0_0_, adresseden0_.hausnrbuchstabe1 as hausnrb13_0_0_, adresseden0_.hausnrbuchstabe2 as hausnrb14_0_0_, adresseden0_.hausnrbuchstabe3 as hausnrb15_0_0_, adresseden0_.hausnrbuchstabe4 as hausnrb16_0_0_, adresseden0_.hausnrgebaeudebez as hausnrg17_0_0_, adresseden0_.hausnrtext as hausnrt18_0_0_, adresseden0_.hausnrverbindung1 as hausnrv19_0_0_, adresseden0_.hausnrverbindung2 as hausnrv20_0_0_, adresseden0_.hausnrverbindung3 as hausnrv21_0_0_, adresseden0_.hausnrzahl1 as hausnrz22_0_0_, adresseden0_.hausnrzahl2 as hausnrz23_0_0_, adresseden0_.hausnrzahl3 as hausnrz24_0_0_, adresseden0_.hausnrzahl4 as hausnrz25_0_0_, adresseden0_.hofname as hofname26_0_0_, adresseden0_.hw as hw27_0_0_, adresseden0_.HW_GEBAEUDE as HW_GEBA28_0_0_, adresseden0_.objektnummer as objektn29_0_0_, adresseden0_.okz as okz30_0_0_, adresseden0_.ortsname as ortsnam31_0_0_, adresseden0_.plz as plz32_0_0_, adresseden0_.quelladresse as quellad33_0_0_, adresseden0_.QUELLADRESSE_GEBAEUDE as QUELLAD34_0_0_, adresseden0_.rw as rw35_0_0_, adresseden0_.RW_GEBAEUDE as RW_GEBA36_0_0_, adresseden0_.skz as skz37_0_0_, adresseden0_.strassenname as strasse38_0_0_, adresseden0_.strassennamenzusatz as strasse39_0_0_, adresseden0_.subcd as subcd40_0_0_, adresseden0_.szusadrbest as szusadr41_0_0_, adresseden0_.zaehlsprengel as zaehlsp42_0_0_ from BEV.ADRESSE_DENORMALIZED adresseden0_ where adresseden0_.adrcd=? {1: '6791990'};
