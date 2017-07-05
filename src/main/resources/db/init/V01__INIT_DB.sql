CREATE TABLE `marketpay`.`business_unit` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `location` VARCHAR(2) NOT NULL,
  `bank_account_name` VARCHAR(45) NOT NULL,
  `bank_account_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `bank_account_number_UNIQUE` (`bank_account_number` ASC)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
