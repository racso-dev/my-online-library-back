CREATE TABLE `marketpay`.`store` (
  `id` INT NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `contract_number_UNIQUE` (`contract_number` ASC),
  CONSTRAINT `bu_id`
    FOREIGN KEY (`contract_number`)
    REFERENCES `marketpay`.`business_unit` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
