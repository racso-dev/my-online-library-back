ALTER TABLE `operation`
ADD COLUMN `id_block` BIGINT(20) NULL AFTER `operation_type`,
ADD INDEX `fk_operation_block_idx` (`id_block` ASC);
ALTER TABLE `operation`
ADD CONSTRAINT `fk_operation_block`
FOREIGN KEY (`id_block`)
REFERENCES `block` (`id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
