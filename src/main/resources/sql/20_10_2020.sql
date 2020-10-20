CREATE TABLE `zaplacrecepte_test`.`USER`
(
    `ID`                          VARCHAR(64)  NOT NULL,
    `USERNAME`                    VARCHAR(255) NOT NULL UNIQUE,
    `PASSWORD`                    VARCHAR(255) NULL,
    `FIRST_NAME`                  VARCHAR(255) NULL,
    `LAST_NAME`                   VARCHAR(255) NULL,
    `PHONE_NUMBER`                VARCHAR(255) NULL,
    `EMAIL`                       VARCHAR(255) NULL,
    `SMS_MESSAGE_REQUEST_PAYMENT` VARCHAR(255) NULL,
    `SMS_MESSAGE_COMPLETED`       VARCHAR(255) NULL,
    `CREATE_DATETIME`             DATETIME     NULL,
    `MODIFICATION_DATETIME`       DATETIME     NULL,
    PRIMARY KEY (`ID`)
);

ALTER TABLE `zaplacrecepte_test`.`PRESCRIPTION`
    ADD COLUMN `OWNER_ID` VARCHAR(64);
ALTER TABLE `zaplacrecepte_test`.`PRESCRIPTION`
    ADD FOREIGN KEY (OWNER_ID) REFERENCES `zaplacrecepte_test`.`USER` (`ID`);


CREATE TABLE `zaplacrecepte_demo`.`USER`
(
    `ID`                          VARCHAR(64)  NOT NULL,
    `USERNAME`                    VARCHAR(255) NOT NULL UNIQUE,
    `PASSWORD`                    VARCHAR(255) NULL,
    `FIRST_NAME`                  VARCHAR(255) NULL,
    `LAST_NAME`                   VARCHAR(255) NULL,
    `PHONE_NUMBER`                VARCHAR(255) NULL,
    `EMAIL`                       VARCHAR(255) NULL,
    `SMS_MESSAGE_REQUEST_PAYMENT` VARCHAR(255) NULL,
    `SMS_MESSAGE_COMPLETED`       VARCHAR(255) NULL,
    `CREATE_DATETIME`             DATETIME     NULL,
    `MODIFICATION_DATETIME`       DATETIME     NULL,
    PRIMARY KEY (`ID`)
);

ALTER TABLE `zaplacrecepte_demo`.`PRESCRIPTION`
    ADD COLUMN `OWNER_ID` VARCHAR(64);
ALTER TABLE `zaplacrecepte_demo`.`PRESCRIPTION`
    ADD FOREIGN KEY (OWNER_ID) REFERENCES `zaplacrecepte_demo`.`USER` (`ID`);

CREATE TABLE `zaplacrecepte_prod`.`USER`
(
    `ID`                          VARCHAR(64)  NOT NULL,
    `USERNAME`                    VARCHAR(255) NOT NULL UNIQUE,
    `PASSWORD`                    VARCHAR(255) NULL,
    `FIRST_NAME`                  VARCHAR(255) NULL,
    `LAST_NAME`                   VARCHAR(255) NULL,
    `PHONE_NUMBER`                VARCHAR(255) NULL,
    `EMAIL`                       VARCHAR(255) NULL,
    `SMS_MESSAGE_REQUEST_PAYMENT` VARCHAR(255) NULL,
    `SMS_MESSAGE_COMPLETED`       VARCHAR(255) NULL,
    `CREATE_DATETIME`             DATETIME     NULL,
    `MODIFICATION_DATETIME`       DATETIME     NULL,
    PRIMARY KEY (`ID`)
);

ALTER TABLE `zaplacrecepte_prod`.`PRESCRIPTION`
    ADD COLUMN `OWNER_ID` VARCHAR(64);
ALTER TABLE `zaplacrecepte_prod`.`PRESCRIPTION`
    ADD FOREIGN KEY (OWNER_ID) REFERENCES `zaplacrecepte_prod`.`USER` (`ID`);
