
ALTER TABLE `user`
CHANGE COLUMN `password` `password` VARCHAR(256) NOT NULL ,
ADD UNIQUE INDEX `unique_login` (`login` ASC);
