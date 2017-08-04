
ALTER TABLE `marketpay`.`user`
ADD COLUMN `last_name` VARCHAR(45) NULL AFTER `id_shop`,
ADD COLUMN `first_name` VARCHAR(45) NULL AFTER `last_name`;

ALTER TABLE `marketpay`.`user`
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC);

