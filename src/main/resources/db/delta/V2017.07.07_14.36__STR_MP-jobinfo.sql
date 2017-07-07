CREATE TABLE `marketpay`.`job_information` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `date` DATETIME(2) NOT NULL,
  `status` INT NOT NULL,
  `filename` VARCHAR(45) NOT NULL,
  `filetype` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8;
