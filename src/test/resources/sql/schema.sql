CREATE TABLE IF NOT EXISTS `STUDENT`
(
    `id`            INTEGER PRIMARY KEY AUTO_INCREMENT,
    `first_name`    VARCHAR(26) NOT NULL,
    `last_name`     VARCHAR(26) NOT NULL,
    `university`    VARCHAR(63) NOT NULL,
    `specialty`     VARCHAR(63) NOT NULL,
    `semester`      INTEGER(2)  NOT NULL,
    `age`           INTEGER(3)  NOT NULL,
    `creation_time` TIMESTAMP   NOT NULL,
    CONSTRAINT uniqueConstraint UNIQUE (`first_name`, `last_name`, `university`, `specialty`, `semester`, `age`)
);


INSERT INTO `STUDENT`(`first_name`, `last_name`, `university`, `specialty`, `semester`, `age`, `creation_time`)
VALUES ('Anny', 'Hallbord', 'Francis Marion University', 'Indian Statistical Institute', 11, 38, now())
