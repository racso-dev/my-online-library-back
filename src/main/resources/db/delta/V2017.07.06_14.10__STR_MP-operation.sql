ALTER TABLE operation drop foreign key id_store_operation;
ALTER TABLE operation MODIFY name_store VARCHAR(45) NULL;
ALTER TABLE operation MODIFY id_store BIGINT(20) NULL;
