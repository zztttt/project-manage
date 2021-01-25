use project;

DROP TABLE IF EXISTS projectInfo;
CREATE TABLE `project`.`projectinfo` (
  `projectId` BIGINT(64) NOT NULL,
  `projectName` VARCHAR(50) NULL,
  `invester` VARCHAR(50) NULL,
  `projectType` VARCHAR(20) NULL,
  `projectCategory` VARCHAR(20) NULL,
  `date` VARCHAR(10) NULL,
  `status` VARCHAR(20) NULL,
  PRIMARY KEY (`projectId`));