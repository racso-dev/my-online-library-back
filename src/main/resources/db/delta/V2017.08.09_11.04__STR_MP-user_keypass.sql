
CREATE TABLE user_key_pass (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `key_pass` VARCHAR(255) NOT NULL,
  `id_user` BIGINT(20) NOT NULL,
  `expiration_date_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_user_UNIQUE` (`id_user` ASC),
  UNIQUE INDEX `key_pass_UNIQUE` (`key_pass` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `marketpay`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
