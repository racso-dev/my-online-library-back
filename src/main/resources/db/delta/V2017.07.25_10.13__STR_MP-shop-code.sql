
ALTER TABLE `marketpay`.`shop`
ADD COLUMN `code_al` VARCHAR(45) NOT NULL AFTER `name`,
ADD COLUMN `gln` VARCHAR(45) NULL AFTER `code_al`,
ADD COLUMN `atica` VARCHAR(45) NULL AFTER `gln`;

