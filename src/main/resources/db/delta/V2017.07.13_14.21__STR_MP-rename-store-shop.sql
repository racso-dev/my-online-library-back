
ALTER TABLE `store`
DROP FOREIGN KEY `id_bu_store`;
ALTER TABLE  `store`
RENAME TO   `shop` ;
ALTER TABLE  `shop`
ADD CONSTRAINT `id_bu_shop`
  FOREIGN KEY (`id_bu`)
  REFERENCES  `business_unit` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `user`
DROP FOREIGN KEY `id_store_user`;
ALTER TABLE  `user`
CHANGE COLUMN `id_store` `id_shop` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE  `user`
ADD CONSTRAINT `id_shop_user`
  FOREIGN KEY (`id_shop`)
  REFERENCES  `shop` (`id`);


ALTER TABLE `operation`
CHANGE COLUMN `name_store` `name_shop` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `id_store` `id_shop` BIGINT(20) NULL DEFAULT NULL ,
DROP INDEX `id_store_operation` ,
ADD INDEX `id_shop_operation` (`id_shop` ASC);

