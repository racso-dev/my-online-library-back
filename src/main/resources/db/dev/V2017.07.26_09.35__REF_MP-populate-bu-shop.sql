
INSERT INTO business_unit VALUES (1,'C.C.CARREFOUR','ES','ES010','A28425270'),
                                 (2,'CARREFOUR NORTE','ES','ES020','B48609069'),
                                 (3,'CARREFOUR BELGIUM SA','BE','BE010','BE0448826918'),
                                 (4,'SPRL','BE','BE946','BE0539931494');

INSERT INTO shop VALUES (1,1,'S.S. DE LOS REYES','0055',NULL,NULL),
                        (2,1,'CARREFOUR AUGUS','0040',NULL,'40'),
                        (3,1,'CARREFOUR CALZA','0045',NULL,'45'),
                        (4,1,'CARREFOUR PONTE','0051',NULL,'000E'),
                        (5,2,'CARREFOUR SESTA','0102',NULL,'005S'),
                        (6,3,'CARREFOUR BELGIUM','1910','5400102119108',NULL),
                        (7,4,'CARREFOUR DRIVE 2','4002','5400102140010',NULL),
                        (8,3,'SIMPLY YOU BOX','1911','5400102619110',NULL);

INSERT INTO shop_contract_number VALUES (1,1,'2703773'),
                                        (2,2,'2704736'),
                                        (3,3,'2704737'),
                                        (4,4,'2704738'),
                                        (5,5,'2704739'),
                                        (6,6,'2705427'),
                                        (7,7,'2705733'),
                                        (8,8,'3080185');

UPDATE user SET id_bu = 3 where id = 2;
UPDATE user SET id_bu = 1 where id = 3;
UPDATE user SET id_bu = 4 where id = 4;
UPDATE user SET id_shop = 6 where id = 5;
UPDATE user SET id_shop = 7 where id = 6;
UPDATE user SET id_shop = 3 where id = 7;

