CREATE TABLE  user
(
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  profile VARCHAR(45) NOT NULL,
  login VARCHAR(45) NOT NULL,
  password VARCHAR(256) NOT NULL,
  PRIMARY KEY (id)
)

