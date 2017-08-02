
CREATE TABLE  `shop_contract_number` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_shop` BIGINT(20) NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `contract_number_UNIQUE` (`contract_number` ASC),
  INDEX `fk_shop_idx` (`id_shop` ASC),
  CONSTRAINT `fk_shop`
    FOREIGN KEY (`id_shop`)
    REFERENCES  `shop` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE  `shop`
DROP COLUMN `contract_number`,
ADD UNIQUE INDEX `code_al_UNIQUE` (`code_al` ASC),
DROP INDEX `contract_number_idx_uq` ;

