/*
 * Create the database structure
 * 
 * This must be applied to an empty database. The schema BEV or any tables in it are not allowed to exists.
 */

 CREATE USER IF NOT EXISTS SA SALT 'a54aa622ff3ccebb' HASH '535100814a5d47df7b5d1f4df7e29d1b558fb5e3cdde195bda10704e73a32dae' ADMIN;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
 CREATE SCHEMA IF NOT EXISTS BEV AUTHORIZATION SA;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
 CREATE CACHED TABLE BEV.ADRESSE(
    ADRCD VARCHAR NOT NULL SELECTIVITY 100,
    GKZ VARCHAR SELECTIVITY 1,
    OKZ VARCHAR SELECTIVITY 1,
    PLZ VARCHAR SELECTIVITY 1,
    SKZ VARCHAR SELECTIVITY 5,
    ZAEHLSPRENGEL VARCHAR SELECTIVITY 1,
    HAUSNRTEXT VARCHAR SELECTIVITY 1,
    HAUSNRZAHL1 VARCHAR SELECTIVITY 4,
    HAUSNRBUCHSTABE1 VARCHAR SELECTIVITY 1,
    HAUSNRVERBINDUNG1 VARCHAR SELECTIVITY 1,
    HAUSNRZAHL2 VARCHAR SELECTIVITY 1,
    HAUSNRBUCHSTABE2 VARCHAR SELECTIVITY 1,
    HAUSNRBEREICH VARCHAR SELECTIVITY 1,
    GNRADRESSE VARCHAR SELECTIVITY 1,
    HOFNAME VARCHAR SELECTIVITY 1,
    RW VARCHAR SELECTIVITY 86,
    HW VARCHAR SELECTIVITY 86,
    EPSG VARCHAR SELECTIVITY 1,
    QUELLADRESSE VARCHAR SELECTIVITY 1,
    BESTIMMUNGSART VARCHAR SELECTIVITY 1
);
 -- 0 +/- SELECT COUNT(*) FROM BEV.ADRESSE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_E7 ON BEV.ADRESSE(ADRCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
 CREATE INDEX BEV.INDEX_E ON BEV.ADRESSE(GKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
 CREATE INDEX BEV.INDEX_E7 ON BEV.ADRESSE(OKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
 CREATE INDEX BEV.INDEX_E72 ON BEV.ADRESSE(PLZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
 CREATE INDEX BEV.INDEX_E722 ON BEV.ADRESSE(SKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
 CREATE INDEX BEV.INDEX_E722B ON BEV.ADRESSE(ZAEHLSPRENGEL);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 CREATE CACHED TABLE BEV.ADRESSE_GST(
    ADRCD VARCHAR,
    KGNR VARCHAR,
    GSTNR VARCHAR,
    LFDNR VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
 -- 0 +/- SELECT COUNT(*) FROM BEV.ADRESSE_GST;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
 CREATE CACHED TABLE BEV.GEBAEUDE_FUNKTION(
    ADRCD VARCHAR,
    SUBCD VARCHAR,
    OBJEKTNUMMER VARCHAR,
    OBJFUNKTKENNZIFFER VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
 -- 0 +/- SELECT COUNT(*) FROM BEV.GEBAEUDE_FUNKTION;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
 CREATE INDEX BEV.INDEX_7 ON BEV.GEBAEUDE_FUNKTION(ADRCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
 CREATE INDEX BEV.INDEX_71 ON BEV.GEBAEUDE_FUNKTION(SUBCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
 CREATE CACHED TABLE BEV.GEBAEUDE(
    ADRCD VARCHAR NOT NULL,
    SUBCD VARCHAR NOT NULL,
    OBJEKTNUMMER VARCHAR,
    HAUPTADRESSE VARCHAR,
    HAUSNRVERBINDUNG2 VARCHAR,
    HAUSNRZAHL3 VARCHAR,
    HAUSNRBUCHSTABE3 VARCHAR,
    HAUSNRVERBINDUNG3 VARCHAR,
    HAUSNRZAHL4 VARCHAR,
    HAUSNRBUCHSTABE4 VARCHAR,
    HAUSNRGEBAEUDEBEZ VARCHAR,
    RW VARCHAR,
    HW VARCHAR,
    EPSG VARCHAR,
    QUELLADRESSE VARCHAR,
    BESTIMMUNGSART VARCHAR,
    EIGENSCHAFT VARCHAR
);                                                                                                                                                                                                                                                                                                                   
 -- 0 +/- SELECT COUNT(*) FROM BEV.GEBAEUDE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_85 ON BEV.GEBAEUDE(ADRCD, SUBCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
 CREATE INDEX BEV.INDEX_8 ON BEV.GEBAEUDE(ADRCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
 CREATE INDEX BEV.INDEX_85 ON BEV.GEBAEUDE(SUBCD);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
 CREATE CACHED TABLE BEV.GEMEINDE(
    GKZ VARCHAR NOT NULL,
    GEMEINDENAME VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
 -- 0 +/- SELECT COUNT(*) FROM BEV.GEMEINDE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_9 ON BEV.GEMEINDE(GKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
 CREATE CACHED TABLE BEV.ORTSCHAFT(
    GKZ VARCHAR,
    OKZ VARCHAR NOT NULL,
    ORTSNAME VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
 -- 0 +/- SELECT COUNT(*) FROM BEV.ORTSCHAFT;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_F ON BEV.ORTSCHAFT(OKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 CREATE INDEX BEV.INDEX_F ON BEV.ORTSCHAFT(GKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
 CREATE INDEX BEV.INDEX_F4 ON BEV.ORTSCHAFT(OKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
 CREATE CACHED TABLE BEV.STRASSE(
    SKZ VARCHAR NOT NULL,
    STRASSENNAME VARCHAR,
    STRASSENNAMENZUSATZ VARCHAR,
    SZUSADRBEST VARCHAR,
    GKZ VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
 -- 0 +/- SELECT COUNT(*) FROM BEV.STRASSE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_B ON BEV.STRASSE(SKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
 CREATE INDEX BEV.INDEX_B ON BEV.STRASSE(GKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
 CREATE CACHED TABLE BEV.ZAEHLSPRENGEL(
    GKZ VARCHAR NOT NULL,
    ZAEHLSPRENGEL VARCHAR NOT NULL,
    ZAEHLSPRENGELNAME VARCHAR
);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
 -- 0 +/- SELECT COUNT(*) FROM BEV.ZAEHLSPRENGEL;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
 CREATE PRIMARY KEY BEV.PRIMARY_KEY_6 ON BEV.ZAEHLSPRENGEL(GKZ, ZAEHLSPRENGEL);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
 CREATE INDEX BEV.INDEX_6 ON BEV.ZAEHLSPRENGEL(GKZ);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
 CREATE INDEX BEV.INDEX_6E ON BEV.ZAEHLSPRENGEL(ZAEHLSPRENGEL);      
 
 CREATE TABLE BEV.ADRESSE_DENORMALIZED (
		ID VARCHAR(2147483647) NOT NULL,
		ADRCD VARCHAR(2147483647),
		GKZ VARCHAR(2147483647),
		OKZ VARCHAR(2147483647),
		PLZ VARCHAR(2147483647),
		SKZ VARCHAR(2147483647),
		ZAEHLSPRENGEL VARCHAR(2147483647),
		HAUSNRTEXT VARCHAR(2147483647),
		HAUSNRZAHL1 VARCHAR(2147483647),
		HAUSNRBUCHSTABE1 VARCHAR(2147483647),
		HAUSNRVERBINDUNG1 VARCHAR(2147483647),
		HAUSNRZAHL2 VARCHAR(2147483647),
		HAUSNRBUCHSTABE2 VARCHAR(2147483647),
		HAUSNRBEREICH VARCHAR(2147483647),
		GNRADRESSE VARCHAR(2147483647),
		HOFNAME VARCHAR(2147483647),
		RW VARCHAR(2147483647),
		HW VARCHAR(2147483647),
		EPSG VARCHAR(2147483647),
		QUELLADRESSE VARCHAR(2147483647),
		BESTIMMUNGSART VARCHAR(2147483647),
		ORTSNAME VARCHAR(2147483647),
		STRASSENNAME VARCHAR(2147483647),
		STRASSENNAMENZUSATZ VARCHAR(2147483647),
		SZUSADRBEST VARCHAR(2147483647),
		GEMEINDENAME VARCHAR(2147483647),
		SUBCD VARCHAR(2147483647),
		OBJEKTNUMMER VARCHAR(2147483647),
		HAUPTADRESSE VARCHAR(2147483647),
		HAUSNRVERBINDUNG2 VARCHAR(2147483647),
		HAUSNRZAHL3 VARCHAR(2147483647),
		HAUSNRBUCHSTABE3 VARCHAR(2147483647),
		HAUSNRVERBINDUNG3 VARCHAR(2147483647),
		HAUSNRZAHL4 VARCHAR(2147483647),
		HAUSNRBUCHSTABE4 VARCHAR(2147483647),
		HAUSNRGEBAEUDEBEZ VARCHAR(2147483647),
		RW_GEBAEUDE VARCHAR(2147483647),
		HW_GEBAEUDE VARCHAR(2147483647),
		EPSG_GEBAEUDE VARCHAR(2147483647),
		EIGENSCHAFT VARCHAR(2147483647),
		QUELLADRESSE_GEBAEUDE VARCHAR(2147483647),
		BESTIMMUNGSART_GEBAEUDE VARCHAR(2147483647)
	);
CREATE PRIMARY KEY BEV.INDEX_BEV_AD_PK ON BEV.ADRESSE_DENORMALIZED (ID);
CREATE INDEX BEV.INDEX_CCA ON BEV.ADRESSE_DENORMALIZED (GKZ ASC);
CREATE INDEX BEV.INDEX_CCA1 ON BEV.ADRESSE_DENORMALIZED (SKZ ASC);
CREATE INDEX BEV.INDEX_C ON BEV.ADRESSE_DENORMALIZED (ADRCD ASC);
CREATE INDEX BEV.INDEX_CCA15 ON BEV.ADRESSE_DENORMALIZED (PLZ ASC);
CREATE INDEX BEV.INDEX_CC ON BEV.ADRESSE_DENORMALIZED (OKZ ASC);

CREATE OR REPLACE VIEW BEV.V_ADRESSE_DENORMALIZED AS
SELECT nvl2(a.ADRCD, a.ADRCD, '') || '-' || nvl2(b.SUBCD, b.SUBCD, '') id, a.*, o.ORTSNAME, s.STRASSENNAME, s.STRASSENNAMENZUSATZ, s.SZUSADRBEST, g.GEMEINDENAME, b.SUBCD, b.OBJEKTNUMMER, b.HAUPTADRESSE, b.HAUSNRVERBINDUNG2, b.HAUSNRZAHL3, b.HAUSNRBUCHSTABE3, b.HAUSNRVERBINDUNG3, b.HAUSNRZAHL4, b.HAUSNRBUCHSTABE4, b.HAUSNRGEBAEUDEBEZ, b.RW RW_GEBAEUDE, b.HW HW_GEBAEUDE, b.EPSG EPSG_GEBAEUDE, b.EIGENSCHAFT, b.QUELLADRESSE QUELLADRESSE_GEBAEUDE, b.BESTIMMUNGSART BESTIMMUNGSART_GEBAEUDE
FROM 
	BEV.ADRESSE a
	JOIN BEV.STRASSE s ON a.skz = s.skz
	JOIN BEV.ORTSCHAFT o ON a.okz = o.okz
	JOIN BEV.GEMEINDE g ON a.gkz = g.gkz
	LEFT JOIN BEV.GEBAEUDE b ON a.adrcd = b.adrcd
;
COMMIT;

