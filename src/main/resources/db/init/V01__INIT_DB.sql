CREATE TABLE db_marketpay.transactions (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  contract_number BIGINT(20) NOT NULL,
  store_name VARCHAR(45) NOT NULL,
  trade_date DATETIME(2) NOT NULL,
  card_type VARCHAR(45) NOT NULL,
  sens VARCHAR(45) NOT NULL,
  gross_amount BIGINT(20) NOT NULL,
  commission BIGINT(20) NOT NULL,
  net_amount BIGINT(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE db_marketpay.users (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(45) NOT NULL,
  lastname VARCHAR(45) NOT NULL,
  username VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  authorization TINYINT(1) NOT NULL,
  admin TINYINT(1) NOT NULL,
  datecreate DATETIME(2) NOT NULL,
  datemodify DATETIME(2),
  PRIMARY KEY (id)
);
