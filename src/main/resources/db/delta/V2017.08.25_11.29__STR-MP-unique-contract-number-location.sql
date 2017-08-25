
ALTER TABLE `shop_contract_number`
ADD COLUMN `location` VARCHAR(2) NOT NULL AFTER `id_shop`,
ADD UNIQUE INDEX `unique_contract_number_location` (`location` ASC, `contract_number` ASC),
DROP INDEX `contract_number_UNIQUE` ;

