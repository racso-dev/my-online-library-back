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

CREATE TABLE `marketpay`.`users` (
  `id` INT NOT NULL,
  `profile` SMALLINT(5) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `account_number_bu` VARCHAR(45) NULL,
  `id_store` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `account_number_bu_idx` (`account_number_bu` ASC),
  INDEX `id_store_idx` (`id_store` ASC),
  CONSTRAINT `account_number_bu`
    FOREIGN KEY (`account_number_bu`)
    REFERENCES `marketpay`.`business_unit` (`bank_account_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_store`
    FOREIGN KEY (`id_store`)
    REFERENCES `marketpay`.`store` (`contract_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`transactions` (
  `id` INT NOT NULL,
  `funding_date` DATETIME NOT NULL,
  `id_client` BIGINT(20) NOT NULL,
  `trade_date` DATETIME NOT NULL,
  `card_type` VARCHAR(45) NOT NULL,
  `sens` VARCHAR(45) NOT NULL,
  `gross_amount` BIGINT(20) NOT NULL,
  `net_amount` BIGINT(20) NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `funding_date_idx` (`funding_date` ASC),
  INDEX `contract_number_idx` (`contract_number` ASC))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;
