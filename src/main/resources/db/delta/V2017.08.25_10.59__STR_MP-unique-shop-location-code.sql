
ALTER TABLE `shop`
ADD COLUMN `location` VARCHAR(2) NOT NULL AFTER `name`,
ADD UNIQUE INDEX `unique_code_al_location` (`location` ASC, `code_al` ASC),
DROP INDEX `code_al_UNIQUE` ;

