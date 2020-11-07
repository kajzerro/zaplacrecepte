ALTER TABLE `zaplacrecepte_test`.`USER`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;
ALTER TABLE `zaplacrecepte_demo`.`USER`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;
ALTER TABLE `zaplacrecepte_prod`.`USER`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;

ALTER TABLE `zaplacrecepte_test`.`PRESCRIPTION`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;
ALTER TABLE `zaplacrecepte_demo`.`PRESCRIPTION`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;
ALTER TABLE `zaplacrecepte_prod`.`PRESCRIPTION`
    ADD COLUMN `FEE_INCLUDED` boolean not null default 1;
