CREATE TABLE `marketpay`.`business_unit` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `location` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`store` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `id_bu` BIGINT(20) NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `contract_number_idx_uq` (`contract_number` ASC),
  INDEX `id_bu_idx` (`id_bu` ASC),
  CONSTRAINT `id_bu_store`
    FOREIGN KEY (`id_bu`)
    REFERENCES `marketpay`.`business_unit` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `profile` INT NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `id_bu` BIGINT(20) NULL,
  `id_store` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `id_bu_idx` (`id_bu` ASC),
  INDEX `id_store_idx` (`id_store` ASC),
  CONSTRAINT `id_bu_user`
    FOREIGN KEY (`id_bu`)
    REFERENCES `marketpay`.`business_unit` (`id`),
  CONSTRAINT `id_store_user`
    FOREIGN KEY (`id_store`)
    REFERENCES `marketpay`.`store` (`contract_number`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`transaction` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `funding_date` DATETIME NOT NULL,
  `trade_date` DATETIME NOT NULL,
  `card_type` VARCHAR(45) NOT NULL,
  `sens` INT NOT NULL,
  `gross_amount` BIGINT(20) NOT NULL,
  `net_amount` BIGINT(20) NOT NULL,
  `contract_number` VARCHAR(45) NOT NULL,
  `name_store` VARCHAR(45) NOT NULL,
  `id_store` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `funding_date_idx` (`funding_date` ASC),
  INDEX `contract_number_idx` (`contract_number` ASC),
  CONSTRAINT `id_store_transactions`
    FOREIGN KEY (`id_store`)
    REFERENCES `marketpay`.`store` (`id`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `marketpay`.`block` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `funding_date` DATETIME(2) NOT NULL,
  `content` VARCHAR(45) NOT NULL,
  `status` INT NOT NULL,
  `id_bu` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_bu_idx` (`id_bu` ASC),
  CONSTRAINT `id_bu_block`
    FOREIGN KEY (`id_bu`)
    REFERENCES `marketpay`.`business_unit` (`id`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;
