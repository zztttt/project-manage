use project;

DROP TABLE IF EXISTS projectInfo;
DROP TABLE IF EXISTS fileInfo;

CREATE TABLE `project`.`projectInfo` (
  `id` BIGINT(64) NOT NULL,
  `projectName` VARCHAR(50) NULL,
  `investor` VARCHAR(50) NULL,
  `projectType` VARCHAR(20) NULL,
  `projectCategory` VARCHAR(20) NULL,
  `date` VARCHAR(10) NULL,
  `status` VARCHAR(20) NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE `project`.`fileInfo` (
  `id` BIGINT(64) NOT NULL,
  `projectName` VARCHAR(50) NULL,
  `fileName` VARCHAR(50) NULL,
  `uploadResult` VARCHAR(20) NULL,
  `uploader` VARCHAR(20) NULL,
  `uploadTime` VARCHAR(20) NULL,
  `isUploaded` TINYINT NULL,
  PRIMARY KEY (`id`));