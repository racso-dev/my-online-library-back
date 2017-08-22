ALTER TABLE `operation`
ADD COLUMN `id_job_history` BIGINT(20) NULL AFTER `create_date`,
ADD INDEX `fk_operation_job_history_idx` (`id_job_history` ASC);
ALTER TABLE `operation`
ADD CONSTRAINT `fk_operation_job_history`
FOREIGN KEY (`id_job_history`)
REFERENCES `job_history` (`id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
