
ALTER TABLE `marketpay`.`business_unit`
CHANGE COLUMN `client_id` `code_bu` VARCHAR(50) NOT NULL ,
ADD COLUMN `cif` VARCHAR(45) NULL AFTER `code_bu`;

