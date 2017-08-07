
ALTER TABLE user
ADD COLUMN `last_name` VARCHAR(45) NULL AFTER `id_shop`,
ADD COLUMN `first_name` VARCHAR(45) NULL AFTER `last_name`;

ALTER TABLE user
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC);

