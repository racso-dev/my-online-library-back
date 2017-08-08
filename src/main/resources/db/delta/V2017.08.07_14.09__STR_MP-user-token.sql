
CREATE TABLE user_token (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(250) NOT NULL,
  `expiration_date_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `token_UNIQUE` (`token` ASC))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;

