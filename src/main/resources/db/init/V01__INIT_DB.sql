CREATE TABLE `marketpay`.`business_unit` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `location` VARCHAR(2) NOT NULL,
  `bank_account_name` VARCHAR(45) NOT NULL,
  `bank_account_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `bank_account_number_UNIQUE` (`bank_account_number` ASC)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`store` (
  `id` INT NOT NULL,
  `id_bu` INT NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `contract_number` (`contract_number` ASC),
  INDEX `fk_id_bu_idx` (`id_bu` ASC),
  CONSTRAINT `fk_id_bu`
    FOREIGN KEY (`id_bu`)
    REFERENCES `marketpay`.`business_unit` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
