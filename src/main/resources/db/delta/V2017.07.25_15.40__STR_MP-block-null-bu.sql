
ALTER TABLE  `block`
CHANGE COLUMN `id_bu` `id_bu` BIGINT(20) NULL ,
ADD INDEX `fk_block_bu_idx` (`id_bu` ASC),
DROP INDEX `id_bu_idx` ;
ALTER TABLE  `block`
ADD CONSTRAINT `fk_block_bu`
  FOREIGN KEY (`id_bu`)
  REFERENCES  `business_unit` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

