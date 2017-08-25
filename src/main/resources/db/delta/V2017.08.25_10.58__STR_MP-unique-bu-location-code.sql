
ALTER TABLE `business_unit`
DROP INDEX `unique_code_bu` ,
ADD UNIQUE INDEX `unique_code_bu_location` (`location` ASC, `code_bu` ASC);

