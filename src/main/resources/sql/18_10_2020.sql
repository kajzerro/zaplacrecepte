CREATE SCHEMA `zaplacrecepte_test` DEFAULT CHARACTER SET utf8;

CREATE USER 'user_test' IDENTIFIED BY 'BwcKbomMpBx3vrkpcHRWH3QJnxCUDG';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON `zaplacrecepte_test`.* TO `user_test`;

CREATE TABLE `zaplacrecepte_test`.`PRESCRIPTION`
(
    `ID`                  VARCHAR(64)  NOT NULL,
    `FIRST_NAME`          VARCHAR(255) NULL,
    `LAST_NAME`           VARCHAR(255) NULL,
    `PESEL`               VARCHAR(255) NULL,
    `POSTAL_CODE`         VARCHAR(255) NULL,
    `REMARKS`             TEXT         NULL,
    `PHONE_NUMBER`        VARCHAR(255) NULL,
    `EMAIL`               VARCHAR(255) NULL,
    `STATUS`              VARCHAR(255) NULL,
    `CREATE_DATETIME`     DATETIME     NULL,
    `ORDER_ID`            INTEGER      NULL,
    `PAYMENT_TOKEN`       VARCHAR(255) NULL,
    `ORDER_URL`           VARCHAR(255) NULL,
    `PRESCRIPTION_NUMBER` VARCHAR(255) NULL,
    `ERRORS`              TEXT         NULL,
    PRIMARY KEY (`ID`)
);

CREATE SCHEMA `zaplacrecepte_demo` DEFAULT CHARACTER SET utf8;

CREATE USER 'user_demo' IDENTIFIED BY 'a8tl3bWTK8az8wtNoHtXbPNYF9anYT';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON `zaplacrecepte_demo`.* TO `user_demo`;

CREATE TABLE `zaplacrecepte_demo`.`PRESCRIPTION`
(
    `ID`                  VARCHAR(64)  NOT NULL,
    `FIRST_NAME`          VARCHAR(255) NULL,
    `LAST_NAME`           VARCHAR(255) NULL,
    `PESEL`               VARCHAR(255) NULL,
    `POSTAL_CODE`         VARCHAR(255) NULL,
    `REMARKS`             TEXT         NULL,
    `PHONE_NUMBER`        VARCHAR(255) NULL,
    `EMAIL`               VARCHAR(255) NULL,
    `STATUS`              VARCHAR(255) NULL,
    `CREATE_DATETIME`     DATETIME     NULL,
    `ORDER_ID`            INTEGER      NULL,
    `PAYMENT_TOKEN`       VARCHAR(255) NULL,
    `ORDER_URL`           VARCHAR(255) NULL,
    `PRESCRIPTION_NUMBER` VARCHAR(255) NULL,
    `ERRORS`              TEXT         NULL,
    PRIMARY KEY (`ID`)
);


CREATE SCHEMA `zaplacrecepte_prod` DEFAULT CHARACTER SET utf8;

CREATE USER 'user_prod' IDENTIFIED BY 'WOObw5gGNB3L1wg2qQyVxl1h9muVrE';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON `zaplacrecepte_prod`.* TO `user_prod`;

CREATE TABLE `zaplacrecepte_prod`.`PRESCRIPTION`
(
    `ID`                  VARCHAR(64)  NOT NULL,
    `FIRST_NAME`          VARCHAR(255) NULL,
    `LAST_NAME`           VARCHAR(255) NULL,
    `PESEL`               VARCHAR(255) NULL,
    `POSTAL_CODE`         VARCHAR(255) NULL,
    `REMARKS`             TEXT         NULL,
    `PHONE_NUMBER`        VARCHAR(255) NULL,
    `EMAIL`               VARCHAR(255) NULL,
    `STATUS`              VARCHAR(255) NULL,
    `CREATE_DATETIME`     DATETIME     NULL,
    `ORDER_ID`            INTEGER      NULL,
    `PAYMENT_TOKEN`       VARCHAR(255) NULL,
    `ORDER_URL`           VARCHAR(255) NULL,
    `PRESCRIPTION_NUMBER` VARCHAR(255) NULL,
    `ERRORS`              TEXT         NULL,
    PRIMARY KEY (`ID`)
);