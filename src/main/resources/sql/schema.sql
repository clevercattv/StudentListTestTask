CREATE TABLE IF NOT EXISTS `STUDENT` (
`id` INTEGER PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(26) NOT NULL,
`last_name` VARCHAR(26) NOT NULL,
`university` VARCHAR(127) NOT NULL,
`specialty` VARCHAR(127) NOT NULL,
`semester` INTEGER(10) NOT NULL,
`entry_date` TIMESTAMP NOT NULL,
`age` INTEGER NOT NULL,
`creation_time` TIMESTAMP NOT NULL
);
